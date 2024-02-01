package org.shlimtech.typesevendatabasecommon.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.shlimtech.typesevendatabasecommon.dto.MetadataDTO;
import org.shlimtech.typesevendatabasecommon.metadata.Metadata;
import org.shlimtech.typesixdatabasecommon.dto.UserDTO;
import org.shlimtech.typesixdatabasecommon.service.UserService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MetadataMapper {

    private final ModelMapper modelMapper;
    private final UserService userService;

    public Metadata fromDTO(MetadataDTO dto) {
        return new Metadata(dto.getVersion(), dto.getMetadataEntrySets(), dto.getSelectedUsers().stream().map(UserDTO::getId).toList(), false);
    }

    public MetadataDTO toDTO(Metadata metadata) {
        MetadataDTO metadataDTO = modelMapper.map(metadata, MetadataDTO.class);
        metadataDTO.getSelectedUsers().clear();
        metadataDTO.getSelectedUsers().addAll(metadata.getSelectedUsers().stream().map(userService::loadUser).toList());
        return metadataDTO;
    }

}
