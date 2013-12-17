/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package animalforgingevolutionofgoaldirectedcognition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author kovacwil
 */
public class ForgingAnimalReproduction {
    
    Random generator;
    int childrenProduced;
    
    final private float mutation_rate;
    
    public ForgingAnimalReproduction( float mutation_rate ){
        generator = new Random();
        this.mutation_rate = mutation_rate;
    }
    
    public ArrayList< ForgingAnimal > newGeneration( int numIndividuals ){
        
        ArrayList< ForgingAnimal > nextGeneration = new ArrayList<>();
        
        //2130706432 = highest defense
        
        for ( int i = 0; i < numIndividuals; i++ )
            nextGeneration.add( new ForgingAnimal( i, 2130706432, 100 ) );
            
        return nextGeneration;
    }
    
    public ArrayList< ForgingAnimal > nextGeneration( ArrayList< ForgingAnimal > currentGeneration ){
        
        ArrayList< ForgingAnimal > nextGeneration = new ArrayList<>();
        childrenProduced = 0;
        
        float roll_fit = getPopulationFitness( currentGeneration );
        float roll;
        float sum_so_far;
        
        for( int i = 0; i < currentGeneration.size(); i++ ){
        
            roll = generator.nextFloat() * roll_fit;
            sum_so_far = 0.0f;
            
            for( ForgingAnimal fa : currentGeneration )
                if( ( sum_so_far += fa.fitness() ) >= roll ){
                    nextGeneration.add( reproduce( fa ) );
                    childrenProduced++;
                    break;
                }
        }
        
        currentGeneration.clear();
        
        return nextGeneration;
    }
    
    private float getPopulationFitness( ArrayList< ForgingAnimal > currentGeneration ){
        
        float tempFit = 0.0f;
        
        for( ForgingAnimal fa : currentGeneration )
            tempFit += fa.fitness();
        
        //tempFit = tempFit / ( (float) currentGeneration.size() );
            
        return tempFit;
    }
    
    private ForgingAnimal reproduce( ForgingAnimal fa ){
        
        boolean[] genotypeChild =  genotypeToBitlist( fa.getGenotype() );
        
        char[] tempGenotype = new char[ 32 ];
        //System.out.println( "Child: " + childrenProduced + " - " + genotypeChild.length );
        
        tempGenotype[ 0 ] = '0';
        
        for( int i = 1; i < genotypeChild.length; i++ )
            if( generator.nextFloat() <= mutation_rate )
                if( generator.nextInt() % 2 == 1 )
                    tempGenotype[i] = '1';
                else
                    tempGenotype[i] = '0';
            else
                tempGenotype[ i ] = genotypeChild[ i ]? '1' : '0';
        
        return new ForgingAnimal( childrenProduced, Integer.parseInt( 
                new String( tempGenotype ), 2 ), 1000 );
    }
    
    private boolean[] genotypeToBitlist( int genotype ){
       String tempGenotype = Integer.toBinaryString( genotype );
             
       boolean[] tempBitlist = new boolean[32];
       Arrays.fill(tempBitlist, false);
       
       //convert to boolean array and add padding (Java Integer.toBinaryString provides no frontside 0 padding)
       for( int i = 0; i < tempGenotype.length(); i++ ){
           if( tempGenotype.charAt( i ) == '1' )
               tempBitlist[ ( 32 - tempGenotype.length() ) + i ] = true;
       }
       
       return tempBitlist;
    }
}
