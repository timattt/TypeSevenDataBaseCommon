package org.shlimtech.typesevendatabasecommon.metadata.versions;

import org.shlimtech.typesevendatabasecommon.metadata.Metadata;
import org.shlimtech.typesevendatabasecommon.metadata.MetadataEntry;
import org.shlimtech.typesevendatabasecommon.metadata.MetadataEntrySet;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class V1Metadata implements VersionedMetadataBuilder {

    @Override
    public Metadata createNewMetadata() {
        Metadata metadata = new Metadata();
        metadata.setVersion(getVersion());

        MetadataEntrySet hobbies = new MetadataEntrySet();
        hobbies.setName("Увлечения");
        hobbies.setMultiChoice(true);
        hobbies.setEntries(List.of(new MetadataEntry("Бег"), new MetadataEntry("Покер")));

        MetadataEntrySet music = new MetadataEntrySet();
        music.setName("Любимая музыка");
        music.setMultiChoice(true);
        music.setEntries(List.of(new MetadataEntry("Классика"), new MetadataEntry("Попса")));

        metadata.setMetadataEntrySets(List.of(hobbies, music));
        metadata.setSelectedUsers(List.of());

        return metadata;
    }

    @Override
    public String getVersion() {
        return "v1";
    }

    @Override
    public String getParentVersion() {
        return null;
    }

    @Override
    public Metadata upgradeFromParentVersion(Metadata metadata) {
        return metadata;
    }
}
