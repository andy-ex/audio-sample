package sample;

import audio.classification.Label;
import audio.classification.MachineLearningClassifier;
import audio.classification.knn.KNearestNeighbors;
import audio.classification.svm.MultiClassSupportVectorMachine;
import audio.classification.svm.SupportVectorMachine;
import audio.classification.tree.RandomForest;
import audio.domain.*;
import audio.features.util.FeaturesCache;
import org.apache.commons.lang.ArrayUtils;
import util.ArraysHelper;

import java.net.URISyntaxException;
import java.util.*;

public class CrossValidation {

    private static final int SAMPLES_COUNT = 100;
    private static final int K = 10;
    private static final int SAMPLES_TO_VALIDATE = 10;
    private static List<String> genres = Arrays.asList("metal", "classical", "jazz", "pop");
    private static FeatureName[] includeFeatures = new FeatureName[] {FeatureName.MFCCS};
    private static MachineLearningClassifier classifier  = new KNearestNeighbors();

    private static Map<String, MachineLearningClassifier> classifierMap = new HashMap<String, MachineLearningClassifier>() {{
        put("knn", new KNearestNeighbors());
        put("rf", new RandomForest());
        put("svm", new SupportVectorMachine());
        put("msvm", new MultiClassSupportVectorMachine());
    }};

    public static void main(String[] args) throws URISyntaxException {
        if (args != null && args.length  > 0) {
            for (String arg : args) {
                processArg(arg);
            }
        }

        System.out.println("Classifier: " + classifier.getClass().getSimpleName());
        System.out.println("Genres: " + genres);
        System.out.println("Features: " + Arrays.toString(includeFeatures));

        long l = System.currentTimeMillis();
        test();
        System.out.println(System.currentTimeMillis() - l);
    }

    private static void processArg(String arg) {
        String[] split = arg.split("=");
        if ("genres".equals(split[0])) {
            String[] genresStrings = split[1].split(",");
            if (genres.size() < 2) return;
            genres = Arrays.asList(genresStrings);
        } else if ("classifier".equals(split[0])) {
            classifier = classifierMap.get(split[1]) == null ? new KNearestNeighbors() : classifierMap.get(split[1]);
        } else if ("features".equals(split[0])) {
            String[] features = split[1].split(",");
            includeFeatures = new FeatureName[0];
            for (String feature : features) {
                includeFeatures = (FeatureName[]) ArrayUtils.add(includeFeatures, FeatureName.fromName(feature));
            }
        }
    }

    public static double test() throws URISyntaxException {
        Sounds sounds = FeaturesCache.loadSounds();
        double sum = 0.0;
        for (int k = 0; k < K; ++k) {
            int genreId = 0;
            int start = k * SAMPLES_TO_VALIDATE;
            int end = start + SAMPLES_TO_VALIDATE;
            Map<Label, Set<double[]>> trainData = new HashMap<>();

            for (Genre genre : sounds.getGenres()) {
                if (!genres.contains(genre.getGenreId())) continue;

                List<Sound> genreSounds = genre.getSounds();
                Set<double[]> featureData = new HashSet<>();
                for (int i = 0; i < SAMPLES_COUNT; i++) {
                    if (i >= start && i < end) continue;
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
                for (int i = start; i < end; i++) {
                    Sound sound = genreSounds.get(i);
                    double[] featureValues = getFeatureValues(sound.getFeatures());
                    Label predicted = classifier.predict(featureValues);
                    //System.out.println(sound.getPath().substring(sound.getPath().lastIndexOf("\\")) + " : " + predicted.getName());

                    if (predicted.getName().equals(genre.getGenreId())) correct++;
                }
            }

            double currentPercent = (double) 100 * correct / (SAMPLES_TO_VALIDATE * genres.size());
            sum += currentPercent;
            System.out.println(currentPercent);
        }
        System.out.println();
        double result = sum / K;
        System.out.println("Overall: " + result + "%");

        return result;
    }

    private static double[] getFeatureValues(Features features) {
        double[] result = new double[] {};
        for (int i = 0; i < includeFeatures.length; i++) {
            String feature = includeFeatures[i].getName();

            if (FeatureName.MFCC_RANGE == includeFeatures[i]) {
                result = ArrayUtils.add(result, ArraysHelper.average(features.getMfccs(), 0, 1));
                result = ArrayUtils.add(result, ArraysHelper.average(features.getMfccs(), 2, 6));
            } else if (FeatureName.MFCCS == includeFeatures[i]) {
                result = ArrayUtils.addAll(result, features.getMfccs());
            } else {
                result = ArrayUtils.add(result, features.getFeatureValue(feature));
            }
        }

        return result;
    }

}
