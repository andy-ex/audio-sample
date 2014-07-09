package sample;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;



public class TestLauncher extends Application {

    BorderPane pane;
    XYChart.Series series1 = new XYChart.Series();
    SimpleDoubleProperty rectinitX = new SimpleDoubleProperty();
    SimpleDoubleProperty rectX = new SimpleDoubleProperty();
    SimpleDoubleProperty rectY = new SimpleDoubleProperty();

    @Override
    public void start(Stage stage) {

        final NumberAxis xAxis = new NumberAxis(1, 12, 1);
        final NumberAxis yAxis = new NumberAxis(0.53000, 0.53910, 0.0005);

        xAxis.setAnimated(false);
        yAxis.setAnimated(false);

        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis) {
            @Override
            public String toString(Number object) {
                return String.format("%7.5f", object);
            }
        });

        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);

        lineChart.setCreateSymbols(false);
        lineChart.setAlternativeRowFillVisible(false);
        lineChart.setAnimated(false);
        lineChart.setLegendVisible(false);

        series1.getData().add(new XYChart.Data(1, 0.53185));
        series1.getData().add(new XYChart.Data(2, 0.532235));
        series1.getData().add(new XYChart.Data(3, 0.53234));
        series1.getData().add(new XYChart.Data(4, 0.538765));
        series1.getData().add(new XYChart.Data(5, 0.53442));
        series1.getData().add(new XYChart.Data(6, 0.534658));
        series1.getData().add(new XYChart.Data(7, 0.53023));
        series1.getData().add(new XYChart.Data(8, 0.53001));
        series1.getData().add(new XYChart.Data(9, 0.53589));
        series1.getData().add(new XYChart.Data(10, 0.53476));

        pane = new BorderPane();
        pane.setCenter(lineChart);
        Scene scene = new Scene(pane, 800, 600);
        lineChart.getData().addAll(series1);

        stage.setScene(scene);

        scene.setOnMouseClicked(mouseHandler);
        scene.setOnMouseDragged(mouseHandler);
        scene.setOnMouseEntered(mouseHandler);
        scene.setOnMouseExited(mouseHandler);
        scene.setOnMouseMoved(mouseHandler);
        scene.setOnMousePressed(mouseHandler);
        scene.setOnMouseReleased(mouseHandler);
        stage.show();
    }
    EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {

            if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
                rectinitX.set(mouseEvent.getX());
            } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED || mouseEvent.getEventType() == MouseEvent.MOUSE_MOVED) {
                LineChart<Number, Number> lineChart = (LineChart<Number, Number>) pane.getCenter();
                NumberAxis xAxis = (NumberAxis) lineChart.getXAxis();

                double Tgap = xAxis.getWidth() / (xAxis.getUpperBound() - xAxis.getLowerBound());
                double newXlower = xAxis.getLowerBound(), newXupper = xAxis.getUpperBound();
                double Delta = 0.3;

                if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                    if (rectinitX.get() < mouseEvent.getX()) {
                        newXlower = xAxis.getLowerBound() - Delta;
                        newXupper = xAxis.getUpperBound() - Delta;
                    } else if (rectinitX.get() > mouseEvent.getX()) {
                        newXlower = xAxis.getLowerBound() + Delta;
                        newXupper = xAxis.getUpperBound() + Delta;
                    }
                    xAxis.setLowerBound(newXlower);
                    xAxis.setUpperBound(newXupper);
                }
                rectinitX.set(mouseEvent.getX());
            }
        }
    };

    public static void main(String[] args) {
        launch(args);
    }
}