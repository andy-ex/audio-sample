package audio.classification.knn;

import audio.classification.Label;
import audio.classification.MachineLearningClassifier;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import util.ArraysHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class KNearestNeighbors implements MachineLearningClassifier<double[]> {

    private Map<Label, Set<double[]>> trainData;
    private static final int k = 5;

    @Override
    public void train(Map<Label, Set<double[]>> trainData) {
        this.trainData = trainData;
    }

    @Override
    public Label predict(double[] input) {
        PriorityQueue<Pair<Double, Label>> neighbors = new PriorityQueue<>((o1, o2) -> o1.getLeft() - o2.getLeft() < 0 ? 1 : -1);
        for (Map.Entry<Label, Set<double[]>> entry : trainData.entrySet()) {
            for (double[] neighbor : entry.getValue()) {
                neighbors.add(new ImmutablePair<>(ArraysHelper.euclideanDistance(input, neighbor), entry.getKey()));
                if (neighbors.size() > k) {
                    neighbors.poll();
                }
            }
        }

        Map<Label, Integer> nearestNeighbors = new HashMap<>();
        for (Pair<Double, Label> neighbor : neighbors) {
            Label value = neighbor.getValue();
            nearestNeighbors.put(value, nearestNeighbors.get(value) == null ? 1 : nearestNeighbors.get(value) + 1);
        }

        int max = -1;
        Label maxLabel = null;
        for (Map.Entry<Label, Integer> entry : nearestNeighbors.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                maxLabel = entry.getKey();
            }
        }

        return maxLabel;
    }

}
