/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package animalforgingevolutionofgoaldirectedcognition;

/**
 *
 * @author wkentkovac
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class animal{
    
    private class path{
        
        final private int x, y, time;
        private boolean food = false;
        
        public path( int[] coordinates, int time ){
            this.x = coordinates[0];
            this.y = coordinates[1];
            this.time = time;
        }
        
        protected void setFood(){
            food = true;
        }
        
        protected boolean getFood(){
            return food;
        }
        
        protected int getX(){ return x; }
        
        protected int getY(){ return y; }
        
        protected int getTime(){ return time; }
        
        protected int[] getXY(){ return new int[]{x,y}; }
    }
    
    //Animal characteristics
    final private int id, lifespan, genotype, turn_no_food, turn_food, memory, competition_strength;
    
    //Simulation variables
    private int ticks_since_last_food, total_food;
    private float facing_direction;
    private boolean alive = true;
    private ArrayList< path >breadCrumbs;
    
    //
    private Random generator;
    
    public animal( int id, int genotype, int lifespan ){
        this.id = id;
        this.genotype = genotype;
        this.lifespan = lifespan;
        breadCrumbs = new ArrayList<>();
        
        generator = new Random();
        
        competition_strength = getGene( 0, 7 );
        memory = getGene( 8, 15 );
        turn_food = getGene( 16, 23 );
        turn_no_food = getGene( 24, 31 );
        
//        System.out.println( "Turn_no_food: " + turn_no_food );
//        System.out.println( "Turn_food: " + turn_food );
//        System.out.println( "Memory: " + memory );
//        System.out.println( "Competitive Strength: " + competition_strength );
        
        //starting off the animal is assumed to have not yet eaten
        ticks_since_last_food = memory;
        facing_direction = randomTriangular( 0.0f, 360.0f );
    }
    
    private boolean[] genotypeToBitlist(){
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
    
    private int getGene( int startBit, int stopBit ){
        
        char[] temp = new char[8];
        
        for( int i = startBit; i <= stopBit; i++ )
            temp[ i - startBit ] = genotypeToBitlist()[ i ]? '1' : '0';
        
        return Integer.parseInt( new String( temp ), 2 );
    }
    
    protected int[] setStartLocation( int x, int y){
        
        if( breadCrumbs.isEmpty() ){
            breadCrumbs.add( new path( new int[]{ generator.nextInt( x ), generator.nextInt( y )}, 0 ) );
            return breadCrumbs.get(0).getXY();
        }
        
        else{
            breadCrumbs.remove(0);
            return setStartLocation( x, y );
        }
    }
   
    protected int[] runNextStep( int tickNumber ){
            
        setFacing();
            
        breadCrumbs.add( new path( moveToNextSpace(), tickNumber ) );
        ticks_since_last_food++;
        
        return breadCrumbs.get( breadCrumbs.size() - 1 ).getXY();
    }
    
    protected int[] rerunNextStep(){
        resetFacing();
        ticks_since_last_food--;
        
        /*int[] loss = breadCrumbs.get( breadCrumbs.size() - 1 ).getXY();
        int[] next;
        
        for(int i = 0; i < 4; i++){
            
            next = runNextStep( breadCrumbs.remove( breadCrumbs.size() - 1 ).getTime() );
            
            if( loss[0] != next[0] || loss[1] != next[1] )
                return next;
            else
                resetFacing();
        }*/
        
        return runNextStep( breadCrumbs.remove( breadCrumbs.size() - 1 ).getTime() );
    }
    
    private void setFacing(){
        if( ticks_since_last_food < memory )
            facing_direction = randomTriangular( facing_direction - 2.0f * turn_food, facing_direction + 2.0f * turn_food );
        
        else
            facing_direction = randomTriangular( facing_direction - 2 * turn_no_food, facing_direction + 2 * turn_no_food );   
    }
    
    private void resetFacing(){
        facing_direction += 22.5;
    }
    
    private int[] moveToNextSpace(){
        
        int x = breadCrumbs.get(breadCrumbs.size() - 1).getX();
        int y = breadCrumbs.get(breadCrumbs.size() - 1).getY();
        
        switch( (int)( ( ( facing_direction + 22.5 ) % 360 ) / 45.0 ) ) {
            case 0: x += 1;
                    break;
            
            case 1: x += 1;
                    y += 1;
                    break;
                
            case 2: y += 1;
                    break;
                
            case 3: x += -1;
                    y += 1;
                    break;
            
            case 4: x += -1;
                    break;
            
            case 5: x += -1;
                    y += -1;
                    break;
                
            case 6: y += -1;
                    break;
                
            case 7: x += 1;
                    y += -1;
                    break;
                
            default: break;
        }
        
        int[] temp = new int[2];
        temp[0] = x;
        temp[1] = y;
        
        return temp;
    }
    
    protected int getID(){ return id; }
    
    protected void giveFood(){
        ticks_since_last_food = 0;
        total_food++;
    }
    
    protected int getNoTurn(){ return turn_no_food; }
    
    protected int getTurn(){ return turn_food; }
    
    protected int getMemory(){ return memory; }
    
    protected int getDefense(){ return competition_strength; }
    
    protected int fitness(){
//        System.out.println( "AnimalID: " + id );
//        System.out.println( "\tAlive: " + alive );
//        System.out.println( "\tFitness: " + total_food );
        
        return total_food;
    }
    
    protected int defend(){
        return competition_strength;
    }
    
    protected void killed(){
        alive = false;
    }
    
    protected boolean isAlive(){ return alive; }
    
    private float randomTriangular( float min, float max){
        return (float)( min + ( generator.nextFloat() * ( ( max - min ) + 1 ) ) );
    }
}
