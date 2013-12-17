/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package animalforgingevolutionofgoaldirectedcognition;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 *
 * @author wkentkovac
 */
public class FitnessEvaluation {
    
    private ArrayList<ForgingAnimal> currentGen;
    final private int numGenerations;
    
    private ArrayList<ForgingAnimal> bestGenotype;
    private ArrayList< float[] > populationFitnessLog;
    private ArrayList< float[] > bestFitnessLog;
    
    private int generation;
    
    private int bestFitness = 0;
    private float popFit = 0, popNoTurn = 0, popTurn = 0, popMemory = 0, popDefense = 0;
    private float bestFit = 0, bestNoTurn = 0, bestTurn = 0, bestMemory = 0, bestDefense = 0;

    public FitnessEvaluation( int numGenerations ){
        this.numGenerations = numGenerations;
        
        populationFitnessLog = new ArrayList<>();
        bestFitnessLog = new ArrayList<>();
        
        for ( int i = 0; i < 5; i++ ){
            populationFitnessLog.add( new float[this.numGenerations] );
            bestFitnessLog.add( new float[this.numGenerations] );
        }
        
        bestGenotype = new ArrayList<>();
        generation = 0;
    }
    
    protected void assessGenerationFitness( ArrayList<ForgingAnimal> currentGen ) throws Exception{
        
        if( numGenerations <= generation )
            throw new Exception( "Too Many Generations!" );
        
        this.currentGen = currentGen;
        
        bestGenotype.clear();
        
        findBestGenotype();                
        
        generation++;
    }
    
    private void findBestGenotype(){
        
        for( ForgingAnimal individual : currentGen ){
            if( individual.fitness() > bestFitness && individual.isAlive() ){
                bestGenotype.clear();
                bestGenotype.add( individual );
            }
        
            //else if ( individual.fitness() == bestFitness && individual.isAlive() )
            //    bestGenotype.add( individual );
            
            //else
            //    continue;
            
            //population fitness assessments 
            popFit += (float)individual.fitness();
            popNoTurn += (float)individual.getNoTurn();
            popTurn += (float)individual.getTurn();
            popMemory += (float)individual.getMemory();
            popDefense += (float)individual.getDefense();
        }
        
        for( ForgingAnimal a : bestGenotype ){
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
        
        populationFitnessLog.get(0)[ generation ] = popFit;
        populationFitnessLog.get(1)[ generation ] = popNoTurn;
        populationFitnessLog.get(2)[ generation ] = popTurn;
        populationFitnessLog.get(3)[ generation ] = popMemory;
        populationFitnessLog.get(4)[ generation ] = popDefense;
        
        //best genotype(s) scores
        bestFit = (float)bestFit; // (float)currentGen.size();
        bestNoTurn = (float)bestNoTurn; // (float)currentGen.size();
        bestTurn = (float)bestTurn; // (float)currentGen.size();
        bestMemory = (float)bestMemory; // (float)currentGen.size();
        bestDefense = (float)bestDefense; // (float)currentGen.size();
        
        bestFitnessLog.get(0)[ generation ] = bestFit;
        bestFitnessLog.get(1)[ generation ] = bestNoTurn;
        bestFitnessLog.get(2)[ generation ] = bestTurn;
        bestFitnessLog.get(3)[ generation ] = bestMemory;
        bestFitnessLog.get(4)[ generation ] = bestDefense;
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
    
    protected float fitnessSum(){
        
        float tempFit = 0.0f;
        
        for( float datum : populationFitnessLog.get(0) )
            tempFit += datum;
        
        return tempFit;
    }
    
    protected void writeLogToFile( String filename ){
        
        try{            
            FileWriter fw = new FileWriter( new File( filename ) );
            BufferedWriter bw = new BufferedWriter(fw);
            
            for( int i = 0; i < numGenerations; i++ ){
                bw.write( i + "," +
                            Float.toString( populationFitnessLog.get( 0 )[i] ) + "," + 
                            Float.toString( populationFitnessLog.get( 1 )[i] ) + "," +
                            Float.toString( populationFitnessLog.get( 2 )[i] ) + "," +
                            Float.toString( populationFitnessLog.get( 3 )[i] ) + "," +
                            Float.toString( populationFitnessLog.get( 4 )[i] ) );
                bw.newLine();
            }
            
            bw.close();
            fw.close();
        }
        catch( Exception e ){
            e.printStackTrace();
        }
        
    }
}
