/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package animalforgingevolutionofgoaldirectedcognition;

import java.util.ArrayList;

/**
 *
 * @author wkentkovac
 */
public class EvolutionReproduction {
    
    final private ArrayList<animal> currentGen;
    final private int populationSize;
    
    private ArrayList<animal> bestGenotype;
    private int bestFitness = 0;
    private float popFit = 0, popNoTurn = 0, popTurn = 0, popMemory = 0, popDefense = 0;
    private float bestFit = 0, bestNoTurn = 0, bestTurn = 0, bestMemory = 0, bestDefense = 0;

    public EvolutionReproduction( ArrayList<animal> currentGen ){
        this.currentGen = currentGen;
        this.populationSize = this.currentGen.size();
        
        bestGenotype = new ArrayList<>();
        
        findBestGenotype();
    }
    
    private void findBestGenotype(){
        
        for( animal individual : currentGen ){
            if( individual.fitness() > bestFitness && individual.isAlive() ){
                bestGenotype.clear();
                bestGenotype.add( individual );
            }
        
            else if ( individual.fitness() == bestFitness && individual.isAlive() )
                bestGenotype.add( individual );
            
            else
                continue;
            
            //population fitness assessments 
            popFit += (float)individual.fitness();
            popNoTurn += (float)individual.getNoTurn();
            popTurn += (float)individual.getTurn();
            popMemory += (float)individual.getMemory();
            popDefense += (float)individual.getDefense();
        }
        
        for( animal a : currentGen ){
            bestFit = (float)a.fitness();
            bestNoTurn = (float)a.getNoTurn();
            bestTurn = (float)a.getTurn();
            bestMemory = (float)a.getMemory();
            bestDefense = (float)a.getDefense();
        }
        
        //averaged population scores
        popFit = (float)popFit / (float)currentGen.size();
        popNoTurn = (float)popNoTurn / (float)currentGen.size();
        popTurn = (float)popTurn / (float)currentGen.size();
        popMemory = (float)popMemory / (float)currentGen.size();
        popDefense = (float)popDefense / (float)currentGen.size();
        
        //best genotype(s) scores
        bestFit = (float)bestFit / (float)currentGen.size();
        bestNoTurn = (float)bestNoTurn / (float)currentGen.size();
        bestTurn = (float)bestTurn / (float)currentGen.size();
        bestMemory = (float)bestMemory / (float)currentGen.size();
        bestDefense = (float)bestDefense / (float)currentGen.size();
        
        
    }
    
    protected int numBestFitness(){
        return bestGenotype.size();
    }
    
    protected float bestFit(){
        return bestFit;
    }
    
    protected float bestNoTurn(){
        return bestNoTurn;
    }
    
    protected float bestTurn(){
        return bestTurn;
    }
    
    protected float bestMemory(){
        return bestMemory;
    }
    
    protected float bestDefense(){
        return bestDefense;
    }
    
    protected float popFit(){
        return popFit;
    }
    
    protected float popNoTurn(){
        return popNoTurn;
    }
    
    protected float popTurn(){
        return popTurn;
    }
    
    protected float popMemory(){
        return popMemory;
    }
    
    protected float popDefense(){
        return popDefense;
    }
}
