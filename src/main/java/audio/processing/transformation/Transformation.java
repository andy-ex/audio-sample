package audio.processing.transformation;

public interface Transformation<I, O> {

    O transform(I input);

}
