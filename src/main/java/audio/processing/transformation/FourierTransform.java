package audio.processing.transformation;

import audio.processing.model.ComplexArray;

/**
 * Created by Dmitry on 7/16/2014.
 */
public interface FourierTransform extends Transformation<double[], ComplexArray>{

    ComplexArray transform(double[] realPart);
}
