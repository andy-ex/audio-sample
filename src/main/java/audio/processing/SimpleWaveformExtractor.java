package audio.processing;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Dmitry on 7/6/2014.
 */
public class SimpleWaveformExtractor implements WaveformExtractor {

    private static final int DEFAULT_BUFFER_SIZE = 32768;

    @Override
    public int[] extract(AudioInputStream in) {
        AudioFormat format = in.getFormat();
        byte[] audioBytes = readBytes(in);

        int[] result = null;
        if (format.getSampleSizeInBits() == 16) {
            int samplesLength = audioBytes.length / 2;
            result = new int[samplesLength];
            if (format.isBigEndian()) {
                for (int i = 0; i < samplesLength; ++i) {
                    byte MSB = audioBytes[i * 2];
                    byte LSB = audioBytes[i * 2 + 1];
                    result[i] = MSB << 8 | (255 & LSB);
                }
            } else {
                for (int i = 0; i < samplesLength; ++i) {
                    byte LSB = audioBytes[i * 2];
                    byte MSB = audioBytes[i * 2 + 1];
                    result[i] = MSB << 8 | (255 & LSB);
                }
            }
        } else {
            int samplesLength = audioBytes.length;
            result = new int[samplesLength];
            if (format.getEncoding().toString().startsWith("PCM_SIGN")) {
                for (int i = 0; i < samplesLength; ++i) {
                    result[i] = audioBytes[i];
                }
            } else {
                for (int i = 0; i < samplesLength; ++i) {
                    result[i] = audioBytes[i] - 128;
                }
            }
        }
        return result;
    }

    private byte[] readBytes(AudioInputStream in) {
        List<Byte> result = new ArrayList<Byte>();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

        try {
            int bytesRead = 0;
            do {
                bytesRead = in.read(buffer);
                for (int i = 0; i < bytesRead; ++i) {
                    result.add(buffer[i]);
                }
            } while (bytesRead != -1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] temp = new byte[result.size()];
        for (int i = 0; i < result.size(); ++i) {
            temp[i] = result.get(i);
        }
        return temp;
    }
}
