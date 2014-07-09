package controller;

import audio.processing.SimpleWaveformExtractor;
import audio.processing.WaveformExtractor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import util.FileSystemUtil;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by Dmitry on 6/27/2014.
 */
public class Controller implements Initializable {

    @FXML
    private LineChart<Number, Number> lineChart;

    public void drawChart() throws IOException, UnsupportedAudioFileException {


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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //lineChart.getXAxis().setAutoRanging(true);
        //lineChart.getYAxis().setAutoRanging(true);
        lineChart.getStyleClass().add("thin-chart");
        lineChart.setCreateSymbols(false);

    }
}
