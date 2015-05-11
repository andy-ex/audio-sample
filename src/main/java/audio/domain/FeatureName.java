package audio.domain;

public enum FeatureName {
    MFCC_MEAN("mfccMean"), ZCR("zcr"), RMS("rms"), SPECTRAL_CENTROID("spectralCentroid");

    private String name;

    private FeatureName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
