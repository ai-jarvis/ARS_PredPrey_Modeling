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
public class AnimalForgingEvolutionOfGoalDirectedCognition {

    /**
     * @param args the command line arguments
     */
    public void runIteration( int itNum ) throws Exception {
        
        int numGenerations = 100;
        int numSims = 100;
        int worldSizeX = 100;
        int worldSizeY = 100;
        int numDaysInSimulation = 1000;
        int foodPatchNum = 5;
        int foodPatchSize = 5;
        
        ArrayList< ForgingAnimal >individuals;
        FitnessEvaluation eRep = new FitnessEvaluation( numGenerations );
        
        ForgingAnimalReproduction faRep = new ForgingAnimalReproduction( 0.01f );
        individuals = faRep.newGeneration( numSims );
        
        System.out.println("Generation: 0" );
        
        ForgingWorld world = new ForgingWorld( worldSizeX, worldSizeY, individuals );
            
        world.setup_food( foodPatchNum, foodPatchSize );
        world.runSimulation( numDaysInSimulation );

        world.writeWorld( "./run/iteration_" + itNum + "_generation_0.jpg" );

        eRep.assessGenerationFitness( individuals );

        //System.out.println( eRep.numBestFitness() );
        
        for( int generations = 1; generations < numGenerations; generations++){
            
            System.out.println("Generation: " + generations + "." + itNum);

            individuals = faRep.nextGeneration( individuals );
            
            world = new ForgingWorld( worldSizeX, worldSizeY, individuals );
            
            world.setup_food( foodPatchNum, foodPatchSize );
            world.runSimulation( numDaysInSimulation );
            
            world.writeWorld( "./run/iteration_number_" + itNum + 
                    "_generation_" + generations + ".jpg" );
            
            eRep.assessGenerationFitness( individuals );
            
//            System.out.println( eRep.fitnessSum());
//            System.out.println( eRep.bestDefense());
//            System.out.println( eRep.bestFit());
//            System.out.println( eRep.bestMemory());
//            System.out.println( eRep.bestTurn());
//            System.out.println( eRep.bestNoTurn());
        }
        
        String filename = "generations_" + numGenerations +
                "_iteration_" + itNum + ".csv";
        
        eRep.writeLogToFile( filename );
        
    }
    
    public static void main(String[] args) throws Exception{
        new AnimalForgingEvolutionOfGoalDirectedCognition().runIteration( 0 );
        new AnimalForgingEvolutionOfGoalDirectedCognition().runIteration( 1 );
        new AnimalForgingEvolutionOfGoalDirectedCognition().runIteration( 2 );
        new AnimalForgingEvolutionOfGoalDirectedCognition().runIteration( 3 );
        new AnimalForgingEvolutionOfGoalDirectedCognition().runIteration( 4 );
//        new AnimalForgingEvolutionOfGoalDirectedCognition().runIteration( 5 );
//        new AnimalForgingEvolutionOfGoalDirectedCognition().runIteration( 6 );
//        new AnimalForgingEvolutionOfGoalDirectedCognition().runIteration( 7 );
//        new AnimalForgingEvolutionOfGoalDirectedCognition().runIteration( 8 );
//        new AnimalForgingEvolutionOfGoalDirectedCognition().runIteration( 9 );
    }
}
