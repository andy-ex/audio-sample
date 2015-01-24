package audio.classification;

/**
 * Created by Dmitry on 24.01.2015.
 */
public interface Predictable<T> {

    Label predict(T input);

}
