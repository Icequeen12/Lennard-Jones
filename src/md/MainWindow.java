package md;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainWindow extends Application {
    private Animation animation;
    private Scene sceneMain;
    private Button btnAnimation;
    private TextField nAtomsText, partSizeText, verletStep;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)  {


        VBox layoutMain = new VBox(20, displayStuff(primaryStage));
        sceneMain = new Scene(layoutMain, 400, 500);
        primaryStage.setScene(sceneMain);
        primaryStage.show();
        primaryStage.setTitle("Main");
    }

    private VBox displayStuff(Stage stage) {


        btnAnimation = new Button("Animation");
        btnAnimation.setPrefWidth(150);
        Label labelError = new Label("");
        labelError.setTextFill(Color.RED);
        btnAnimation.setOnAction(e -> {

            animation = new Animation();

            try {
                Integer.parseInt(nAtomsText.getText());
                Integer.parseInt(partSizeText.getText());
                Double.parseDouble(verletStep.getText());
            } catch (NumberFormatException e1) {
                nAtomsText.setText("0");
                partSizeText.setText("0");
                verletStep.setText("0");
            } finally {
                if (Integer.parseInt(nAtomsText.getText()) > 900 || Integer.parseInt(nAtomsText.getText()) < 1) {
                    labelError.setText("Wrong number of atoms!");
                } else if (Integer.parseInt(partSizeText.getText()) > 10 || Integer.parseInt(partSizeText.getText()) < 1) {
                    labelError.setText("Wrong size of atoms!");
                } else if (Double.parseDouble(verletStep.getText()) < 0 || Double.parseDouble(verletStep.getText()) > 1) {
                    labelError.setText("Wrong step value!");
                } else {
                        animation.start(Integer.parseInt(nAtomsText.getText()), Integer.parseInt(partSizeText.getText()), Double.parseDouble(verletStep.getText()));
                }
            }

        });

        Label labelAtoms = new Label("Pick number of atoms (1-900)");
        nAtomsText = new TextField();
        nAtomsText.setText("30");
        nAtomsText.setMaxWidth(80);
        nAtomsText.setAlignment(Pos.CENTER);


        Label labelPart = new Label("Pick size of atoms (1-10)");
        partSizeText = new TextField();
        partSizeText.setText("5");
        partSizeText.setMaxWidth(80);
        partSizeText.setAlignment(Pos.CENTER);

        Label labelStep = new Label("Set step for Verlet algorythm (>0)");
        verletStep = new TextField();
        verletStep.setText("0.01");
        verletStep.setMaxWidth(80);
        verletStep.setAlignment(Pos.CENTER);

        labelAtoms.setFont(Font.font("Arial", 18));
        labelPart.setFont(Font.font("Arial", 18));
        labelStep.setFont(Font.font("Arial", 18));

        labelAtoms.setPadding(new Insets(50, 0, 0, 0));

        labelError.setPadding(new Insets(30, 0, 0, 0));


        VBox box = new VBox(20, labelAtoms, nAtomsText, labelPart, partSizeText, labelStep, verletStep, btnAnimation, labelError);
        box.setAlignment(Pos.CENTER);
        return box;
    }


}
