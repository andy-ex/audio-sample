package audio.classification.tree;

import audio.classification.AbstractWekaClassifier;
import weka.classifiers.Classifier;

public class RandomForest extends AbstractWekaClassifier {
    @Override
    public Classifier getClassifier() {
        return new weka.classifiers.trees.RandomForest();
    }
}
