package md;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Chart {


    private NumberAxis xAxis = new NumberAxis();
    private NumberAxis yAxis = new NumberAxis();
    private NumberAxis xAxis1 = new NumberAxis();
    private NumberAxis yAxis1 = new NumberAxis();
    private NumberAxis xAxis2 = new NumberAxis();
    private NumberAxis yAxis2 = new NumberAxis();
    private NumberAxis xAxis3 = new NumberAxis();
    private NumberAxis yAxis3 = new NumberAxis();
    private NumberAxis xAxis4 = new NumberAxis();
    private NumberAxis yAxis4 = new NumberAxis();

    private LineChart<Number, Number> lineP = new LineChart<Number, Number>(xAxis, yAxis);
    private LineChart<Number, Number> lineK = new LineChart<Number, Number>(xAxis1, yAxis1);
    private LineChart<Number, Number> lineT = new LineChart<>(xAxis2, yAxis2);
    private LineChart<Number, Number> lineS = new LineChart<Number, Number>(xAxis3, yAxis3);
    private LineChart<Number, Number> C = new LineChart<Number, Number>(xAxis4, yAxis4);
    private XYChart.Series kinetyczna;
    private XYChart.Series potencjalna;
    private XYChart.Series calkowita;
    private XYChart.Series sprezysta;
    private XYChart.Series pressure;
    private Double max;

    public void start(XYChart.Series kinetyczna,
                      XYChart.Series potencjalna,
                      XYChart.Series calkowita,
                      XYChart.Series sprezysta, Double max,XYChart.Series pressure) {
        this.calkowita = calkowita;
        this.potencjalna = potencjalna;
        this.kinetyczna = kinetyczna;
        this.sprezysta = sprezysta;
        this.max = max;
        this.pressure = pressure;


        lineS.setCreateSymbols(false);
        lineK.setCreateSymbols(false);
        lineP.setCreateSymbols(false);
        lineT.setCreateSymbols(false);
        C.setCreateSymbols(false);


        lineT.getData().add(calkowita);
        lineK.getData().add(kinetyczna);
        lineP.getData().add(potencjalna);
        lineS.getData().add(sprezysta);
        C.getData().add(pressure);

        Stage window = new Stage();
        window.setTitle("Wykresy");

        Button btn = new Button("Zapisz");

        btn.setOnAction(e -> {

            FileChooser fileChooser = new FileChooser();

            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV type files", "*.csv");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            File file = fileChooser.showSaveDialog(window);

            try {
                FileWriter archivo = new FileWriter(file);

                for (int i = 0; i < potencjalna.getData().size(); i++) {

                    archivo.write(String.valueOf(potencjalna.getData().get(i)) + String.valueOf(potencjalna.getData().get(i)) + String.valueOf(potencjalna.getData().get(i)) + String.valueOf(potencjalna.getData().get(i))+String.valueOf(pressure.getData().get(i)));
                    archivo.flush();
                    archivo.write("\n");

                }
                archivo.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        });


        HBox wers = new HBox(lineK, lineP);
        HBox wers2 = new HBox(lineS, lineT);
        HBox wers3 = new HBox(C);
        wers.setAlignment(Pos.CENTER);
        wers2.setAlignment(Pos.CENTER);
        wers3.setAlignment(Pos.CENTER);

        VBox pion = new VBox(wers, wers2,wers3, btn);
        pion.setAlignment(Pos.CENTER);
        Scene scene = new Scene(pion, 800, 1100);
        window.setScene(scene);
        window.show();


    }
}


