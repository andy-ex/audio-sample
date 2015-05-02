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

    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, URISyntaxException {
        new Controller().plotDefault();
    }

    public void plotDefault() throws IOException, UnsupportedAudioFileException, URISyntaxException {

        FeatureExtractor extractor = new SpectralCentroid();

        List<String> genres = Arrays.asList("classical", "metal", "rock");

        for (String genre : genres) {
            System.out.println("Genre: " + genre);
            for (int i = 0; i < 10; ++i) {
                File file = new File(FileSystem.getResourceURL("/genres/" + genre + "/" + genre + "." + i + ".wav").toURI());
                double[] waveform = extractWaveform(file);
                //double[][] coefficients = new MfccExtractor().extractCoefficients(waveform, 22050);
                //System.out.println(average(averageByColumn(coefficients)));
                System.out.println(ArraysHelper.average(extractor.extract(waveform, 22050)[0]));
            }
            System.out.println();
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
        lineChart.getStyleClass().add("thin-chart");
        lineChart.setCreateSymbols(false);

    }
}
