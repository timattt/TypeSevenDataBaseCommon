package org.shlimtech.typesevendatabasecommon.metadata.versions;

import lombok.extern.java.Log;
import org.shlimtech.typesevendatabasecommon.metadata.Metadata;
import org.shlimtech.typesevendatabasecommon.metadata.MetadataEntry;
import org.shlimtech.typesevendatabasecommon.metadata.MetadataEntrySet;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Log
public class V1Metadata implements VersionedMetadataBuilder {

    private static final String GENDER = "Пол";
    private static final String TARGET = "Кого ищем";
    private static final String MAN = "Парень";
    private static final String WOMAN = "Девушка";

    @Override
    public Metadata createNewMetadata() {
        Metadata metadata = new Metadata();
        metadata.setVersion(getVersion());

        MetadataEntrySet gender = new MetadataEntrySet();
        gender.setName(GENDER);
        gender.setMinimumChoices(1);
        gender.setMaximumChoices(1);
        gender.setMessage("Кто вы?");
        gender.setEntries(List.of(new MetadataEntry(MAN, true), new MetadataEntry(WOMAN)));

        MetadataEntrySet request = new MetadataEntrySet();
        request.setName(TARGET);
        request.setMessage("Кого ищем?");
        request.setMinimumChoices(1);
        request.setMaximumChoices(1);
        request.setEntries(List.of(new MetadataEntry(MAN, "Парней"), new MetadataEntry(WOMAN, "Девушек", true)));

        MetadataEntrySet hobbies = new MetadataEntrySet();
        hobbies.setName("Увлечения");
        hobbies.setEntries(List.of(new MetadataEntry("Бег"), new MetadataEntry("Покер")));

        MetadataEntrySet music = new MetadataEntrySet();
        music.setName("Любимая музыка");
        music.setEntries(List.of(new MetadataEntry("Классика"), new MetadataEntry("Попса")));

        metadata.setMetadataEntrySets(List.of(gender, request, hobbies, music));
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

    @Override
    public int metric(Metadata a, Metadata b) {
        List<MetadataEntrySet> set1 = filterMetadataEntriesFromServiceData(a);
        List<MetadataEntrySet> set2 = filterMetadataEntriesFromServiceData(b);

        if (set1.size() != set2.size()) {
            throw new RuntimeException("bad entry set lists sizes");
        }

        int result = 0;

        for (int i = 0; i < set1.size(); i++) {
            MetadataEntrySet entrySet1 = set1.get(i);
            MetadataEntrySet entrySet2 = set2.get(i);

            if (!entrySet1.getName().equals(entrySet2.getName())) {
                throw new RuntimeException("bad entries sets names");
            }

            List<MetadataEntry> entries1 = entrySet1.getEntries();
            List<MetadataEntry> entries2 = entrySet2.getEntries();

            if (entries1.size() != entries2.size()) {
                throw new RuntimeException("entries sizes must be equals");
            }

            for (int j = 0; j < entries1.size(); j++) {
                MetadataEntry entry1 = entries1.get(j);
                MetadataEntry entry2 = entries2.get(j);

                if (!entry1.getName().equals(entry2.getName())) {
                    throw new RuntimeException("entries names must be equals");
                }

                if (entry1.isFlag() && entry2.isFlag()) {
                    result++;
                }
                // TODO maybe if they have different values of flag we can decrease metric value by one
            }
        }

        return result;
    }

    @Override
    public boolean canMatch(Metadata a, Metadata b) {
        String aTarget = getTarget(a);
        String bTarget = getTarget(b);

        String aGender = getGender(a);
        String bGender = getGender(b);

        return aTarget.equals(bGender) && bTarget.equals(aGender);
    }

    @Override
    public boolean validateMetadata(Metadata a) {
        try {
            String target = getTarget(a);
            String gender = getGender(a);
            if (!target.equals(MAN) && !target.equals(WOMAN)) {
                return false;
            }
            if (!gender.equals(MAN) && !gender.equals(WOMAN)) {
                return false;
            }
            for (var entrySet : a.getMetadataEntrySets()) {
                int count = 0;
                for (var entry : entrySet.getEntries()) {
                    if (entry.isFlag()) {
                        count++;
                    }
                }

                if (entrySet.getMinimumChoices() > count || entrySet.getMaximumChoices() < count) {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private List<MetadataEntrySet> filterMetadataEntriesFromServiceData(Metadata metadata) {
        return metadata.getMetadataEntrySets()
                .stream()
                .filter(entry -> !entry.getName().equals(GENDER) && !entry.getName().equals(TARGET)).collect(Collectors.toList());
    }

    private String getGender(Metadata metadata) {
        String gender = metadata
                .getMetadataEntrySets()
                .stream()
                .filter(met -> met.getName().equals(GENDER))
                .map(MetadataEntrySet::getEntries)
                .findAny()
                .get()
                .stream()
                .filter(MetadataEntry::isFlag)
                .findAny()
                .get()
                .getName();
        return gender;
    }

    private String getTarget(Metadata metadata) {
        String target = metadata
                .getMetadataEntrySets()
                .stream()
                .filter(met -> met.getName().equals(TARGET))
                .map(MetadataEntrySet::getEntries)
                .findAny()
                .get()
                .stream()
                .filter(MetadataEntry::isFlag)
                .findAny()
                .get()
                .getName();
        return target;
    }

}
