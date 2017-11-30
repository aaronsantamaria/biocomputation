package data3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author aaronsantamaria
 */
public class main {

    static int NumR = 10; //number of rules
    static int D = 2000; //size of Data set
    static int P = 100; //population size
    static int G = 1000; //number of generations
    static int variablesSize = 12; //number of data variables
    static int min = 0;
    static int max = (P - 1);

    static int totalFit = 0;
    static int meanFit = 0;
    static int bestFit = 0;
    static int[] meanFitArray = new int[G];
    static int[] bestFitArray = new int[G];
    static int[] totalFitArray = new int[G];

    //0.05
    static double mutationRate = 0.05; //0.01 for data set 1 //0.05 for data set 2
    //0.6
    static double crossoverRate = 0.00; //0.6-0.9 //0.06 for data set 2

    static Individual[] population = new Individual[P];
    static Individual[] offspring = new Individual[P];
    static Data[] data = new Data[D];

    static PrintWriter printOut;

    static float step = 0.100001f;

    static int[][] mA = new int[5][G];
    static int[][] bA = new int[5][G];

    ///data set 3 - ((6*2)+1) * 10 = 130
    // mutation - +/- rand(step) .... step = 0.1 
    //random + or - 
    // step is another parameter 0.1 could be 0.01 - 0.001
    //add or subtract a random number between the step 
    public static void main(String[] args) throws FileNotFoundException {

        readInData("data3.txt");

        for (int q = 0; q < 5; q++) {

            createPop();
            calculateAllFitness(0); //calculate fitness for the first individual
            //printPop();

            for (int i = 0; i < G - 1; i++) {
                chooseOff();
                crossover();
                mutation();
                switchOffToPop();
                calculateAllFitness(i + 1);
                //printPop();
                System.out.println(i);
            }//loop

            System.out.println("NEXT RUN"+q);

            for (int y = 0; y < meanFitArray.length; y++) {
                //mA[q][y] = new int[10][G];
                mA[q][y] = meanFitArray[y];
                bA[q][y] = bestFitArray[y];
            }

        }
        //end loop 
        float tempMean = 0;
        float tempBest = 0;
        for (int y = 0; y < 5; y++) {
            tempMean += mA[y][(G - 1)];
            tempBest += bA[y][(G - 1)];
        }
        tempMean = (tempMean / 5);
        tempBest = (tempBest / 5);

        System.out.println("mean: " + tempMean);
        System.out.println("best: " + tempBest);

        //printAllFitness();
        writeToCSV2();
    }//exit-main

    public static void createPop() {
        Individual.N = (variablesSize + 1) * NumR; //should be 130
        Random rand = new Random();
        for (int i = 0; i < P; i++) {
            population[i] = new Individual();
            for (int j = 0; j < Individual.N; j++) {
                population[i].setGene(j, rand.nextFloat());
            }//loop N 
            //population[i].setFitness(0);
        }//loop P
    }//end - createPop

    public static void printPop() {
        for (int i = 0; i < P; i++) {
            System.out.println(population[i].printGene());
        }
        System.out.println("Total = " + totalFit + "  Mean = " + meanFit
                + "  Best = " + bestFit + "\n");
    }//end - printPop

    public static void printOff() {
        for (int i = 0; i < P; i++) {
            System.out.println(offspring[i].printGene());
        }//loop-P
    }//end - printOff

    public static void printData() {
        for (int i = 0; i < D; i++) {
            System.out.println(data[i].printData());
        }//loop-P
    }//end - printData

    public static void countFitNormalGA() {
        totalFit = 0;
        meanFit = 0;
        bestFit = 0;
        for (int i = 0; i < P; i++) {
            population[i].setFitness(0);
            for (int j = 0; j < Individual.N; j++) {
                if (population[i].getGene(j) == 1) {
                    population[i].incFit();
                    totalFit++;
                }
            }//loop-N
            if (bestFit < population[i].getFitness()) {
                bestFit = population[i].getFitness();
            }
        }//loop-P
        meanFit = (totalFit / P);
    }//end - countFitNormalGA

    public static void chooseOff() {
        for (int i = 0; i < P; i++) {
            offspring[i] = new Individual();
            int p1 = min + (int) (Math.random() * ((max - min) + 1));
            int p2 = min + (int) (Math.random() * ((max - min) + 1));

            int pop1 = population[p1].getFitness();
            int pop2 = population[p2].getFitness();

            if (pop1 > pop2) {
                offspring[i] = population[p1];
            } else {
                offspring[i] = population[p2];
            }
        }//loop-P
    }//end - chooseOff

    public static void switchOffToPop() {

        int fit = 0;

        for (int i = 0; i < P; i++) {
            if (population[i].getFitness() > population[fit].getFitness()) {
                fit = i;
            }
        }

        for (int i = 0; i < P; i++) {
            if (i != fit) {
                population[i] = new Individual();
                population[i] = offspring[i];
            }
        }
//        for (int i = 0; i < P; i++) {
//            population[i] = new Individual();
//            population[i] = offspring[i];
//        }//loop-P
    }//end - switchOffToPop

    public static void crossover() {
        Random rand = new Random();
        for (int i = 0; i < P; i += 2) {
            int crossPoint = rand.nextInt(Individual.N);
            //crosspoint changes for each Individual
            double cross = Math.random(); //random number, 0.00-0.99
            if (cross < crossoverRate) {
                float[] temp1 = offspring[i].getGene();
                float[] temp2 = offspring[i + 1].getGene();
                float[] child1 = new float[Individual.N];
                float[] child2 = new float[Individual.N];

                for (int j = 0; j < Individual.N; j++) {
                    if (j < crossPoint) {
                        child1[j] = temp1[j];
                        child2[j] = temp2[j];

                    } else {
                        child1[j] = temp2[j];
                        child2[j] = temp1[j];
                    }
                }//loop-J
                offspring[i] = new Individual();
                offspring[i + 1] = new Individual();
                offspring[i].setGene(child1);
                offspring[i + 1].setGene(child2);
            }//crossover rate
        }//loop-P
    }//end - crossover

    public static void mutation() {
        Random rand = new Random();
        Random rand2 = new Random();
        //Random r = new Random();
        for (int i = 0; i < P; i++) {
            for (int j = 0; j < Individual.N; j++) {
                double mutation = Math.random(); //random number, 0.00-0.99
                if (mutation < mutationRate) {
                    //boolean a = r.nextBoolean();
                    int minusOrPlus = rand.nextInt(2);
                    float random = (float) (0.0 + rand2.nextFloat() * (step - 0));
                    if (minusOrPlus == 0) {
                        //step down
                        float minus = population[i].getGene(j);
                        minus = minus - random;
                        if (minus < 0) {
                            minus = 0.02f;
                        }
                        if (minus > 0.99) {
                            minus = 0.98f;
                        }
                        population[i].setGene(j, minus);
                    } else {
                        //step up
                        float plus = population[i].getGene(j);
                        plus = plus + random;
                        if (plus < 0) {
                            plus = 0.02f;
                        }
                        if (plus > 0.99) {
                            plus = 0.98f;
                        }
                        population[i].setGene(j, plus);
                    }
                }//if-mutation
            }//loop-N
            //int temp = countFitness(offspring[i]);
            //offspring[i].setFitness(temp);
        }//loop-P
    }//end - mutation

    public static int countFitness(Individual solution) {
        int fitness = 0;
        int k = 0;
        Rule[] rulebase = new Rule[NumR];

        for (int i = 0; i < NumR; i++) {
            rulebase[i] = new Rule();

            for (int j = 0; j < Rule.ConL; j++) {
                rulebase[i].setCond(j, solution.getGene(k));
                k++;
            }
            int temp = (int) Math.round(solution.getGene(k));
            rulebase[i].setOut(temp);
            k++;
        }//NumR

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < NumR; j++) {
                if (matches(data[i].getVariables(), rulebase[j].getCond())) {
                    if (data[i].getClass1() == rulebase[j].getOut()) {
                        fitness++;
                    }
                    break;// note it is important to get the next data item after a match
                }
            }
        }//end for loop
        return fitness;
    }//end - countFitness

    public static boolean matches(float[] var, float[] cond) {
        boolean match = true;
        int index = 0;
        for (int i = 0; i < cond.length; i += 2) {
            if (var[i / 2] > cond[i] && var[i / 2] < cond[i + 1]) {
                //if (cond[i] != 2) {
                match = false;
                //}
            }
            index++;
        }
        return match;
    }//end - matches

    public static void readInData(String dataSetName) {

        data = new Data[D];

        Scanner readData = new Scanner(main.class.getResourceAsStream(dataSetName));
        int index = 0;
        String skipRow1 = readData.nextLine(); //first line contains redundant data

        while (readData.hasNextLine()) {
            String oneLine = readData.nextLine();

            String[] split = oneLine.split(" ");

            data[index] = new Data();
            data[index].setVariables(0, Float.parseFloat(split[0]));
            data[index].setVariables(1, Float.parseFloat(split[1]));
            data[index].setVariables(2, Float.parseFloat(split[2]));
            data[index].setVariables(3, Float.parseFloat(split[3]));
            data[index].setVariables(4, Float.parseFloat(split[4]));
            data[index].setVariables(5, Float.parseFloat(split[5]));
            data[index].setClass1(Integer.parseInt(split[6]));
            //for (int i = 0; i < oneLine.length(); i++) {
            //if (i > oneLine.length() - 1) {
            //System.out.println("split[6]: " + split[i]);
            //int tempC = Integer.parseInt(split[i]);
            //data[index].setClass1(tempC);
            //} else {
            //float tempV = Float.parseFloat(split[i]);
            //data[index].setVariables(i, tempV);
            //}
            //}//end for loop - add variables into data
            index++;
        }//end while loop - reading in countRows 
        readData.close();
    }//end - readInData

    public static void printAllFitness() {
        for (int i = 0; i < G; i++) {
            System.out.print("Gen_" + i + "  Total = "
                    + totalFitArray[i] + "  Mean = " + meanFitArray[i]
                    + "  Best = " + bestFitArray[i]);
            System.out.println("");
        }//loop G - generations
    }//end - printAllFitness

    public static void writeToCSV() throws FileNotFoundException {

        printOut = new PrintWriter(new File("dataCSV.csv"));

        StringBuilder csv = new StringBuilder();

        csv.append("Generation");
        csv.append(',');
        csv.append("Mean Fitness");
        csv.append(',');
        csv.append("Best Fitness");
        csv.append('\n');

        for (int i = 0; i < G; i++) {
            csv.append(i + 1);
            csv.append(',');
            csv.append(meanFitArray[i]);
            csv.append(',');
            csv.append(bestFitArray[i]);
            csv.append('\n');
        }
        printOut.write(csv.toString());

        printOut.close();
    }//end - writeToCSV - writeToCSV - writeToCSV - writeToCSV - writeToCSV
    
    public static void writeToCSV2() throws FileNotFoundException {

        printOut = new PrintWriter(new File("4000.csv")); //printing
        StringBuilder csv = new StringBuilder();//printing

        csv.append("MEAN");
        csv.append(',');
        csv.append("=AVERAGE(B203:K203)");
        csv.append('\n');

        csv.append("BEST");
        csv.append(',');
        csv.append("=AVERAGE(B404:K404)");
        csv.append('\n');

        csv.append("Generation");
        csv.append(',');
        csv.append("Mean Fitness");
        csv.append('\n');

        for (int i = 0; i < G; i++) {
            csv.append(i + 1);
            csv.append(',');
            csv.append(mA[0][i]);
            csv.append(',');
            csv.append(mA[1][i]);
            csv.append(',');
            csv.append(mA[2][i]);
            csv.append(',');
            csv.append(mA[3][i]);
            csv.append(',');
            csv.append(mA[4][i]);
            csv.append('\n');
        }

        csv.append("Generation");
        csv.append(',');
        csv.append("Best Fitness");
        csv.append('\n');

        for (int i = 0; i < G; i++) {
            csv.append(i + 1);
            csv.append(',');
            csv.append(bA[0][i]);
            csv.append(',');
            csv.append(bA[1][i]);
            csv.append(',');
            csv.append(bA[2][i]);
            csv.append(',');
            csv.append(bA[3][i]);
            csv.append(',');
            csv.append(bA[4][i]);
            csv.append('\n');
        }
        printOut.write(csv.toString());
        printOut.close();

    }//end - writeToCSV - writeToCSV - writeToCSV - writeToCSV - writeToCSV

    public static void calculateAllFitness(int index) {
        meanFit = 0;
        bestFit = 0;
        totalFit = 0;
        for (int i = 0; i < P; i++) {
            int tempFitness = countFitness(population[i]);
            population[i].setFitness(tempFitness);
            totalFit += tempFitness;
            if (bestFit < tempFitness) {
                bestFit = tempFitness;
            }
        }
        meanFit = (totalFit / P);
        meanFitArray[index] = meanFit;
        bestFitArray[index] = bestFit;
        totalFitArray[index] = totalFit;
    }//end - calculateAllFitness - calculateAllFitness - calculateAllFitness

}//exit - class

