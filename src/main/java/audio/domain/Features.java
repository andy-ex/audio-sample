package audio.domain;

import java.lang.reflect.Field;

public class Features {

    private double mfcc;
    private double zcr;
    private double rms;
    private double spectralCentroid;

    public double getFeatureValue(String name) {
        try {
            Field field = this.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.getDouble(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return -1;
        }
    }

    public double getMfcc() {
        return mfcc;
    }

    public void setMfcc(double mfcc) {
        this.mfcc = mfcc;
    }

    public double getZcr() {
        return zcr;
    }

    public void setZcr(double zcr) {
        this.zcr = zcr;
    }

    public double getRms() {
        return rms;
    }

    public void setRms(double rms) {
        this.rms = rms;
    }

    public double getSpectralCentroid() {
        return spectralCentroid;
    }

    public void setSpectralCentroid(double spectralCentroid) {
        this.spectralCentroid = spectralCentroid;
    }
}
