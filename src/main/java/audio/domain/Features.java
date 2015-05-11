package audio.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.lang.reflect.Field;

@XmlAccessorType(XmlAccessType.FIELD)
public class Features {

    @XmlElementWrapper(name = "mfccs")
    @XmlElement(name = "mfcc")
    private double[] mfccs;

    private double mfccMean;
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

    public double[] getMfccs() {
        return mfccs;
    }

    public void setMfccs(double[] mfccs) {
        this.mfccs = mfccs;
    }

    public double getMfccMean() {
        return mfccMean;
    }

    public void setMfccMean(double mfccMean) {
        this.mfccMean = mfccMean;
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
