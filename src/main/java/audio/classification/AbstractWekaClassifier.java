package audio.classification;

import audio.features.util.ArffBuilder;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public abstract class AbstractWekaClassifier implements MachineLearningClassifier<double[]> {

    private static final String RELATION = "sound";

    private Instances instances;
    private Classifier classifier;
    private Map<Double, Label> labelMap;

    @Override
    public void train(Map<Label, Set<double[]>> trainData) {
        int attributesCount = trainData.values().iterator().next().iterator().next().length;
        String arff = ArffBuilder.createTrainingSet(RELATION, attributesCount, trainData);

        labelMap = new HashMap<>();
        Iterator<Label> iterator = trainData.keySet().iterator();
        for (int i = 0; i < trainData.keySet().size(); i++) {
            labelMap.put(Double.valueOf(i), iterator.next());
        }

        try {
            instances = new Instances(new StringReader(arff));
            instances.setClassIndex(instances.numAttributes() - 1);

            classifier = getClassifier();
            classifier.buildClassifier(instances);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Label predict(double[] input) {
        if (classifier  == null) {
            throw new IllegalStateException("Classifier was not built properly!");
        }

        try {
            Instance instance = new Instance(1.0, input);
            instance.setDataset(instances);
            double result = classifier.classifyInstance(instance);
            return labelMap.get(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public abstract Classifier getClassifier();
}
