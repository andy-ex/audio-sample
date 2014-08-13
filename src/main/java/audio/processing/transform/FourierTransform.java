package audio.processing.transform;

import audio.processing.model.ComplexArray;

/**
 * Created by Dmitry on 7/16/2014.
 */
public interface FourierTransform {

    ComplexArray transform(double[] realPart);

}
