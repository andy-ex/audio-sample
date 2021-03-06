package sample;

import javafx.application.Application;
import javafx.stage.Stage;
import util.FxmlUtil;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

/**
 * Created by Dmitry on 6/27/2014.
 */
public class Launcher extends Application {

    public static void main(String[] args) throws IOException, UnsupportedAudioFileException {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FxmlUtil.showScene("/fxml/graph.fxml", stage, "Plot graph");
    }
}
