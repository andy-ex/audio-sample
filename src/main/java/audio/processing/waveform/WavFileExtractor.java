package audio.processing.waveform;

import java.io.File;

public class WavFileExtractor implements WaveformExtractor {

    @Override
    public double[] extract(File in) {
        try {
            WavFile wavFile = WavFile.openWavFile(in);

            double[] result = new double[Integer.valueOf(String.valueOf(wavFile.getNumFrames()))];
            wavFile.readFrames(result, result.length);

            wavFile.close();

            return result;
        } catch (Exception e) {
            System.out.println("Error reading audio file: " + e.getMessage());
            return new double[0];
        }
    }
}
