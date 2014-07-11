package controller;

import audio.processing.SimpleWaveformExtractor;
import audio.processing.WaveformExtractor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import util.FileSystemUtil;
import util.LineChartUtil;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by Dmitry on 6/27/2014.
 */
public class Controller implements Initializable {

    @FXML
    private LineChart<Number, Number> lineChart;
    @FXML
    private VBox root;

    public void plotDefault() throws IOException, UnsupportedAudioFileException {

        AudioInputStream rock = AudioSystem.getAudioInputStream(FileSystemUtil.getResourceURL("/rock.au"));
        AudioInputStream classical = AudioSystem.getAudioInputStream(FileSystemUtil.getResourceURL("/classical.au"));

        WaveformExtractor extractor = new SimpleWaveformExtractor();

        XYChart.Series<Number, Number> rockSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> classicalSeries = new XYChart.Series<>();

        int category = 0;
        int[] extract = extractor.extract(rock);
        for (int i = 0; i < extract.length; i = i + 100) {
            rockSeries.getData().add(new XYChart.Data<>(category++, extract[i]));
        }
        category = 0;
        int[] extract1 = extractor.extract(classical);
        for (int i = 0; i < extract1.length; i = i + 100) {
            classicalSeries.getData().add(new XYChart.Data<>(category++, extract1[i]));
        }

        lineChart.getData().add(rockSeries);
        lineChart.getData().add(classicalSeries);
    }

    public void plotAudio() throws IOException, UnsupportedAudioFileException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(FileSystemUtil.getResourceFolderPath()));

        File file;
        if ( (file = fileChooser.showOpenDialog(root.getScene().getWindow())) != null) {
            AudioInputStream in = AudioSystem.getAudioInputStream(file);
            int[] extractedData = new SimpleWaveformExtractor().extract(in);
            extractedData = Arrays.copyOfRange(extractedData, 0, 100);
            XYChart.Series<Number, Number> series = LineChartUtil.createNumberSeries(extractedData);
            series.setName(file.getName());
            lineChart.getData().add(series);
        }
    }

    public void clear() {
        lineChart.getData().clear();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //lineChart.getXAxis().setAutoRanging(true);
        //lineChart.getYAxis().setAutoRanging(true);
        lineChart.getStyleClass().add("thin-chart");
        lineChart.setCreateSymbols(false);

    }
}
