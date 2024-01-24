package org.shlimtech.typesevendatabasecommon.metadata;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MetadataEntry {

    private String name;
    private String message;
    private boolean flag;

    public MetadataEntry(String name, String message) {
        this.name = name;
        this.message = message;
        this.flag = false;
    }

    public MetadataEntry(String name, String message, boolean flag) {
        this.name = name;
        this.message = message;
        this.flag = flag;
    }

    public MetadataEntry(String name) {
        this.name = this.message = name;
        this.flag = false;
    }

    public MetadataEntry(String name, boolean flag) {
        this.name = this.message = name;
        this.flag = flag;
    }

}
