package org.shlimtech.typesevendatabasecommon.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.shlimtech.typesevendatabasecommon.dto.MetadataDTO;
import org.shlimtech.typesevendatabasecommon.mapper.MetadataMapper;
import org.shlimtech.typesevendatabasecommon.metadata.Metadata;
import org.shlimtech.typesevendatabasecommon.metadata.versions.V1Metadata;
import org.shlimtech.typesevendatabasecommon.metadata.versions.VersionedMetadataBuilder;
import org.shlimtech.typesevendatabasecommon.model.Type7Metadata;
import org.shlimtech.typesevendatabasecommon.repository.Type7MetadataRepository;
import org.shlimtech.typesixdatabasecommon.dto.UserDTO;
import org.shlimtech.typesixdatabasecommon.service.UserService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class MetadataService {

    private List<VersionedMetadataBuilder> allVersions;
    private final V1Metadata latest;
    private final UserService userService;
    private final Type7MetadataRepository metadataRepository;
    private final ModelMapper modelMapper;
    private final MetadataMapper metadataMapper;
    private final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory()).findAndRegisterModules();
    private final ResourceLoader resourceLoader;

    public Metadata generateMetadata() {
        return generateMetadata(latest);
    }

    @SneakyThrows
    public Metadata generateMetadata(VersionedMetadataBuilder builder) {
        Resource resource = resourceLoader.getResource(builder.getMetadataTemplatePath());
        // HERE ONLY INPUT STREAM CAN BE USED BECAUSE FILE WILL BE INSIDE JAR
        return objectMapper.readValue(resource.getInputStream(), Metadata.class);
    }

    public boolean hasLatestVersion(Metadata metadata) {
        return metadata.getVersion().equals(latest.getVersion());
    }

    public VersionedMetadataBuilder findBuilder(String version) {
        return allVersions.stream().filter(builder -> builder.getVersion().equals(version)).findAny().get();
    }

    public Metadata update(Metadata metadata) {
        // TODO create version upgrade mechanics
        return metadata;
    }

    private Type7Metadata getUserMetadataEntity(int userId) {
        UserDTO user = userService.loadUser(userId);

        Type7Metadata entity = metadataRepository.findByUserId(user.getId());

        if (entity == null) {
            entity = new Type7Metadata(user.getId(), generateMetadata());
            metadataRepository.save(entity);
        }

        return entity;
    }

    @Transactional
    public Metadata loadUserMetadata(int userID) {
        Metadata metadata = getUserMetadataEntity(userID).getMetadata();
        if (!hasLatestVersion(metadata)) {
            metadata = update(metadata);
            // TODO save updated metadata
        }
        return metadata;
    }

    @Transactional
    public void saveUserMetadata(int userId, Metadata metadata) {
        Metadata toSet = metadata;
        if (!hasLatestVersion(toSet)) {
            toSet = update(toSet);
        }
        if (!latest.validateMetadata(toSet)) {
            throw new RuntimeException("metadata validation error");
        }

        Type7Metadata entity = getUserMetadataEntity(userId);
        entity.setMetadata(toSet);
        metadataRepository.save(entity);
    }

    @Transactional
    public void saveUserMetadataDTO(int userId, MetadataDTO metadataDTO) {
        saveUserMetadata(userId, metadataMapper.fromDTO(metadataDTO));
    }

    @Transactional
    public MetadataDTO loadUserMetadataDTO(int userId) {
        return metadataMapper.toDTO(loadUserMetadata(userId));
    }

    public int metaMetric(Metadata a, Metadata b) {
        return latest.metric(a, b);
    }

    public boolean canMatch(Metadata a, Metadata b) {
        return a.getVersion().equals(b.getVersion()) && latest.canMatch(a, b);
    }

    public boolean isValid(Metadata metadata) {
        return metadata.getVersion().equals(latest.getVersion()) && latest.validateMetadata(metadata);
    }

}
