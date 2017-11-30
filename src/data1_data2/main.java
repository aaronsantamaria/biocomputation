package data1_data2;

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
    static int D = 32; //size of Data set
    static int P = 100; //population size
    static int G = 200; //number of generations
    static int variablesSize = 0; //number of data variables = 5 or 6
    static int min = 0;
    static int max = (P - 1);

    static int totalFit = 0;
    static int meanFit = 0;
    static int bestFit = 0;
    static int[] meanFitArray = new int[G];
    static int[] bestFitArray = new int[G];
    static int[] totalFitArray = new int[G];

    //OG values m=0.05 & c=0.6
    //P100 and G 200

    static double mutationRate = 0.05; //0.01 for data set 1 //0.05 for data set 2

    static double crossoverRate = 0.6; //0.6-0.9 //0.06 for data set 2

    static Individual[] population = new Individual[P];
    static Individual[] offspring = new Individual[P];
    static Data[] data = new Data[D];

    static PrintWriter printOut;

    static int[][] mA = new int[5][G];
    static int[][] bA = new int[5][G];

    public static void main(String[] args) throws FileNotFoundException {

        readInData("data2.txt");
        
        /// loop 
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
        }//loop

        //printAllFitness();
        System.out.println(q);

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
            tempMean += mA[y][(G-1)];
            tempBest += bA[y][(G-1)];
        }
        tempMean = (tempMean / 5);
        tempBest = (tempBest / 5);

        System.out.println("mean: " + tempMean);
        System.out.println("best: " + tempBest);

        writeToCSV();

    }//exit-main

    public static void createPop() {
        Individual.N = (variablesSize + 1) * NumR;
        Random rand = new Random();
        for (int i = 0; i < P; i++) {
            population[i] = new Individual();
            for (int j = 0; j < Individual.N; j++) {
                if (((j + 1) % (Rule.ConL + 1)) == 0) {
                    population[i].setGene(j, rand.nextInt(2));
                } else {
                    population[i].setGene(j, rand.nextInt(3));
                }
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
                int[] temp1 = offspring[i].getGene();
                int[] temp2 = offspring[i + 1].getGene();
                int[] child1 = new int[Individual.N];
                int[] child2 = new int[Individual.N];

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
        for (int i = 0; i < P; i++) {
            for (int j = 0; j < Individual.N; j++) {
                double mutation = Math.random(); //random number, 0.00-0.99
                if (mutation < mutationRate) {
                    //if (offspring[i].getGene(j) == 1) {
                    //offspring[i].setGene(j, 0);
                    //} else {
                    //offspring[i].setGene(j, 1);
                    //}
                    if (((j + 1) % (Rule.ConL + 1)) == 0) {
                        population[i].setGene(j, rand.nextInt(2));
                    } else {
                        population[i].setGene(j, rand.nextInt(3));
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
            rulebase[i].setOut(solution.getGene(k));
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

    public static boolean matches(int[] var, int[] cond) {
        boolean match = true;
        for (int i = 0; i < cond.length; i++) {
            if (cond[i] != var[i]) {
                if (cond[i] != 2) {
                    match = false;
                }
            }
        }
        return match;
    }//end - matches

    public static void readInData(String dataSetName) {
        Scanner countRows = new Scanner(main.class.getResourceAsStream(dataSetName));
        String[] split1 = null;
        int numberOfLines = 0;

        String skip1 = countRows.nextLine(); //skip first line of data set

        while (countRows.hasNextLine()) {
            String tempV = countRows.next(); //temp1 will hold variables as a string
            String tempC = countRows.next(); //temp2 will hold the class, but we dont use it yet  
            split1 = tempV.split(""); //split temp1 into an array, to count number of variables 
            numberOfLines++; //incremented after each line, used to initialize Data array
        }//end while loop - counting number of rows & variables 

        countRows.close();

        variablesSize = split1.length;
        Data.Vars = variablesSize;
        Rule.ConL = variablesSize;
        D = numberOfLines;
        data = new Data[D];

        Scanner readData = new Scanner(main.class.getResourceAsStream(dataSetName));
        int index = 0;
        String skip2 = readData.nextLine(); //first line contains redundant data

        while (readData.hasNextLine()) {
            String variableString = readData.next();
            String classString = readData.next();
            String[] variableSplit = variableString.split("");
            data[index] = new Data();
            for (int i = 0; i < Data.Vars; i++) {
                int variable = Integer.parseInt(variableSplit[i]);
                data[index].setVariables(i, variable);
            }//end for loop - add variables into data 
            data[index].setClass1(Integer.parseInt(classString));
            index++;
        }//end while loop - reading in countRows 
        readData.close();
    }//end - readInData

    public static void oldGA() {
        stuff.createPop();
        countFitNormalGA();
        printPop();

        meanFitArray[0] = meanFit;
        bestFitArray[0] = bestFit;
        totalFitArray[0] = totalFit;
        for (int i = 0; i < 49; i++) {
            chooseOff();
            switchOffToPop();
            crossover();
            mutation();
            countFitNormalGA();
            printPop();

            meanFitArray[i + 1] = meanFit;
            bestFitArray[i + 1] = bestFit;
            totalFitArray[i + 1] = totalFit;
        }//loop

        printAllFitness();
    }//oldGA

    public static void printAllFitness() {
        for (int i = 0; i < G; i++) {
            System.out.print("Gen_" + i + "  Total = "
                    + totalFitArray[i] + "  Mean = " + meanFitArray[i]
                    + "  Best = " + bestFitArray[i]);
            System.out.println("");
        }//loop G - generations
    }//end - printAllFitness

    public static void writeToCSV() throws FileNotFoundException {

        printOut = new PrintWriter(new File("dataCSV.csv")); //printing
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

