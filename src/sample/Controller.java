package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        exchange.setVisible(false);
    }

    @FXML
    private Button runner;

    @FXML
    private Button chart;

    @FXML
    private Label islandOne;

    @FXML
    private Label islandTwo;

    @FXML
    private Label exchange;

    @FXML
    private Label islandThree;

    @FXML
    private Label islandFour;

    @FXML
    private Label islandFive;

    @FXML
    private Button mutation;

    @FXML
    private TextField mutatuionText;

    private int valueOfMutation = 0;

    private int step = 0;

    private boolean flag = false;

    private ArrayList<Double> points1 = new ArrayList<>();
    private ArrayList<Double> points2 = new ArrayList<>();
    private ArrayList<Double> points3 = new ArrayList<>();
    private ArrayList<Double> points4 = new ArrayList<>();
    private ArrayList<Double> points5 = new ArrayList<>();
    private ArrayList<Double> points6 = new ArrayList<>();

    private Island island1;
    private Island island2;
    private Island island3;
    private Island island4;
    private Island island5;
    private Island island6;

    @FXML
    void drawChart(ActionEvent event) {

    }

    @FXML
    void setMutation(ActionEvent event) {
        this.valueOfMutation = Integer.parseInt(mutatuionText.getText());
    }

    @FXML
    void start(ActionEvent event) throws IOException {
        exchange.setVisible(false);
        if (!flag) {
            generatePoints();
            flag = true;

        }
        if (step == 0) {
            island1 = new Island(points1, "island1.txt", -5.4);
            island2 = new Island(points2, "island2.txt", -5.4);
            island3 = new Island(points3, "island3.txt", -5.4);
            island4 = new Island(points4, "island4.txt", -5.4);
            island5 = new Island(points5, "island5.txt", -5.4);
            //island6 = new Island(points6, "island6.txt", -5.4, this.valueOfMutation);*/
        } else {
            //Change model
            if (step % 5 == 0) {
                exchange.setVisible(true);
                points1 = island1.getBest();
                points2 = island2.getBest();
                points3 = island3.getBest();
                points4 = island4.getBest();
                points5 = island5.getBest();
                island1.points.addAll(points5);
                island1.points.addAll(points4);
                island2.points.addAll(points5);
                island2.points.addAll(points1);
                island3.points.addAll(points2);
                island3.points.addAll(points1);
                island4.points.addAll(points3);
                island4.points.addAll(points2);
                island5.points.addAll(points3);
                island5.points.addAll(points4);

            }
            if (!island1.ready) {
                island1.tournamentWithoutParent(decision());
                island1.chronicler();
            } else {
                islandOne.setStyle("-fx-background-color: #4DB64A");
            }
            if (!island2.ready) {
                island2.tournamentWithParent(decision());
                island2.chronicler();

            } else {
                islandTwo.setStyle("-fx-background-color: #4DB64A");
            }
            if (!island3.ready) {
                island3.bestHalfWithoutParent(decision());
                island3.chronicler();
            } else {
                islandThree.setStyle("-fx-background-color: #4DB64A");
            }
            if (!island4.ready) {
                island4.bestHalfWithParent(decision());
                island4.chronicler();
            } else {
                islandFour.setStyle("-fx-background-color: #4DB64A");
            }
            if (!island5.ready) {
                island5.proportionalSelection(decision());
                island5.chronicler();
            } else {
                islandFive.setStyle("-fx-background-color: #4DB64A");
            }
           /* if (!island6.ready) {
                island6.proportionalSelection();

            } else {
                islandTwo.setStyle("-fx-background-color: #4DB64A");
            }*/
        }
        this.step++;

    }

    private boolean decision() {
        int p = (int) ((Math.random() * 101) + 1);
        if (p <= valueOfMutation)
            return true;
        else
            return false;
    }

    private void generatePoints() {
        ArrayList<Double> allPoints = new ArrayList<>();
        for (double i = -50; i < -10; i += 0.25) {
            allPoints.add(i);
        }
        for (double i = 10; i < 50; i += 0.25) {
            allPoints.add(i);
        }
        Collections.shuffle(allPoints);
        int i = 0;
        for (Double b : allPoints
        ) {
            if (i == 0)
                points1.add(b);
            if (i == 1)
                points2.add(b);
            if (i == 2)
                points3.add(b);
            if (i == 3)
                points4.add(b);
            if (i == 4)
                points5.add(b);
            if (i == 5) {
                points6.add(b);
                i = -1;
            }
            i++;
        }
    }

}
