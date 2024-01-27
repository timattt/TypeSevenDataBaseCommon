package org.shlimtech.typesevendatabasecommon.metadata.versions;

import lombok.RequiredArgsConstructor;
import org.shlimtech.typesevendatabasecommon.metadata.Metadata;
import org.shlimtech.typesevendatabasecommon.service.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class V3Metadata implements VersionedMetadataBuilder {

    private MetadataService metadataService;
    private final V2Metadata parent;

    @Autowired
    @Lazy
    public void setMetadataService(MetadataService metadataService) {
        this.metadataService = metadataService;
    }

    @Override
    public String getMetadataTemplatePath() {
        return "classpath:metadataVersions/v3.yaml";
    }

    @Override
    public String getVersion() {
        return "v3";
    }

    @Override
    public String getParentVersion() {
        return "v2";
    }

    @Override
    public Metadata upgradeFromParentVersion(Metadata metadata) {
        return metadataService.generateMetadata(this); // just rewrite
    }

    @Override
    public int metric(Metadata a, Metadata b) {
        return parent.metric(a, b);
    }

    @Override
    public boolean canMatch(Metadata a, Metadata b) {
        return parent.canMatch(a, b);
    }

    @Override
    public boolean validateMetadata(Metadata a) {
        return parent.validateMetadata(a);
    }
}
