package org.shlimtech.typesevendatabasecommon.metadata.versions;

import org.shlimtech.typesevendatabasecommon.metadata.Metadata;

public interface VersionedMetadataBuilder {

    Metadata createNewMetadata();
    String getVersion();
    String getParentVersion();
    Metadata upgradeFromParentVersion(Metadata metadata);

}
