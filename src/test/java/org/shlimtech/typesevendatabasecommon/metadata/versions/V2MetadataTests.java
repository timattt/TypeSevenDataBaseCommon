package org.shlimtech.typesevendatabasecommon.metadata.versions;

import org.junit.jupiter.api.Test;
import org.shlimtech.typesevendatabasecommon.BaseTest;
import org.shlimtech.typesevendatabasecommon.metadata.Metadata;
import org.shlimtech.typesevendatabasecommon.service.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

public class V2MetadataTests extends BaseTest {

    @Autowired
    private MetadataService metadataService;

    @Autowired
    private V2Metadata v2Metadata;

    @Test
    public void simpleTest() {
        Metadata metadata = metadataService.generateMetadata(v2Metadata);
        Assert.isTrue(metadata.getVersion().equals("v2"), "bad version");
    }

}
