package audio.processing;

import javax.sound.sampled.AudioInputStream;

/**
 * Created by Dmitry on 7/6/2014.
 */
public interface WaveformExtractor {

    int[] extract(AudioInputStream in);
}
