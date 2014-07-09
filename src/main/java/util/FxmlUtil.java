package util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Dmitry on 2/4/14.
 */
public class FxmlUtil
{
    private static final Logger log = LoggerFactory.getLogger(FxmlUtil.class);

    public static Scene loadScene(String fxmlFile) throws IOException
    {
        log.debug("Loading FXML for main view from: {}", fxmlFile);
        FXMLLoader loader = new FXMLLoader();

        //loader.setResources(ResourceBundle.getBundle("conf.conf"));
        Parent rootNode = (Parent) loader.load(FxmlUtil.class.getResourceAsStream(fxmlFile));

        log.debug("Showing JFX scene");
        return new Scene(rootNode);
    }

    public static void showScene(String fxmlFile, Stage stage, String title) throws IOException
    {
        Scene scene = loadScene(fxmlFile);

        scene.getStylesheets().add("css/style.css");
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

}
