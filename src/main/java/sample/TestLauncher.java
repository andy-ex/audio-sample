package sample;

import audio.classification.Label;
import audio.classification.model.Point;
import audio.classification.svm.SupportVectorMachine;
import edu.berkeley.compbio.jlibsvm.kernel.GaussianRBFKernel;
import edu.berkeley.compbio.jlibsvm.kernel.KernelFunction;
import edu.berkeley.compbio.jlibsvm.kernel.LinearKernel;
import edu.berkeley.compbio.jlibsvm.util.SparseVector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class TestLauncher {

    public static void main(String[] args) {

        KernelFunction<SparseVector> kernelFunction = new GaussianRBFKernel(0.5f);

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
        trainData.put(new Label(1, "x > y"), trueExamples);
        trainData.put(new Label(-1, "x < y"), falseExamples);

        SupportVectorMachine svm = new SupportVectorMachine();
        svm.train(trainData);

        for (int i = 0; i < 5; i++) {
            Point point = createPoint(i, 5 - i);
            System.out.println(point + "; Result: " + svm.predict(point));
        }
    }

    private static Point createPoint(float x, float y) {
        return new Point(x, y);
    }
}