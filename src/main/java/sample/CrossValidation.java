package sample;

import audio.classification.Label;
import audio.classification.MachineLearningClassifier;
import audio.classification.knn.KNearestNeighbors;
import audio.classification.svm.MultiClassSupportVectorMachine;
import audio.classification.svm.SupportVectorMachine;
import audio.classification.tree.RandomForest;
import audio.domain.*;
import audio.features.util.FeaturesCache;

import java.net.URISyntaxException;
import java.util.*;

public class CrossValidation {

    private static final int SAMPLES_TO_TRAIN = 89;
    private static final int SAMPLES_TO_VALIDATE = 10;
    private static final FeatureName[] includeFeatures = new FeatureName[] {FeatureName.MFCC_MEAN};
    //private static List<String> genres = Arrays.asList("rock", "classical", "pop", "jazz");
    private static List<String> genres = Arrays.asList("rock", "classical");
    private static MachineLearningClassifier classifier  = new KNearestNeighbors();

    public static void main(String[] args) throws URISyntaxException {
        test();
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
                double[] featureValues = getFeatureValues(features);
                featureData.add(featureValues);
            }
            trainData.put(new Label(genreId++, genre.getGenreId()), featureData);
        }

        classifier.train(trainData);

        int correct = 0;
        for (Genre genre : sounds.getGenres()) {
            if (!genres.contains(genre.getGenreId())) continue;

            List<Sound> genreSounds = genre.getSounds();
            for (int i = SAMPLES_TO_TRAIN; i < SAMPLES_TO_TRAIN + SAMPLES_TO_VALIDATE; i++) {
                Sound sound = genreSounds.get(i);
                double[] featureValues = getFeatureValues(sound.getFeatures());
                Label predicted = classifier.predict(featureValues);
                System.out.println(sound.getPath().substring(sound.getPath().lastIndexOf("\\")) + " : " + predicted.getName());

                if (predicted.getName().equals(genre.getGenreId())) correct++;
            }
        }

        System.out.println("Overall: " + (double) 100 * correct / (SAMPLES_TO_VALIDATE * genres.size()) + "%");
    }

    private static double[] getFeatureValues(Features features) {
        double[] result = new double[includeFeatures.length];
        for (int i = 0; i < includeFeatures.length; i++) {
            result[i] = features.getFeatureValue(includeFeatures[i].getName());
        }

        return result;
    }

}
