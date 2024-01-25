package org.shlimtech.typesevendatabasecommon.metadata.service;

import org.junit.jupiter.api.Test;
import org.shlimtech.typesevendatabasecommon.BaseTest;
import org.shlimtech.typesevendatabasecommon.service.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;

public class MetadataServiceTests extends BaseTest {

    @Autowired
    private MetadataService metadataService;

    @Test
    public void simpleValidationTest() {
        metadataService.isValid(metadataService.generateMetadata());
    }

}
