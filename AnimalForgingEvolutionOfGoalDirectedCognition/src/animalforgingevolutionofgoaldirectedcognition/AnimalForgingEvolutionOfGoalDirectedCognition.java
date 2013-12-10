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
    public static void main(String[] args) {

        ArrayList< animal >individuals = new ArrayList<>();
        EvolutionReproduction eRep;
        
        for( int generations = 0; generations < 100; generations++){
            
            System.out.println("Generation: " + generations);
            
            for( int i = 0; i < 5; i++ )
                individuals.add( new animal( i, i*i * 547, 1000 ) );

            forgingWorld world = new forgingWorld( 300, 300, individuals );
            world.setup_food(45,5);
            world.runSimulation(1000);
            
            world.writeWorld( "./run/generation_" + generations + ".jpg" );
            
            eRep = new EvolutionReproduction( individuals );
            System.out.println( eRep.numBestFitness() );
            
            individuals.clear();
        }
        
        
//        //for( animal aIndividual : individuals )
//        //    aIndividual.communitySize();
//        
//        for( int i = 0; i < 1000; i++ )
//            for( animal aIndividual : individuals )
//                aIndividual.runNextStep(i);
//        
//        world.writeWorld("test.jpg");
//        
//        for( animal aIndividual : individuals )
//            aIndividual.fitness();
        
        //GifOfEvolution video = new GifOfEvolution( 10, true, 3);
        
//        for( int j = 0; j < 1; j++ ){
//            
//            System.out.println( "Generation: " + j );
//        
//            for( int i = 0; i < 5; i++ ){
//
//                world = new forgingWorld( 150, 150 );
//                world.setup_food(50,5);
//                image = new animal( 1, 1, 1000, world );
//
//                image.run();
//                //video.addImage( world.getWorldImage() );
//                //world.writeWorld( "./run/world_" + i + "_generation_" + j + ".jpg" );
//            }
//        }
        
        //video.writeGIF("test.gif");
        
        //world.printWorld();
//        for( int j = 0; j < 100; j++ ){
//            
//            System.out.println("Generation: " + j);
//            
//            for( int i = 0; i < 1000; i++ ){
//                world = new forgingWorld(150, 150);
//                world.setup_food(25, 2);
//                animal test = new animal( i, j *i, 200, world );
//                test.run();
//            }
//        }
        //world.printWorld();
    }
    
}
