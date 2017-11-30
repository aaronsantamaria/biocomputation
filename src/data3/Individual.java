package data3;

/**
 *
 * @author aaronsantamaria
 */
public class Individual {

    public Individual() {
    }

    public static int N = 130;///////////////////////////////////////////////////
    
    public float[] gene = new float[N];////////////////////////////////////////////

    public float[] getGene() {
        return gene;
    }

    public void setGene(float[] gene) {
        this.gene = gene;
    }

    public float getGene(int index) {
        return this.gene[index];
    }

    public void setGene(int index, float gene) {
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
