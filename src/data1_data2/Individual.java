package data1_data2;

/**
 *
 * @author aaronsantamaria
 */
public class Individual {

    public Individual() {
    }

    public Individual(Individual i) {
        this.fitness = i.getFitness();
        this.gene = i.getGene();
    }

    public Individual(int[] gene, int fitness) {
        this.fitness = fitness;
        this.gene = gene;
    }

    public static int N = 10;///////////////////////////////////////////////////

//    public static int getN() {
//        return N;
//    }
//
//    public static void setN(int N) {
//        Individual.N = N;
//    }
    public int[] gene = new int[N];////////////////////////////////////////////

    public int[] getGene() {
        return gene;
    }

    public void setGene(int[] gene) {
        this.gene = gene;
    }

    public int getGene(int index) {
        return this.gene[index];
    }

    public void setGene(int index, int gene) {
        this.gene[index] = gene;
    }

    private int fitness = 0;////////////////////////////////////////////////////

    public void incFit() {
        this.fitness++;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public String printGene() {
        String wholeGene = "";
        for (int i = 0; i < N; i++) {
            wholeGene += (gene[i] + " ");
        }
        wholeGene += (" Fitness: " + fitness);
        return wholeGene;
    }

}//exit-Individual
