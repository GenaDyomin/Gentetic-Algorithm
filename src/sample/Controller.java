package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Controller {

    @FXML
    private Label islandOne;

    @FXML
    private Label islandTwo;

    @FXML
    private Label islandThree;

    @FXML
    private Label islandFour;

    @FXML
    private Label islandFive;

    @FXML
    private TextField mutatuion;

    @FXML
    private Button launch;

    @FXML
    private TextField iterations;

    @FXML
    private TextField Accuaracy;

    @FXML
    private TextField checkPoint;

    @FXML
    private LineChart<String, Number> chart;

    @FXML
    private TextField amountIndivid;

    @FXML
    private RadioButton sMutation;

    @FXML
    private ToggleGroup group1;

    @FXML
    private TextField fExchange;

    @FXML
    private RadioButton wMuatation;

    @FXML
    private Button setParams;

    @FXML
    private RadioButton op;

    @FXML
    private ToggleGroup group2;

    @FXML
    private RadioButton tp;

    @FXML
    private RadioButton uni;

    private ArrayList<Double> allPoints = new ArrayList<>();

    private int pMuatation;
    private int amountOfIterations;
    private double accuracy;
    private double endPoint;
    private int population;
    private int degreeOfMutation;
    private int numExchange;
    private int selection;
    private Island island1;
    private Island island2;
    private Island island3;
    private Island island4;
    private Island island5;
    boolean flag = false;

    @FXML
    void launch(ActionEvent event) throws IOException {
        int count = amountOfIterations;
        int exch = numExchange;
        while (count>0){
            if(!island1.ready){
                island1.tournamentWithParent();
                island1.chronicler();
            }
            else {
                islandOne.setStyle("-fx-background-color: #4DB64A");
                islandOne.setText(String.valueOf(island1.bestPoint));
            }
           if(!island2.ready){
                island2.tournamentWithoutParent();
                island2.chronicler();
            }
            else {
               islandTwo.setStyle("-fx-background-color: #4DB64A");
               islandTwo.setText(String.valueOf(island2.bestPoint));
            }
            if(!island3.ready){
                island3.bestHalfWithParent();
                island3.chronicler();
            }
            else {
                islandThree.setStyle("-fx-background-color: #4DB64A");
                islandThree.setText(String.valueOf(island3.bestPoint));
            }
            if(!island4.ready){
                island4.bestHalfWithoutParent();
                island4.chronicler();
            }
            else {
                islandFour.setStyle("-fx-background-color: #4DB64A");
                islandFour.setText(String.valueOf(island4.bestPoint));
            }
            if(!island5.ready){
                island5.proportionalSelection();
                island5.chronicler();
            }
            else {
                islandFive.setStyle("-fx-background-color: #4DB64A");
                islandFive.setText(String.valueOf(island5.bestPoint));
            }
            count--;
            exch--;
            if(exch==0){
                stockExchangeVar1();
                exch=numExchange;
            }
        }

    }

    public ArrayList<Double> pointForDrowing(){
        ArrayList<Double> dots = new ArrayList<>();
        for (double i = -1; i<1;i+=0.01) {
            BigDecimal b =  new BigDecimal(String.valueOf(i)).setScale(3, RoundingMode.UP);
            dots.add(b.doubleValue());
        }
        return dots;
    }

    private void stockExchangeVar1(){
        ArrayList<Double> p1 = island1.getBest();
        ArrayList<Double> p2 = island2.getBest();
        ArrayList<Double> p3 = island3.getBest();
        ArrayList<Double> p4 = island4.getBest();
        ArrayList<Double> p5 = island5.getBest();
        island1.points.addAll(p5);
        island1.points.addAll(p4);
        island2.points.addAll(p5);
        island2.points.addAll(p1);
        island3.points.addAll(p1);
        island3.points.addAll(p2);
        island4.points.addAll(p2);
        island4.points.addAll(p3);
        island5.points.addAll(p3);
        island5.points.addAll(p4);
    }

    @FXML
    void setParams(ActionEvent event) {
        numExchange = Integer.parseInt(fExchange.getText());
        pMuatation = Integer.parseInt(mutatuion.getText());
        amountOfIterations = Integer.parseInt(iterations.getText());
        accuracy = Double.parseDouble(Accuaracy.getText());
        endPoint = function(Double.parseDouble(checkPoint.getText()));
        population = Integer.parseInt(amountIndivid.getText());
        if (wMuatation.isSelected()) {
            degreeOfMutation = 0;
        } else
            degreeOfMutation = 1;
        if(op.isSelected())
            selection=1;
        if(tp.isSelected())
            selection=2;
        if(uni.isSelected())
            selection=3;
        for (double i = endPoint-50; i > -100; i -= 0.125) {
            allPoints.add(i/100);
        }
        for (double i = endPoint+50; i < 100; i += 0.125) {
            allPoints.add(i/100);
        }
        if(!flag) {
            island1 = new Island(getPoints(), "island1.txt", endPoint, pMuatation, degreeOfMutation, accuracy, selection, population);
            island2 = new Island(getPoints(), "island2.txt", endPoint, pMuatation, degreeOfMutation, accuracy, selection, population);
            island3 = new Island(getPoints(), "island3.txt", endPoint, pMuatation, degreeOfMutation, accuracy, selection, population);
            island4 = new Island(getPoints(), "island4.txt", endPoint, pMuatation, degreeOfMutation, accuracy, selection, population);
            island5 = new Island(getPoints(), "island5.txt", endPoint, pMuatation, degreeOfMutation, accuracy, selection, population);
            flag=true;
        }
        else {
            island1.change(endPoint, pMuatation, degreeOfMutation, accuracy, selection, population);
            island2.change(endPoint, pMuatation, degreeOfMutation, accuracy, selection, population);
            island3.change(endPoint, pMuatation, degreeOfMutation, accuracy, selection, population);
            island4.change(endPoint, pMuatation, degreeOfMutation, accuracy, selection, population);
            island5.change(endPoint, pMuatation, degreeOfMutation, accuracy, selection, population);
        }
        XYChart.Series<String, Number> series =  new XYChart.Series<String, Number>();
        for (Double d:pointForDrowing()
             ) {
            series.getData().add(new XYChart.Data<String, Number>(String.valueOf(d),function(d)));
        }
        chart.getData().add(series);

    }

    private ArrayList<Double> getPoints() {
        ArrayList<Double> result = new ArrayList<>();
        for (int i = 0; i < population; i++) {
            BigDecimal dot = new BigDecimal(String.valueOf(allPoints.get((int) (Math.random() * allPoints.size())))).setScale(3,RoundingMode.UP);
            result.add(dot.doubleValue());
        }
        return result;
    }

    private double function2(double x){
        double b =0.05*Math.pow((x-1),2)+(3-(2.9*(Math.pow(Math.E,(-2.77257*x*x)))))*(1-Math.cos(x*(4-(50*(Math.pow(Math.E,(-2.77257*x*x)))))));
        BigDecimal a = new BigDecimal(String.valueOf(b)).setScale(3, RoundingMode.UP);
        return a.doubleValue();
    }

    private double function(double x){
        double b =1 - 0.5*Math.cos(1.5*(10*x - 0.3))*Math.cos(31.4*x)+0.5*Math.cos(Math.pow(5,0.5)*10*x)*Math.cos(35*x);
        BigDecimal a = new BigDecimal(String.valueOf(b)).setScale(3, RoundingMode.UP);
        return a.doubleValue();
    }

}
