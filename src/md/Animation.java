package md;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;


public class Animation {

    private Timeline t1;
    private int licznik = 0;
    private double max = 0;
    private AnchorPane anchor;
    private int nAtomset, atomsSize;
    private ArrayList<Circle> circles;
    private int boxWidth = 100;
    private double[] x, y, vx, vy;
    private Double verletStep;
    private XYChart.Series kinetyczna = new XYChart.Series();
    private XYChart.Series potencjalna = new XYChart.Series();
    private XYChart.Series calkowita = new XYChart.Series();
    private XYChart.Series sprezysta = new XYChart.Series();
    private XYChart.Series pressureW = new XYChart.Series();

    public void start(Integer nAtomset, Integer atomsSize, Double verletStep) {

        this.nAtomset = nAtomset;
        this.atomsSize = atomsSize;
        this.verletStep = verletStep;


        Stage window = new Stage();
        window.setTitle("Animation");

        makeCircles(nAtomset, boxWidth);
        MD md = new MD(x, y, vx, vy, boxWidth);
        circles = new ArrayList<>();

        for (int i = 0; i < x.length; i++) {
            Circle circle = new Circle(x[i], y[i], atomsSize);
            Random generator = new Random();
            circle.setFill(Color.color(generator.nextDouble(), generator.nextDouble(), generator.nextDouble()));
            circles.add(circle);
        }

        anchor = new AnchorPane();


        KeyFrame kf = new KeyFrame(Duration.millis(10), event -> {

            for (int i = 0; i < circles.size(); i++) {
                circles.get(i).setCenterX(anchor.getWidth() / 100 * md.getX()[i]);
                circles.get(i).setCenterY(anchor.getHeight() / 100 * md.getY()[i]);

            }
            md.verletStep(verletStep);

            potencjalna.setName("Energia potencjalna");
            potencjalna.getData().add(new XYChart.Data(licznik, md.pE));

            kinetyczna.setName("Energia kinetyczna");
            kinetyczna.getData().add(new XYChart.Data(licznik, md.kE));

            sprezysta.setName("Energia sprężystości");
            sprezysta.getData().add(new XYChart.Data(licznik, md.eE));

            calkowita.setName("Energia całkowita");
            calkowita.getData().add(new XYChart.Data(licznik, md.te));

            pressureW.setName("Ciśnienie");
            pressureW.getData().add(new XYChart.Data(licznik, md.getPressure()));

            licznik++;

            if (max < md.te) {
                max = md.te;
            }


        });

        anchor.getChildren().addAll(circles);

        t1 = new Timeline();
        t1.setCycleCount(Timeline.INDEFINITE);
        t1.getKeyFrames().add(kf);
        t1.setAutoReverse(false);
        t1.play();

        Scene scene = new Scene(anchor, 600, 600);
        window.setScene(scene);
        window.show();
        window.setOnCloseRequest(e -> {

            Chart ch = new Chart();
            ch.start(kinetyczna, potencjalna, calkowita, sprezysta, max, pressureW);
            t1.stop();

        });

    }

    private void makeCircles(int nAtoms, int boxWidth) {

        x = new double[nAtoms];
        y = new double[nAtoms];
        vx = new double[nAtoms];
        vy = new double[nAtoms];
        Random rng = new Random();
        petla:
        for (int i = 0; i < nAtoms; i++) {
            x[i] = rng.nextDouble() * boxWidth;
            y[i] = rng.nextDouble() * boxWidth;
            for (int j = 0; j < i; j++) {
                double r = (x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j]) * (y[i] - y[j]);
                if (r < 1) {
                    i--;
                    continue petla;

                }
            }
        }


        for (int i = 0; i < nAtoms; i++) {
            double dir = (rng.nextDouble() - 0.5) * 2;
            double v = rng.nextGaussian() * 3;
            vx[i] = v * (1 - Math.abs(dir));
            vy[i] = v * dir;
        }
    }
}
