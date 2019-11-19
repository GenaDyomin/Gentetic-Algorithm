package sample;

import javax.xml.crypto.dom.DOMCryptoContext;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class Island {
    ArrayList<Double> points = new ArrayList<>();

    private ArrayList<Double> newGeneration = new ArrayList<>();

    private ArrayList<Double> result = new ArrayList<>();

    private int degreeOfMutation = 0;

    private Double accuracy;

    private int generation = 0;

    private String fileName;

    private int selection;

    private int mutation;

    private int population;

    public boolean ready = false;

    private double finalPoint;

    public double bestPoint;

    public Island(ArrayList<Double> points, String fileName, Double finalPoint, int mutation, int degreeOfMutation, double accuracy, int selection, int population) {
        this.points = points;
        this.fileName = fileName;
        this.finalPoint = finalPoint;
        this.mutation = mutation;
        this.degreeOfMutation = degreeOfMutation;
        this.accuracy = accuracy;
        this.selection = selection;
        this.population = population;
    }

    private void check() throws IOException {
        for (Double d : result
        ) {
            if (function(d) <= accuracy) {
                ready = true;
                bestPoint = d;
                Writer output;
                System.out.println(bestPoint);
                output = new BufferedWriter(new FileWriter("C:\\Users\\Gena\\IdeaProjects\\GA\\GA\\src\\sample\\Docs\\" + this.fileName, true));
                output.append("Result: " + bestPoint+ "\n");
                output.close();
            }
        }
    }

    public void change(Double finalPoint, int mutation, int degreeOfMutation, double accuracy, int selection, int population){
        this.finalPoint = finalPoint;
        this.mutation = mutation;
        this.degreeOfMutation = degreeOfMutation;
        this.accuracy = accuracy;
        this.selection = selection;
        this.population = population;
    }


    public boolean decision(int p) {
        return (Math.random() * 100 <= p);
    }

    public void createNewGeneration(int mutation, String lst) {
        String[] list = lst.split("\n");
        if (list.length > 1) {
            for (String a : list
            ) {
                String[] b = a.split(" ");
                if (b.length == 2) {
                    if (Integer.parseInt(b[0]) <= points.size() && Integer.parseInt(b[1]) <= points.size()) {
                        double firstIndivid = this.points.get(Integer.parseInt(b[0]));
                        double secondIndivid = this.points.get(Integer.parseInt(b[1]));
                        boolean mut = decision(mutation);
                        createChild(String.valueOf(firstIndivid) + " " + String.valueOf(secondIndivid), mut);
                    }
                } else
                    this.newGeneration.add(Double.parseDouble(b[0]));
            }
            for (int i = 0; i < newGeneration.size(); i++) {
                if (newGeneration.get(i) > 127.5)
                    newGeneration.set(i, BigDecimal.valueOf(newGeneration.get(i) - 256).setScale(3, RoundingMode.UP).doubleValue());
                if (newGeneration.get(i) < -127.5)
                    newGeneration.set(i, BigDecimal.valueOf(256 + newGeneration.get(i)).setScale(3, RoundingMode.UP).doubleValue());
            }
        }

    }

    public void createChild(String parents, boolean mutation) {
        boolean flag1 = false;
        boolean flag2 = false;
        String[] b = parents.split(" ");
        if (b[0].contains("-")) {
            flag1 = true;
        }
        if (b[1].contains("-")) {
            flag2 = true;
        }
        String genomF = convert(b[0]);
        String genomS = convert(b[1]);
        if (selection == 0) {
            String[] points = uniform(genomF + "!" + genomS).split("!");
            genomF = points[0];
            genomS = points[1];

        }
        if (selection == 1) {
            String[] points = onePoints(genomF + "!" + genomS).split("!");
            genomF = points[0];
            genomS = points[1];

        }
        if (selection == 2) {
            String[] points = twoPoints(genomF + "!" + genomS).split("!");
            genomF = points[0];
            genomS = points[1];

        }
        if (mutation) {
            genomF = mutation(genomF);
            genomS = mutation(genomS);
        }
        String[] a = genomF.split("\\.");
        String[] c = genomS.split("\\.");
        if (flag1) {
            String result = Integer.parseInt(a[0], 2) + "." + Integer.parseInt(a[1], 2);
            double q = -BigDecimal.valueOf(256.0 - Double.parseDouble(result)).setScale(3, RoundingMode.UP).doubleValue();
            newGeneration.add(q);
        } else {
            String result = (Integer.parseInt(a[0], 2) + "." + Integer.parseInt(a[1], 2));
            double q = BigDecimal.valueOf(Double.parseDouble(result)).setScale(3, RoundingMode.UP).doubleValue();
            newGeneration.add(q);
        }
        if (flag2) {
            String result = Integer.parseInt(c[0], 2) + "." + Integer.parseInt(c[1], 2);
            double d = -BigDecimal.valueOf(256.0 - Double.parseDouble(result)).setScale(3, RoundingMode.UP).doubleValue();
            newGeneration.add(d);
        } else {
            String result = (Integer.parseInt(c[0], 2) + "." + Integer.parseInt(c[1], 2));
            double d = BigDecimal.valueOf(Double.parseDouble(result)).setScale(3, RoundingMode.UP).doubleValue();
            newGeneration.add(d);
        }
    }

    public String convert(String value) {
        if (value.contains("-")) {
            value = value.replace("-", "");
            String[] a = value.split("\\.");
            if (!a[0].equals("0")) {
                String minus = String.valueOf(256 - Integer.parseInt(a[0]));
                value = minus + "." + a[1];
            }
        }
        BigDecimal t = new BigDecimal(value).setScale(3, RoundingMode.UP);
        value = t.toString();
        String[] b = value.split(" ");
        String[] values = b[0].split("\\.");
        values[0] = Integer.toBinaryString(Integer.parseInt(values[0]));
        values[0] = addZeroBeforeDot(values[0]);
        values[1] = Integer.toBinaryString(Integer.parseInt(values[1]));
        values[1] = addZeroAfterDot(values[1]);
        return values[0] + "." + values[1];
    }

    public String addZeroBeforeDot(String line) {
        while (line.length() < 8) {
            line = "0" + line;
        }
        return line;
    }

    public String addZeroAfterDot(String line) {
        while (line.length() < 12) {
            line = "0" + line;
        }
        return line;
    }

    public String mutation(String line) {
        String[] str = line.split("");
        line = "";
        if (degreeOfMutation == 1) {
            for (int i = 0; i < str.length; i++) {
                if (i % 2 == 0) {
                    if (str[i].equals(".")) {
                        line += ".";
                    }
                    if (str[i].equals("0"))
                        line += "1";
                    else
                        line += "0";
                } else
                    line += str[i];
            }
        } else {
            ArrayList<Integer> position = new ArrayList<>();
            position.add((int) (Math.random() * 8));
            position.add((int) (Math.random() * 8));
            position.add((int) (Math.random() * 8));
            position.add(8 + (int) (Math.random() * line.length()));
            position.add(8 + (int) (Math.random() * line.length()));
            for (int i = 0; i < str.length; i++) {
                if (position.contains(i)) {
                    if (str[i].equals(".")) {
                        line += ".";
                        position.add((int) (Math.random() * line.length()) + i);
                    }
                    if (str[i].equals("0"))
                        line += "1";
                    else
                        line += "0";
                } else
                    line += str[i];
            }
        }
        return line;
    }

    public String listOfLove(int size) {
        String result = "";
        ArrayList<Integer> list = new ArrayList<>();
        int count = size;
        while (count > 0) {
            if (count == 1) {
                int individ = (int) (Math.random() * size);
                if (!list.contains(individ))
                    result += individ;
                count -= 2;
            } else {
                int first = (int) (Math.random() * size);
                int second = (int) (Math.random() * size);
                if (first != second && !list.contains(first) && !list.contains(second)) {
                    result += first + " " + second + "\n";
                    list.add(first);
                    list.add(second);
                    count -= 2;
                }
            }
        }
        return result;
    }

    public void checkMutation(int degreeOfMutation) {
        this.degreeOfMutation = degreeOfMutation;
    }

    public String twoPoints(String line) {
        String[] mas = line.split("!");
        int dot1 = 1 + (int) (Math.random() * (mas[0].length()));
        if (dot1 == 7)
            dot1++;
        int dot2 = 1 + (int) (Math.random() * (mas[0].length()));
        if (dot2 == 7 || dot1 == dot2)
            dot2++;
        String[] line1 = mas[0].split("");
        String[] line2 = mas[1].split("");
        String res1 = "";
        String res2 = "";
        for (int i = 0; i < line1.length; i++) {
            if (i != 7) {
                if (dot1 < dot2) {
                    if (i < dot1) {
                        res1 += line1[i];
                        res2 += line2[i];
                    }
                    if (i >= dot1 && i < dot2) {
                        res1 += line2[i];
                        res2 += line1[i];
                    }
                    if (i >= dot2) {
                        res1 += line1[i];
                        res2 += line2[i];
                    }
                } else {
                    if (i < dot2) {
                        res1 += line1[i];
                        res2 += line2[i];
                    }
                    if (i >= dot2 && i < dot1) {
                        res1 += line2[i];
                        res2 += line1[i];
                    }
                    if (i >= dot1) {
                        res1 += line1[i];
                        res2 += line2[i];
                    }
                }
            }
        }
        return res1 + "!" + res2;
    }

    public String onePoints(String line) {
        String[] mas = line.split("!");
        int dot = 1 + (int) (Math.random() * (mas[0].length()));
        if (dot == 7)
            dot++;
        String[] line1 = mas[0].split("");
        String[] line2 = mas[1].split("");
        String res1 = "";
        String res2 = "";
        for (int i = 0; i < line1.length; i++) {
            if (i != 7) {
                if (i < dot) {
                    res1 += line1[i];
                    res2 += line2[i];
                }
                if (i >= dot) {
                    res1 += line2[i];
                    res2 += line1[i];
                }
            }
        }
        return res1 + "!" + res2;
    }

    public String uniform(String line) {
        String[] mas = line.split("!");
        String model = "";
        for (int i = 0; i < mas[0].length(); i++) {
            if ((int) (Math.random() * 2) == 0)
                model += "0";
            else
                model += "1";

        }
        String genomF = "";
        String genomS = "";
        String[] line1 = mas[0].split("");
        String[] line2 = mas[1].split("");
        String[] line3 = model.split("");
        for (int i = 0; i < line3.length; i++) {
            if (line3[i].equals("1")) {
                genomF += line1[i];
                genomS += line2[i];
            } else {
                genomF += line2[i];
                genomS += line1[i];
            }
            if (i == 7) {
                genomF += ".";
                genomS += ".";
            }

        }
        return genomF + "!" + genomS;
    }

    public void tournamentWithParent() throws IOException {
        checkMutation(mutation);
        String listOfSelection = listOfLove(points.size());
        createNewGeneration(mutation, listOfSelection);
        String grid = listOfLove(this.newGeneration.size());
        String[] tour = grid.split("\n");
        for (String t : tour
        ) {
            String[] fight = t.split(" ");
            if (fight.length == 2) {
                if (function(this.newGeneration.get(Integer.parseInt(fight[0]))) > function(this.newGeneration.get(Integer.parseInt(fight[1]))))
                    this.result.add(this.newGeneration.get(Integer.parseInt(fight[0])));
                else
                    this.result.add(this.newGeneration.get(Integer.parseInt(fight[1])));
            } else
                this.result.add(this.newGeneration.get(Integer.parseInt(fight[0])));

        }
        addBestParents();
        check();
    }

    public void tournamentWithoutParent() throws IOException {
        checkMutation(mutation);
        String listOfSelection = listOfLove(points.size()) + listOfLove(points.size());
        createNewGeneration(mutation, listOfSelection);
        String grid = listOfLove(this.newGeneration.size());
        String[] tour = grid.split("\n");
        for (String t : tour
        ) {
            String[] fight = t.split(" ");
            if (fight.length > 0) {
                if (fight.length == 2) {
                    if (function(this.newGeneration.get(Integer.parseInt(fight[0]))) > function(this.newGeneration.get(Integer.parseInt(fight[1])))) {
                        this.result.add(this.newGeneration.get(Integer.parseInt(fight[0])));
                    } else {
                        this.result.add(this.newGeneration.get(Integer.parseInt(fight[1])));

                    }
                } else {
                    this.result.add(this.newGeneration.get(Integer.parseInt(fight[0])));
                }
            }
        }
        check();
    }

    public void bestHalfWithParent() throws IOException {
        checkMutation(mutation);
        String listOfSelection = listOfLove(points.size());
        createNewGeneration(mutation, listOfSelection);
        HashMap<Double, Double> list = new HashMap<>();
        for (Double d : this.newGeneration
        ) {
            list.put(d, function(d));
        }
        sortMap(list);
        addBestParents();
        check();
    }

    public void bestHalfWithoutParent() throws IOException {
        checkMutation(mutation);
        String listOfSelection = listOfLove(points.size()) + listOfLove(points.size());
        createNewGeneration(mutation, listOfSelection);
        HashMap<Double, Double> list = new HashMap<>();
        for (Double d : this.newGeneration
        ) {
            list.put(d, function(d));
        }
        sortMap(list);
        check();
    }

    public void elitarTournament() throws IOException {
        tournamentWithoutParent();
        newGeneration.clear();
        //Map<Double, Double> sortList = new TreeMap<Double, Double>(list);
        double theBestParent = function(points.get(0));
        for (Double p : points) {
            if (Math.abs(function(p)) < Math.abs(theBestParent))
                theBestParent = p;
        }
        this.result.add(theBestParent);
    }

    public void proportionalSelection() throws IOException {
        checkMutation(mutation);
        HashMap<Double, Double> list = new HashMap<>();
        double globalFitness = 0;
        double count = 0;
        ArrayList<Double> keys = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        for (Double p : points
        ) {
            if (!keys.contains(p)) {
                keys.add(p);
                globalFitness += function(p);
            }
        }
        for (Double p : points
        ) {
            BigDecimal b = new BigDecimal(String.valueOf(function(p)/globalFitness)).setScale(3,RoundingMode.UP);
            list.put(p, b.doubleValue());
        }
        keys.clear();
        values.clear();
        for (Map.Entry<Double, Double> a : list.entrySet()
        ) {
            if (!keys.contains(a.getKey())) {
                keys.add(a.getKey());
                values.add(count + "!" + (a.getValue() + count) + "!" + a.getKey());
                count += a.getValue();
            }
        }
        points.clear();
        for (int i = 0; i < population; i++) {
            int p = (int) (Math.random() * 100);
            boolean flag = false;
            while (!flag) {
                for (String s : values
                ) {
                    String[] b = s.split("!");
                    if(b.length>2) {
                        if (p >= Double.parseDouble(b[0]) * 100 && p <= Double.parseDouble(b[1]) * 100) {
                            points.add(Double.parseDouble(b[2]));
                            flag = true;
                        }
                    }
                }
            }
        }
        String lst = listOfLove(points.size());
        HashMap<Double,Integer> select = new HashMap<>();
        createNewGeneration(mutation, lst);
        int c = 1;
        int amount = 0;
        while (select.size() < population&&amount<population) {
            for (int i = 0; i < newGeneration.size(); i++) {
                if(select.containsKey(newGeneration.get(i))&&select.get(newGeneration.get(i))<c) {
                    select.put(newGeneration.get(i), c + 1);
                    amount++;
                }
                if(!select.containsKey(newGeneration.get(i))) {
                    select.put(newGeneration.get(i), 1);
                    amount++;
                }
            }
            c++;
        }
        for (Map.Entry<Double,Integer> m:select.entrySet()
             ) {
            result.add(m.getKey());
        }
    }

    public void chronicler() throws IOException {
        //  PrintWriter pw = new PrintWriter(new FileOutputStream("C:/concat.txt", true));
        Writer output;
        this.generation++;
        output = new BufferedWriter(new FileWriter("C:\\Users\\Gena\\IdeaProjects\\GA\\GA\\src\\sample\\Docs\\" + this.fileName, true));
        output.append("Generation " + this.generation + "\n");
        for (Double a : result
        ) {
            output.append(a + "\n");
        }
        output.close();
        points.clear();
        points.addAll(result);
        newGeneration.clear();
        result.clear();
    }

    private Map<Double, Double> sortMap(HashMap<Double, Double> unsortMap) {
        Map<Double, Double> res = unsortMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        int count = population;
        for (Map.Entry<Double, Double> e : res.entrySet()
        ) {
            if (count >= 0) {
                this.result.add(e.getKey());
            }
            count--;
        }
        return res;
    }

    public ArrayList<Double> getBest() {
        HashMap<Double, Double> map = new HashMap<>();
        for (Double d : result
        ) {
            map.put(d, function(d));
        }
        int count = result.size() / 10;
        ArrayList<Double> forBarter = new ArrayList<>();
        for (Map.Entry<Double, Double> e : sortMap(map).entrySet()
        ) {
            if (count >= 0) {
                forBarter.add(e.getKey());
            }
            count--;
        }

        return forBarter;
    }

    private void addBestParents() {
        HashMap<Double, Double> map2 = new HashMap<>();
        for (Double d : points
        ) {
            map2.put(d, function(d));
        }
        int count = 1;
        for (Map.Entry<Double, Double> e : sortMap(map2).entrySet()
        ) {
            if (count > 0) {
                result.add(e.getKey());
            }
            count--;
        }
    }

    private double function2(double x) {
        double b = 0.05 * Math.pow((x - 1), 2) + (3 - 2.9 * (Math.pow(Math.E, (-2.77257 * x * x)))) * (1 - Math.cos(x * (4 - (50 * (Math.pow(Math.E, (-2.77257 * x * x)))))));
        BigDecimal a = new BigDecimal(String.valueOf(b)).setScale(3, RoundingMode.UP);
        return Math.abs(a.doubleValue() - finalPoint);
    }
    private double function(double x){
        double b =1 - 0.5*Math.cos(1.5*(10*x - 0.3))*Math.cos(31.4*x)+0.5*Math.cos(Math.pow(5,0.5)*10*x)*Math.cos(35*x);
        BigDecimal a = new BigDecimal(String.valueOf(b)).setScale(3, RoundingMode.UP);
        return Math.abs(a.doubleValue()-finalPoint);
    }

}
