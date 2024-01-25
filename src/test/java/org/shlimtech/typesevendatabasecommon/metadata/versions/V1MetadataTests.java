package org.shlimtech.typesevendatabasecommon.metadata.versions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.shlimtech.typesevendatabasecommon.BaseTest;
import org.shlimtech.typesevendatabasecommon.metadata.Metadata;
import org.shlimtech.typesevendatabasecommon.service.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;

import java.io.File;

public class V1MetadataTests extends BaseTest {

    @Autowired
    private V1Metadata v1Metadata;
    @Autowired
    private MetadataService metadataService;
    private final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory()).findAndRegisterModules();

    @Test
    public void simpleMetricsTest() {
        Metadata a = metadataService.generateMetadata(v1Metadata);
        Metadata b = metadataService.generateMetadata(v1Metadata);

        int metric = v1Metadata.metric(a, b);

        Assert.isTrue(metric == 0, "metric must be zero for plain metadata");
    }

    @Test
    public void somethingChangedMetricsTest() {
        Metadata a = metadataService.generateMetadata(v1Metadata);
        Metadata b = metadataService.generateMetadata(v1Metadata);

        a.getMetadataEntrySets().get(2).getEntries().get(0).setFlag(true);
        b.getMetadataEntrySets().get(2).getEntries().get(0).setFlag(true);

        int metric = v1Metadata.metric(a, b);

        Assert.isTrue(metric == 1, "bad metrics");
    }

    @Test
    public void ignoreGenderAndTargetMetricsTest() {
        Metadata a = metadataService.generateMetadata(v1Metadata);
        Metadata b = metadataService.generateMetadata(v1Metadata);

        // gender
        a.getMetadataEntrySets().get(0).getEntries().get(0).setFlag(true);
        b.getMetadataEntrySets().get(0).getEntries().get(0).setFlag(true);

        // target
        a.getMetadataEntrySets().get(1).getEntries().get(0).setFlag(true);
        b.getMetadataEntrySets().get(1).getEntries().get(0).setFlag(true);

        // other
        a.getMetadataEntrySets().get(2).getEntries().get(0).setFlag(true);
        b.getMetadataEntrySets().get(2).getEntries().get(0).setFlag(true);

        int metric = v1Metadata.metric(a, b);

        Assert.isTrue(metric == 1, "bad metrics");
    }

    @Test
    public void validationTest() {
        Metadata a = metadataService.generateMetadata(v1Metadata);
        Assert.isTrue(v1Metadata.validateMetadata(a), "not validated correctly");

        a.getMetadataEntrySets().get(0).getEntries().get(0).setFlag(false);
        Assert.isTrue(!v1Metadata.validateMetadata(a), "not validated correctly");
    }

    @Test
    public void countValidationTest() {
        Metadata a = metadataService.generateMetadata(v1Metadata);
        a.getMetadataEntrySets().get(0).setMaximumChoices(1);
        a.getMetadataEntrySets().get(0).getEntries().get(0).setFlag(true);
        Assert.isTrue(v1Metadata.validateMetadata(a), "not validated correctly");
        a.getMetadataEntrySets().get(0).getEntries().get(1).setFlag(true);
        Assert.isTrue(!v1Metadata.validateMetadata(a), "not validated correctly");
    }

    @Test
    @SneakyThrows
    public void loadFromYamlTest() {
        File metafile = new ClassPathResource("metadataVersions/v1.yaml").getFile();
        Assert.isTrue(metafile.exists(), "metafile not found");
        Metadata metadata = objectMapper.readValue(metafile, Metadata.class);
        Assert.isTrue(metadata.getVersion().equals("v1"), "incorrect version");
        Assert.isTrue(v1Metadata.validateMetadata(metadata), "not validated");
        Assert.isTrue(metadata.getMetadataEntrySets().get(2).getName().equals("Увлечения"), "bad metadata");
        Assert.isTrue(metadata.getMetadataEntrySets().get(2).getEntries().get(0).getName().equals("Покер"), "bad metadata");
        Assert.isTrue(metadata.getSelectedUsers() != null, "bad metadata");
    }

}
