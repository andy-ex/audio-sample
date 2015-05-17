package audio.classification.svm;

import audio.classification.Label;
import audio.classification.MachineLearningClassifier;
import edu.berkeley.compbio.jlibsvm.ImmutableSvmParameter;
import edu.berkeley.compbio.jlibsvm.ImmutableSvmParameterPoint;
import edu.berkeley.compbio.jlibsvm.binary.C_SVC;
import edu.berkeley.compbio.jlibsvm.kernel.KernelFunction;
import edu.berkeley.compbio.jlibsvm.kernel.LinearKernel;
import edu.berkeley.compbio.jlibsvm.kernel.PolynomialKernel;
import edu.berkeley.compbio.jlibsvm.kernel.SigmoidKernel;
import edu.berkeley.compbio.jlibsvm.multi.MultiClassModel;
import edu.berkeley.compbio.jlibsvm.multi.MultiClassProblemImpl;
import edu.berkeley.compbio.jlibsvm.multi.MultiClassificationSVM;
import edu.berkeley.compbio.jlibsvm.scaler.NoopScalingModel;
import edu.berkeley.compbio.jlibsvm.util.SparseVector;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MultiClassSupportVectorMachine implements MachineLearningClassifier<double[]> {

    private MultiClassModel<Label, SparseVector> multiClassModel;

    @Override
    public void train(Map<Label, Set<double[]>> trainData) {
        KernelFunction<SparseVector> kernelFunction = new LinearKernel();
        //KernelFunction<SparseVector> kernelFunction = new PolynomialKernel(4, 1, 1);

        Map<SparseVector, Label> trainMap = createTrainMap(trainData);
        Map<SparseVector, Integer> ids = createIds(trainMap);

        MultiClassProblemImpl<Label, SparseVector> problem = new MultiClassProblemImpl<>(
                Label.class, new LabelInverter(), trainMap, ids, new NoopScalingModel<>());

        ImmutableSvmParameter<Label, SparseVector> param = new ImmutableSvmParameterPoint<>(createBuilder(kernelFunction));

        MultiClassificationSVM<Label, SparseVector> svm = new MultiClassificationSVM<>(new C_SVC<>());

        this.multiClassModel = svm.train(problem, param);
    }

    @Override
    public Label predict(double[] input) {
        if (multiClassModel == null) {
            throw new IllegalStateException("SupportVectorMachine should be trained first!");
        }
        return multiClassModel.predictLabel(createSparseVector(input));
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
        for (Map.Entry<SparseVector, Label> entry : trainMap.entrySet()) {
            ids.put(entry.getKey(), entry.getValue().getId());
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
