package controller;

import audio.processing.mfcc.MfccExtractor;
import audio.processing.waveform.*;
import audio.processing.mfcc.MelFilterBank;
import audio.processing.model.ComplexArray;
import audio.processing.transformation.FFT;
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

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * Created by Dmitry on 6/27/2014
 */
public class Controller implements Initializable {

    public static final int RATE = 1;
    @FXML
    private LineChart<Number, Number> lineChart;
    @FXML
    private VBox root;

    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, URISyntaxException {
        double d = (double)6/10;
        System.out.println(d);
        new Controller().plotDefault();
    }

    public void plotDefault() throws IOException, UnsupportedAudioFileException, URISyntaxException {
        File file = new File(FileSystem.getResourceURL("/sine-440.wav").toURI());

        if (file != null) {
            double[] waveform = extractWaveform(file);
            double[][] coefficients = new MfccExtractor().extractCoefficients(waveform, 44100);
            for (double[] coefficient : coefficients) {
                print(coefficient);
            }
        }
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
        MelFilterBank bank = new MelFilterBank();
        int[] melSpacedFrequencies = bank.getMelSpacedFrequencies(300, 8000, 512, 10);
        System.out.println(Arrays.toString(melSpacedFrequencies));

        for (int i = 1; i < melSpacedFrequencies.length; i++) {
            plot(bank.createFilterBank(melSpacedFrequencies, i));
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
        lineChart.getStyleClass().add("thin-chart");
        lineChart.setCreateSymbols(false);

    }
}
