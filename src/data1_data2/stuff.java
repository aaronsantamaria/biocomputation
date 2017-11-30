/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data1_data2;

import static data1_data2.main.population;
import static data1_data2.main.P;

/**
 *
 * @author aaronsantamaria
 */
public class stuff {
    
    public static void createPop() {
        //create population
        for (int i = 0; i < P; i++) {
            population[i] = new Individual();
            for (int j = 0; j < Individual.N; j++) {
                int randInt = (int) Math.round(Math.random());
                population[i].setGene(j, randInt);
            }//loop-N
            population[i].setFitness(0);
        }//loop-P
    }//end-createPop
    
}
