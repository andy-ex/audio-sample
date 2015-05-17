package audio.domain;

public enum FeatureName {
    MFCC_MEAN("mfccMean"), MFCC_RANGE("mfccRange"), ZCR("zcr"), RMS("rms"), SPECTRAL_CENTROID("spectralCentroid"), MFCCS("mfccs");

    private String name;

    private FeatureName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static FeatureName fromName(String name) {
        for (FeatureName featureName : FeatureName.values()) {
            if (featureName.getName().equals(name)) {
                return featureName;
            }
        }

        return null;
    }

}
