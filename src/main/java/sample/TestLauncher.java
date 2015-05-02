package sample;

import audio.classification.Label;
import audio.classification.MachineLearningClassifier;
import audio.classification.model.Point;
import audio.classification.svm.SupportVectorMachine2D;
import audio.classification.svm.SupportVectorMachine;

import java.util.*;


public class TestLauncher {

    public static void main(String[] args) {

        pointsWorkingExample();
        System.out.println();
        arraysExample();
    }

    private static void arraysExample() {
        final Set<double[]> trueExamples = new HashSet<double[]>() {{
            add(createArray(5, 1));
            add(createArray(4, 2));
            add(createArray(4.5f, 1.5f));
            add(createArray(6, 2));
        }};

        final Set<double[]> falseExamples = new HashSet<double[]>() {{
            add(createArray(1, 5));
            add(createArray(2, 4));
            add(createArray(1.5f, 4.5f));
            add(createArray(2, 6));
            add(createArray(5.5f, 6));
            add(createArray(2, 2.3f));
        }};

        Map<Label, Set<double[]>> trainData = new HashMap<>();
        trainData.put(new Label(-10, "x > y"), trueExamples);
        trainData.put(new Label(10, "x < y"), falseExamples);

        MachineLearningClassifier classifier = new SupportVectorMachine();
        classifier.train(trainData);

        for (int i = 0; i < 5; i++) {
            double[] point = createArray(0.5f*i, i);
            System.out.println(Arrays.toString(point) + "; Result: " + classifier.predict(point));
        }
    }

    private static void pointsWorkingExample() {
        final Set<Point> trueExamples = new HashSet<Point>() {{
            add(createPoint(5,1));
            add(createPoint(4,2));
            add(createPoint(4.5f,1.5f));
            add(createPoint(6,2));
        }};

        final Set<Point> falseExamples = new HashSet<Point>() {{
            add(createPoint(1,5));
            add(createPoint(2,4));
            add(createPoint(1.5f,4.5f));
            add(createPoint(2,6));
        }};

        Map<Label, Set<Point>> trainData = new HashMap<>();
        trainData.put(new Label(-10, "x > y"), trueExamples);
        trainData.put(new Label(10, "x < y"), falseExamples);

        SupportVectorMachine2D classifier = new SupportVectorMachine2D();
        classifier.train(trainData);

        for (int i = 0; i < 5; i++) {
            Point point = createPoint(0.5f*i, i);
            System.out.println(point + "; Result: " + classifier.predict(point));
        }
    }

    private static Point createPoint(float x, float y) {
        return new Point(x, y);
    }

    private static double[] createArray(float x, float y) {
        return new double[] {x, y};
    }
}