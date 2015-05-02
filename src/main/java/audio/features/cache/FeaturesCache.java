package audio.features.cache;

import audio.domain.Features;
import audio.domain.Genre;
import audio.domain.Sound;
import audio.domain.Sounds;
import audio.features.FeatureExtractor;
import audio.features.mfcc.MfccExtractor;
import audio.features.spectral.RMS;
import audio.features.spectral.SpectralCentroid;
import audio.features.spectral.ZCR;
import audio.processing.waveform.WavFileExtractor;
import util.ArraysHelper;
import util.FileSystem;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.net.URISyntaxException;
import java.util.*;

public class FeaturesCache {



    public static void main(String[] args) throws URISyntaxException, JAXBException {
        naturalSort();
    }

    public static Sounds loadSounds() {
        try {
            JAXBContext context = JAXBContext.newInstance(Sounds.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (Sounds) unmarshaller.unmarshal(new File("sounds.xml"));
        } catch (JAXBException e) {
            return null;
        }
    }

    private static void naturalSort() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Sounds.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Sounds sounds = (Sounds) unmarshaller.unmarshal(new File("sounds.xml"));

        NaturalOrderComparator comparator = new NaturalOrderComparator();
        for (Genre genre : sounds.getGenres()) {
            Collections.sort(genre.getSounds(), (o1, o2) -> comparator.compare(o1.getPath(), o2.getPath()));
        }

        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(sounds, new File("sounds.xml"));
    }

    private static void storeToXML() throws JAXBException, URISyntaxException {
        FeatureExtractor spectralCentroid = new SpectralCentroid();
        FeatureExtractor zcr = new ZCR();
        FeatureExtractor rms = new RMS();

        Sounds sounds = new Sounds();

        JAXBContext context = JAXBContext.newInstance(Sounds.class);

        File root = new File(FileSystem.getResourceURL("/genres/").toURI());
        File[] subFiles = root.listFiles();


        for (int g = 0; g < subFiles.length; ++g) {
            File file = subFiles[g];
            List<Sound> soundList = new ArrayList<>();

            File[] soundPaths = file.listFiles();
            for (int i = 0; i < soundPaths.length; ++i) {
                File soundFile = soundPaths[i];
                double[] waveform = new WavFileExtractor().extract(soundFile);

                Sound sound = new Sound();
                Features features = new Features();

                double[][] coefficients = new MfccExtractor().extractCoefficients(waveform, 22050);
                features.setMfcc(ArraysHelper.average(ArraysHelper.averageByColumn(coefficients)));

                double[][] sc = spectralCentroid.extract(waveform, 22050);
                features.setSpectralCentroid(ArraysHelper.average(sc[0]));

                double[][] zcrFeature = zcr.extract(waveform, 22055);
                features.setZcr(zcrFeature[0][0]);

                double[][] rmsFeature = rms.extract(waveform, 22055);
                features.setRms(rmsFeature[0][0]);

                sound.setPath(soundFile.getPath());
                sound.setFeatures(features);

                soundList.add(sound);
            }
            Genre genre1 = new Genre();
            genre1.setGenreId(file.getName());
            genre1.setSounds(soundList);

            sounds.getGenres().add(genre1);
        }


        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(sounds, new File("sounds.xml"));
    }

}
