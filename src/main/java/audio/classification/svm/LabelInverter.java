package audio.classification.svm;

import audio.classification.Label;

public class LabelInverter implements edu.berkeley.compbio.jlibsvm.labelinverter.LabelInverter<Label> {
    @Override
    public Label invert(Label label) {
        return new Label(-label.getId(), "Not " + label.getName());
    }
}
