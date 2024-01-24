package org.shlimtech.typesevendatabasecommon.metadata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetadataEntrySet {

    private String name;
    private String message;
    private int minimumChoices = 0;
    private int maximumChoices = Integer.MAX_VALUE;
    private List<MetadataEntry> entries;

}
