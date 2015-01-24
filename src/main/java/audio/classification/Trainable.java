package audio.classification;

import java.util.Map;
import java.util.Set;

/**
 * Created by Dmitry on 24.01.2015.
 */
public interface Trainable<T> {

    void train(Map<Label, Set<T>> trainData);
}
