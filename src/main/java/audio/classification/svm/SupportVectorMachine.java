package audio.classification.svm;

import audio.classification.Label;
import audio.classification.MachineLearningClassifier;
import edu.berkeley.compbio.jlibsvm.ImmutableSvmParameter;
import edu.berkeley.compbio.jlibsvm.ImmutableSvmParameterPoint;
import edu.berkeley.compbio.jlibsvm.binary.BinaryClassificationProblem;
import edu.berkeley.compbio.jlibsvm.binary.BinaryClassificationProblemImpl;
import edu.berkeley.compbio.jlibsvm.binary.BinaryModel;
import edu.berkeley.compbio.jlibsvm.binary.C_SVC;
import edu.berkeley.compbio.jlibsvm.kernel.*;
import edu.berkeley.compbio.jlibsvm.util.SparseVector;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SupportVectorMachine implements MachineLearningClassifier<double[]> {

    private BinaryModel<Label, SparseVector> binaryModel;

    @Override
    public void train(Map<Label, Set<double[]>> trainData) {
        KernelFunction<SparseVector> kernelFunction = new LinearKernel();
        //KernelFunction<SparseVector> kernelFunction = new GaussianRBFKernel(0.1f);

        Map<SparseVector, Label> trainMap = createTrainMap(trainData);
        Map<SparseVector, Integer> ids = createIds(trainMap);

        BinaryClassificationProblem<Label, SparseVector> problem = new BinaryClassificationProblemImpl<>(
                Label.class, trainMap, ids);

        ImmutableSvmParameter<Label, SparseVector> param = new ImmutableSvmParameterPoint<>(createBuilder(kernelFunction));

        C_SVC<Label, SparseVector> svm = new C_SVC<>();

        binaryModel = svm.train(problem, param);
    }

    @Override
    public Label predict(double[] input) {
        if (binaryModel == null) {
            throw new IllegalStateException("SupportVectorMachine should be trained first!");
        }
        return binaryModel.predictLabel(createSparseVector(input));
    }

    private ImmutableSvmParameterPoint.Builder<Label, SparseVector> createBuilder(KernelFunction<SparseVector> kernelFunction) {
        ImmutableSvmParameterPoint.Builder<Label, SparseVector> builder = new ImmutableSvmParameterPoint.Builder<>();
        builder.kernel = kernelFunction;
        builder.eps = 0.00001f;
        return builder;
    }

    private Map<SparseVector, Label> createTrainMap(Map<Label, Set<double[]>> trainData) {
        Map<SparseVector, Label> trainMap = new HashMap<>();
        for (Map.Entry<Label, Set<double[]>> entry : trainData.entrySet()) {
            for (double[] array : entry.getValue()) {
                trainMap.put(createSparseVector(array), entry.getKey());
            }
        }
        return trainMap;
    }

    private Map<SparseVector, Integer> createIds(Map<SparseVector, Label> trainMap) {
        Map<SparseVector, Integer> ids = new HashMap<>();
        int i = 0;
        for (SparseVector sparseVector : trainMap.keySet()) {
            ids.put(sparseVector, i++);
        }
        return ids;
    }

    private SparseVector createSparseVector(double[] vector) {
        SparseVector sparseVector = new SparseVector(vector.length);
        for (int i = 0; i < vector.length; i++) {
            sparseVector.indexes[i] = i;
            sparseVector.values[i] = (float) vector[i];
        }
        return sparseVector;
    }
}
