package controller;

import audio.processing.SimpleWaveformExtractor;
import audio.processing.WaveformExtractor;
import audio.processing.model.Complex;
import audio.processing.model.ComplexArray;
import audio.processing.transform.DFT;
import audio.processing.transform.FFT;
import audio.processing.transform.FFTTest;
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
           // double[] data = toDoubleArray(Arrays.copyOfRange(extractedData, 0, 3700));
            XYChart.Series<Number, Number> series = LineChartUtil.createNumberSeries(extractedData);
            series.setName(file.getName());
            lineChart.getData().add(series);
        }
    }

    private void printMax(double[] data) {
        int maxIndex = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i] > data[maxIndex]) {
                maxIndex = i;
            }
        }
        System.out.println("Max: index = " + maxIndex);
    }

    public void plotCosine() {
        double[] cos = createCos(16, 4);
        System.out.println("Cosine created. N = " + cos.length);
        FFT fft = new FFT();
        double[] empty = new double[cos.length];
        ComplexArray transformResult = fft.fft(cos);
        //FFTTest.transform(cos, empty);
        //Complex[] transformResult = new DFT().transform(cos);
        System.out.println("DFT applied");
        XYChart.Series<Number, Number> numberSeries = LineChartUtil.createNumberSeries(transformResult.getRealPart());
        //XYChart.Series<Number, Number> numberSeries = LineChartUtil.createNumberSeries(cos);
        lineChart.getData().add(numberSeries);
        System.out.println("Plotting...");
    }

    private double[] createCos(int sampleRate, int samples) {
        double[] result = new double[samples * sampleRate];
        for (int i = 0; i < samples; i++) {
            for (int j = 0; j < sampleRate; ++j) {
                result[i * sampleRate + j] = Math.cos(2.0 * Math.PI * j / sampleRate);
            }
        }
        return result;
    }

    private double[] toDoubleArray(int[] input) {
        double[] result = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            result[i] = input[i];
        }
        return result;
    }

    private double[] toReal(Complex[] complex) {
        double[] result = new double[complex.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = complex[i].getReal();
        }
        return result;
    }

    private double[] toAbsoluteArray(Complex[] complex) {
        int size = complex.length;
        double[] result = new double[size];
        int max = 0;
        int secondMax = 0;
        for (int i = 2; i < size; i++) {
            result[i] = complex[i].getAbsoluteValue();
            if (result[i] > result[max]) {
                max = i;
            } else if (result[i] > result[secondMax]) {
                secondMax = i;
            }
        }
        System.out.println("Max value:" + result[max] + ", index = " + max);
        System.out.println("Second max value:" + result[secondMax] + ", index = " + secondMax);

        return result;
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
