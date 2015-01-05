package audio.processing.waveform;

import java.io.File;

/**
 * Created by Dmitry on 7/6/2014.
 */
public interface WaveformExtractor {

    double[] extract(File in);
}
