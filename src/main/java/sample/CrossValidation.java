package sample;

import audio.classification.Label;
import audio.classification.MachineLearningClassifier;
import audio.classification.knn.KNearestNeighbors;
import audio.classification.svm.MultiClassSupportVectorMachine;
import audio.classification.svm.SupportVectorMachine;
import audio.domain.*;
import audio.features.cache.FeaturesCache;

import java.net.URISyntaxException;
import java.util.*;

public class CrossValidation {

    private static final int SAMPLES_TO_TRAIN = 89;
    private static final int SAMPLES_TO_VALIDATE = 10;
    private static final String feature = FeatureName.MFCC.getName();
    private static List<String> genres = Arrays.asList("rock", "classical", "jazz", "hiphop");;

    public static void main(String[] args) throws URISyntaxException {
        test();
    }

    private static void saveSoundArff() {
        Sounds sounds = FeaturesCache.loadSounds();

        System.out.println("@DATA");
        for (Genre genre : sounds.getGenres()) {
            if (!genres.contains(genre.getGenreId())) continue;
            for (Sound sound : genre.getSounds()) {
                System.out.println(sound.getFeatures().getFeatureValue(feature) + "," + genre.getGenreId());
            }
        }
    }

    public static void test() throws URISyntaxException {
        Map<Label, Set<double[]>> trainData = new HashMap<>();
        Sounds sounds = FeaturesCache.loadSounds();
        int genreId = 0;

        for (Genre genre : sounds.getGenres()) {
            if (!genres.contains(genre.getGenreId())) continue;

            List<Sound> genreSounds = genre.getSounds();
            Set<double[]> featureData = new HashSet<>();
            for (int i = 0; i < SAMPLES_TO_TRAIN; i++) {
                Features features = genreSounds.get(i).getFeatures();
                featureData.add(new double[]{features.getFeatureValue(feature)});
            }
            trainData.put(new Label(genreId++, genre.getGenreId()), featureData);
        }

        MachineLearningClassifier classifier = new MultiClassSupportVectorMachine();
        classifier.train(trainData);

        int correct = 0;
        for (Genre genre : sounds.getGenres()) {
            if (!genres.contains(genre.getGenreId())) continue;

            List<Sound> genreSounds = genre.getSounds();
            for (int i = SAMPLES_TO_TRAIN; i < SAMPLES_TO_TRAIN + SAMPLES_TO_VALIDATE; i++) {
                Sound sound = genreSounds.get(i);
                Label predicted = classifier.predict(new double[]{sound.getFeatures().getFeatureValue(feature)});
                System.out.println(sound.getPath().substring(sound.getPath().lastIndexOf("\\")) + " : " + predicted.getName());

                if (predicted.getName().equals(genre.getGenreId())) correct++;
            }
        }

        System.out.println("Overall: " + (double) 100 * correct / (SAMPLES_TO_VALIDATE * genres.size()) + "%");
    }

}
