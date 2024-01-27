package org.shlimtech.typesevendatabasecommon.metadata.versions;

import org.junit.jupiter.api.Test;
import org.shlimtech.typesevendatabasecommon.BaseTest;
import org.shlimtech.typesevendatabasecommon.metadata.Metadata;
import org.shlimtech.typesevendatabasecommon.service.MetadataService;
import org.shlimtech.typesixdatabasecommon.dto.UserDTO;
import org.shlimtech.typesixdatabasecommon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

public class V3MetadataTests extends BaseTest {

    @Autowired
    private MetadataService metadataService;

    @Autowired
    private UserService userService;

    @Autowired
    private V1Metadata v1Metadata;

    @Autowired
    private V2Metadata v2Metadata;

    @Autowired
    private V3Metadata v3Metadata;

    private int insertTestUser() {
        userService.createOrComplementUser(UserDTO.builder().email("ggg@gmail.com").firstName("hhh").build());
        Assert.isTrue(userService.loadUser("ggg@gmail.com") != null, "must contains user");
        int id = userService.loadUser("ggg@gmail.com").getId();
        return id;
    }

    @Test
    public void simpleUpgradeFromV1Test() {
        int userId = insertTestUser();
        metadataService.saveUserMetadata(userId, metadataService.generateMetadata(v1Metadata));
        Metadata migrated = metadataService.loadUserMetadata(userId);
        Assert.isTrue(migrated.getVersion().equals(v3Metadata.getVersion()), "not migrated");
    }

    @Test
    public void simpleUpgradeFromV2Test() {
        int userId = insertTestUser();
        metadataService.saveUserMetadata(userId, metadataService.generateMetadata(v2Metadata));
        Metadata migrated = metadataService.loadUserMetadata(userId);
        Assert.isTrue(migrated.getVersion().equals(v3Metadata.getVersion()), "not migrated");
    }

}
