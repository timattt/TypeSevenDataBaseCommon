package org.shlimtech.typesevendatabasecommon.metadata.versions;

import org.junit.jupiter.api.Test;
import org.shlimtech.typesevendatabasecommon.BaseTest;
import org.shlimtech.typesevendatabasecommon.metadata.Metadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

public class V1MetadataTests extends BaseTest {

    @Autowired
    private V1Metadata v1Metadata;

    @Test
    public void simpleMetricsTest() {
        Metadata a = v1Metadata.createNewMetadata();
        Metadata b = v1Metadata.createNewMetadata();

        int metric = v1Metadata.metric(a, b);

        Assert.isTrue(metric == 0, "metric must be zero for plain metadata");
    }

    @Test
    public void somethingChangedMetricsTest() {
        Metadata a = v1Metadata.createNewMetadata();
        Metadata b = v1Metadata.createNewMetadata();

        a.getMetadataEntrySets().get(2).getEntries().get(0).setFlag(true);
        b.getMetadataEntrySets().get(2).getEntries().get(0).setFlag(true);

        int metric = v1Metadata.metric(a, b);

        Assert.isTrue(metric == 1, "bad metrics");
    }

    @Test
    public void ignoreGenderAndTargetMetricsTest() {
        Metadata a = v1Metadata.createNewMetadata();
        Metadata b = v1Metadata.createNewMetadata();

        // gender
        a.getMetadataEntrySets().get(0).getEntries().get(0).setFlag(true);
        b.getMetadataEntrySets().get(0).getEntries().get(0).setFlag(true);

        // target
        a.getMetadataEntrySets().get(1).getEntries().get(0).setFlag(true);
        b.getMetadataEntrySets().get(1).getEntries().get(0).setFlag(true);

        // other
        a.getMetadataEntrySets().get(2).getEntries().get(0).setFlag(true);
        b.getMetadataEntrySets().get(2).getEntries().get(0).setFlag(true);

        int metric = v1Metadata.metric(a, b);

        Assert.isTrue(metric == 1, "bad metrics");
    }

    @Test
    public void validationTest() {
        Metadata a = v1Metadata.createNewMetadata();
        Assert.isTrue(v1Metadata.validateMetadata(a), "not validated correctly");

        a.getMetadataEntrySets().get(0).getEntries().get(0).setFlag(false);
        Assert.isTrue(!v1Metadata.validateMetadata(a), "not validated correctly");
    }

    @Test
    public void countValidationTest() {
        Metadata a = v1Metadata.createNewMetadata();
        a.getMetadataEntrySets().get(0).setMaximumChoices(1);
        a.getMetadataEntrySets().get(0).getEntries().get(0).setFlag(true);
        Assert.isTrue(v1Metadata.validateMetadata(a), "not validated correctly");
        a.getMetadataEntrySets().get(0).getEntries().get(1).setFlag(true);
        Assert.isTrue(!v1Metadata.validateMetadata(a), "not validated correctly");
    }

}
