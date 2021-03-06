package controller;

import audio.features.FeatureExtractor;
import audio.features.mfcc.MfccExtractor;
import audio.features.spectral.RMS;
import audio.features.spectral.SpectralCentroid;
import audio.features.spectral.ZCR;
import audio.processing.waveform.*;
import audio.features.mfcc.MelFilterBank;
import audio.processing.model.ComplexArray;
import audio.processing.transformation.FFT;
import audio.processing.window.BlackmanWindow;
import audio.processing.window.HammingWindow;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import util.ArraysHelper;
import util.FileSystem;
import util.LineChartUtil;
import weka.core.Instances;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

import static util.ArraysHelper.average;
import static util.ArraysHelper.averageByColumn;

/**
 * Created by Dmitry on 6/27/2014
 */
public class Controller implements Initializable {

    public static final int RATE = 1;
    @FXML
    private LineChart<Number, Number> lineChart;
    @FXML
    private VBox root;

    public void plotDefault() throws IOException, UnsupportedAudioFileException, URISyntaxException {

        XYChart.Series<Number, Number> kNN = LineChartUtil.createNumberSeries(new double[]{0, 0, 0.773, 0.859, 0.866, 0.832});
        XYChart.Series<Number, Number> rf = LineChartUtil.createNumberSeries(new double[]{0, 0, 1.307, 1.654, 2.244, 2.469});
        XYChart.Series<Number, Number> svm = LineChartUtil.createNumberSeries(new double[]{0, 0, 1.091, 10.219, 12.002, 16.246});

        kNN.setName("kNN");
        rf.setName("Random forest");
        svm.setName("SVM");

        lineChart.getData().addAll(kNN, rf, svm);

    }

    private void plotWIndowFunctions() {
        double[] array = new double[128];
        Arrays.fill(array, 1);
        double[] windowed = new HammingWindow().apply(array);
        XYChart.Series<Number, Number> numberSeries = LineChartUtil.createNumberSeries(windowed);

        lineChart.getData().addAll(numberSeries);

        Arrays.fill(array, 1);
        windowed = new BlackmanWindow().apply(array);
        XYChart.Series<Number, Number> numberSeries1 = LineChartUtil.createNumberSeries(windowed);


        lineChart.getData().addAll(numberSeries1);
    }

    public void plotAudio() throws IOException, UnsupportedAudioFileException, WavFileException {
        File file = chooseFile();

        if (file != null) {
            double[] extractedData = extractWaveform(file);
            plot(extractedData);
        }
    }

    public void plotFFT() throws IOException, UnsupportedAudioFileException {
        File file = chooseFile();

        if (file != null) {
            double[] extractedData = extractWaveform(file);
            double[] windowed = new HammingWindow().apply(extractedData);
            ComplexArray fft = new FFT().fft(windowed);
            plot(toDbScale(fft));
        }
    }

    public void plotMelFilterBank() {
        MelFilterBank bank = new MelFilterBank(0, 22050, 1024, 26);
        System.out.println(bank.getFilterBank(0).length);
        for (int i = 0; i < bank.getFiltersCount(); i++) {
            plot(bank.getFilterBank(i));
        }
    }

    private double[] toDbScale(ComplexArray input) {
        double[] result = new double[input.getRealPart().length];
        for (int i = 0; i < result.length; i++) {
            double abs = Math.sqrt(Math.pow(input.getRealPart()[i], 2) + Math.pow(input.getImaginaryPart()[i], 2));
            result[i] = 20 * Math.log10(abs);
        }
        return result;
    }

    private double[] extractWaveform(File file) throws IOException, UnsupportedAudioFileException {
        return new WavFileExtractor().extract(file);
    }

    private File chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(FileSystem.getResourceFolderPath()));

        return fileChooser.showOpenDialog(null);
    }

    private void print(double[] data) {
        System.out.println(Arrays.toString(data));
    }

    private void plot(int[] array) {
        plot(ArraysHelper.toDoubleArray(array));
    }

    private void plot(double[] array) {
        XYChart.Series<Number, Number> numberSeries = LineChartUtil.createNumberSeries(array, RATE);
        //lineChart.getData().clear();
        lineChart.getData().addAll(numberSeries);
    }

    public void plotCosine() {
        double[] cos = createCos(16, 4);
        System.out.println("Cosine created. N = " + cos.length);
        FFT fft = new FFT();
        double[] empty = new double[cos.length];
        ComplexArray transformResult = fft.fft(cos);
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

    public void clear() {
        lineChart.getData().clear();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //lineChart.getXAxis().setAutoRanging(true);
        //lineChart.getYAxis().setAutoRanging(true);
//        lineChart.getStyleClass().add("thin-chart");
        //lineChart.setCreateSymbols(false);

    }
}
