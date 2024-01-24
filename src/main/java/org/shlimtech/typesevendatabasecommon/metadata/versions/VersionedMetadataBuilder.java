package org.shlimtech.typesevendatabasecommon.metadata.versions;

import org.shlimtech.typesevendatabasecommon.metadata.Metadata;

public interface VersionedMetadataBuilder {

    Metadata createNewMetadata();
    String getVersion();
    String getParentVersion();
    Metadata upgradeFromParentVersion(Metadata metadata);
    int metric(Metadata a, Metadata b);
    boolean canMatch(Metadata a, Metadata b);
    boolean validateMetadata(Metadata a);

}
