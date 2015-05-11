package audio.features.util;

import audio.classification.Label;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ArffBuilder {

    public static final String RELATION = "@RELATION";
    public static final String EMPTY_LINE = "\r\n\r\n";
    public static final String ATTRIBUTE = "@ATTRIBUTE";
    public static final String NUMERIC = "NUMERIC";
    public static final String CLASS = "class";
    public static final String COMMA = ",";
    public static final String NEW_LINE = "\r\n";
    public static final String DATA = "@DATA";

    public static String createTrainingSet(String relation, int attributesCount, Map<Label, Set<double[]>> data) {
        StringBuilder builder = new StringBuilder();

        builder.append(RELATION).append(" ").append(relation);
        builder.append(EMPTY_LINE);

        for (int i = 0; i < attributesCount; i++) {
            builder.append(ATTRIBUTE).append(" ").append(i).append(" ").append(NUMERIC).append(NEW_LINE);
        }

        builder.append(ATTRIBUTE).append(" " + CLASS + " ").append("{" + StringUtils.join(labelsToString(data), COMMA) + "}");

        builder.append(EMPTY_LINE);

        builder.append(DATA).append(NEW_LINE);
        for (Map.Entry<Label, Set<double[]>> entry : data.entrySet()) {
            Label label = entry.getKey();
            Set<double[]> value = entry.getValue();
            for (double[] array : value) {
                for (double v : array) {
                    builder.append(v).append(COMMA);
                }
                builder.append(label.getName()).append(NEW_LINE);
            }
        }

        return builder.toString();
    }

    public static String createInstance(double[] values) {
        return null;
    }

    private static List<String> labelsToString(Map<Label, Set<double[]>> data) {
        return data.keySet().stream().map(Label::getName).collect(Collectors.toList());
    }
}
