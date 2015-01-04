package audio.processing.mfcc;

/**
 * Created by Dmitry on 05.10.2014.
 */
public class MelFrequencyConverter {

    public static double melToFrequency(double melValue) {
        return 700.0 * (Math.exp(melValue / 1125.0) - 1);
    }

    public static double frequencyToMel(double frequencyValue) {
        return 1125.0 * Math.log(1.0 + frequencyValue / 700.0);
    }
}
