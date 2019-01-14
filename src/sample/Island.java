package sample;

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

    private int generation = 0;

    private String fileName;

    public boolean ready = false;

    private double finalPoint;

    public Island(ArrayList<Double> points, String fileName, Double finalPoint) {
        this.points = points;
        this.fileName = fileName;
        this.finalPoint = finalPoint;
    }

    private void check() {
        for (Double d : result
        ) {
            if (Math.abs(d - finalPoint) < 0.5) {
                System.out.println("Point has found");
                System.out.println(d);
                ready = true;
            }
        }
    }

    public void generator() {
     /*   for (Double d : this.points
        ) {
            System.out.println(d);
        }*/

    }

    public void createNewGeneration(boolean mutation, String lst) {
        System.out.println("Creating new generation");
        String[] list = lst.split("\n");
        System.out.println(lst);
        if(list.length>1) {
            for (String a : list
            ) {
                System.out.println("PARA");
                System.out.println(a);
                String[] b = a.split(" ");
                if (b.length == 2) {
                    double firstIndivid = this.points.get(Integer.parseInt(b[0]));
                    double secondIndivid = this.points.get(Integer.parseInt(b[1]));
                    createChild(String.valueOf(firstIndivid) + " " + String.valueOf(secondIndivid), mutation);
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
        System.out.println("PARENTS");
        System.out.println(parents);
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
        System.out.println("Before");
        System.out.println(genomF);
        System.out.println(genomS);
        genomF = genomF.replaceAll("\\.", "");
        genomS = genomS.replaceAll("\\.", "");
        String model = "";
        for (int i = 0; i < genomF.length(); i++) {
            if ((int) (Math.random() * 2) == 0)
                model += "0";
            else
                model += "1";

        }
        System.out.println("Model: " + model);
/*        int gap = 5 + (int) (Math.random() * 18);
        System.out.println(gap);*/
        String[] line1 = genomF.split("");
        String[] line2 = genomS.split("");
        String[] line3 = model.split("");
        System.out.println(genomF);
        genomF = "";
        genomS = "";
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
        System.out.println("After");
        System.out.println(genomF);
        System.out.println(genomS);
        if (mutation) {
            System.out.println("Mutation");
            genomF = mutation(genomF);
            genomS = mutation(genomS);
            System.out.println(genomF);
            System.out.println(genomS);
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
        System.out.println(newGeneration.get(newGeneration.size() - 1));
        System.out.println(newGeneration.get(newGeneration.size() - 2));
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
        if (degreeOfMutation >= 10) {
            System.out.println("Strong Mutation!!");
            System.out.println(line);
            if (degreeOfMutation == 10) {
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
            }
            if (degreeOfMutation == 11) {
                for (int i = 0; i < str.length; i++) {
                    if (i % 2 != 0) {
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
                degreeOfMutation = 0;
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
        System.out.println(line);
        return line;
    }

    public String listOfLove(int size) {
        System.out.println("SIZE: " +size);
        String result = "";
        ArrayList<Integer> list = new ArrayList<>();
        int count = size;
        System.out.println("CREATE LIST OF SELECTION");
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
        System.out.println("END");
        return result;
    }

    public void checkMutation(boolean mutation) {
        if (mutation)
            degreeOfMutation++;
    }

    public void tournamentWithParent(boolean mutation) throws IOException {
        checkMutation(mutation);
        String listOfSelection = listOfLove(points.size());
        createNewGeneration(mutation, listOfSelection);
        String grid = listOfLove(this.points.size() + this.newGeneration.size());
        this.points.addAll(this.newGeneration);
        String[] tour = grid.split("\n");
        for (String t : tour
        ) {
            String[] fight = t.split(" ");
            if (fight.length == 2) {
                if (function(this.points.get(Integer.parseInt(fight[0]))) > function(this.points.get(Integer.parseInt(fight[1]))))
                    this.result.add(this.points.get(Integer.parseInt(fight[0])));
                else
                    this.result.add(this.points.get(Integer.parseInt(fight[1])));
            } else
                this.result.add(this.points.get(Integer.parseInt(fight[0])));

        }
        check();
    }

    public void tournamentWithoutParent(boolean mutation) throws IOException {
        checkMutation(mutation);
        String listOfSelection = listOfLove(points.size()) + listOfLove(points.size());
        createNewGeneration(mutation, listOfSelection);
        String grid = listOfLove(this.newGeneration.size());
        System.out.println("Size of new Generation" + " " + newGeneration.size());
        String[] tour = grid.split("\n");
        for (String t : tour
        ) {
            String[] fight = t.split(" ");
            if (fight.length == 2) {
                if (function(this.newGeneration.get(Integer.parseInt(fight[0]))) > function(this.newGeneration.get(Integer.parseInt(fight[1])))) {
                    if (!this.result.contains(this.newGeneration.get(Integer.parseInt(fight[0])))) {
                        this.result.add(this.newGeneration.get(Integer.parseInt(fight[0])));
                    }
                } else {
                    if (!this.result.contains(this.newGeneration.get(Integer.parseInt(fight[1])))) {
                        this.result.add(this.newGeneration.get(Integer.parseInt(fight[1])));
                    }

                }
            } else {
                if (!this.result.contains(this.newGeneration.get(Integer.parseInt(fight[0])))) {
                    this.result.add(this.newGeneration.get(Integer.parseInt(fight[0])));
                }
            }

        }
        check();
    }

    public void bestHalfWithParent(boolean mutation) throws IOException {
        checkMutation(mutation);
        String listOfSelection = listOfLove(points.size());
        createNewGeneration(mutation, listOfSelection);
        this.points.addAll(this.newGeneration);
        HashMap<Double, Double> list = new HashMap<>();
        for (Double d : this.points
        ) {
            list.put(d, function(d));
        }
        sortMap(list);
        check();
    }

    public void bestHalfWithoutParent(boolean mutation) throws IOException {
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
        check();
    }

    public void elitarTournament(boolean mutation) throws IOException {
        System.out.println("ELITAR");
        tournamentWithoutParent(mutation);
        newGeneration.clear();
        //Map<Double, Double> sortList = new TreeMap<Double, Double>(list);
        double theBestParent = function(points.get(0));
        for (Double p : points) {
            if (Math.abs(function(p)) < Math.abs(theBestParent))
                theBestParent = p;
        }
        System.out.println("Elit");
        System.out.println(theBestParent);
        this.result.add(theBestParent);
    }

    public void proportionalSelection(boolean mutation) throws IOException {
        checkMutation(mutation);
        HashMap<Double, Double> list = new HashMap<>();
        double globalFitness = 0;
        for (Double p : points
        ) {
            System.out.println(p);
            list.put(p, function(p));
            globalFitness += function(p);
        }
        globalFitness = globalFitness / list.size();
        ArrayList<Double> listOfParents = new ArrayList<>();
        for (Map.Entry<Double, Double> entry : list.entrySet()
        ) {
            double chance = entry.getValue() / globalFitness;
            while (chance >= 1) {
                listOfParents.add(entry.getKey());
                chance--;
            }
            int p = (int) (Math.random() * 101) + 1;
            if (chance >= p)
                listOfParents.add(entry.getKey());
        }
        System.out.println("-----------");
        System.out.println(points.size());
        for (Double d : listOfParents
        ) {
            System.out.println(d);
        }
        System.out.println(listOfParents.size());
        System.out.println("-----------");
        Collections.shuffle(listOfParents);
        points.clear();
        points.addAll(listOfParents);
        String lst = listOfLove(listOfParents.size());
        createNewGeneration(mutation, lst);
        list.clear();
        for (Double d : points
        ) {
            list.put(d, function(d));
        }
        sortMap(list);
        check();
        result.addAll(newGeneration);
      /*  Set<Double> set = new HashSet<>(result);
        result.clear();
        result.addAll(set);
        int next = 2;
        for (Map.Entry<Double, Double> entry : sortMap(list).entrySet()
        ) {
            if (next != 0) {
                result.add(entry.getKey());
            }
            next--;
        }*/

    }

    public void chronicler() throws IOException {
        //  PrintWriter pw = new PrintWriter(new FileOutputStream("C:/concat.txt", true));
        Writer output;
        this.generation++;
        //  FileWriter fileWriter = new FileWriter("C:\\Users\\Gena\\IdeaProjects\\GA\\src\\sample\\Docs\\"+this.name, true);
        output = new BufferedWriter(new FileWriter("C:\\Users\\Gena\\IdeaProjects\\GA\\src\\sample\\Docs\\" + this.fileName, true));
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
        int count = res.size() / 2;
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

    public double function(double x) {
        double value = 5 - 24 * x + 17 * Math.pow(x, 2) - Math.pow(x, 3) * 3.7 + 0.25 * Math.pow(x, 4);
        double rang = Math.abs(finalPoint) - Math.abs(value);
        return rang;
    }

}
