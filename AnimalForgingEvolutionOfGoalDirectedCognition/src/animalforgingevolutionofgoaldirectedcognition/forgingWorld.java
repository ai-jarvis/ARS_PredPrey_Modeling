package animalforgingevolutionofgoaldirectedcognition;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wkentkovac
 */

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class forgingWorld {
    
    private class posInfo{
        
        private boolean food = false;
        private boolean visited = false;
        final private int x, y;
        private ArrayList< Integer > tickNum = new ArrayList<>();
               
        public posInfo( int x, int y){ this.x = x; this.y = y; }
        
        protected int getX(){ return x; }
        
        protected int getY(){ return y; }
        
        protected void setFood( boolean food ){ this.food = food; }
        
        protected boolean getFood(){ return food; }
        
        protected int setVisited( boolean visited, int tickNumber ){ 
            this.visited = visited; 
            tickNum.add( tickNumber ); 
            
            return tickNum.size();
        }
        
        protected boolean getVisited(){ return visited; }
        
        protected int getNumVisits(){ return tickNum.size(); }
        
        @Override
        public String toString(){
            if( food && !visited )
                return "F";
            
            else if( food && visited )
                return Integer.toString( -tickNum.size() );
            
            else if( visited )
                return Integer.toString( tickNum.size() );
            
            else
                return "O";
        }
    }
    
    final private posInfo[][] world;
    final private int width, height;
    private Map< Integer, posInfo >currentLocation = new HashMap<>();
    private Map< Integer, posInfo >tempCurrentLocation = new HashMap<>();
    private Map< posInfo, Integer >locationOccupied = new HashMap<>();
    private Map< posInfo, Integer >tempLocationOccupied = new HashMap<>();
    private int mostVisited = 0, contests = 0, checksPerformed = 0;
    private ArrayList< animal >community, queue;
    
    public forgingWorld( int width, int height, ArrayList community ){
        
        //
        this.width = width;
        this.height = height;
        this.community = community;
        queue = new ArrayList<>();

        //setup matrix representing 2D world
        world = new posInfo[ width ][ height ];
        
        //add posInfo objects to matrix
        for( int x = 0; x < width; x++)
            for( int y = 0; y < height; y++ )
                world[x][y] = new posInfo( x, y );
    }
    
    public void setup_food( int numPatches, int patchSize ){
        
        //setup random generator and food array
        Random generator = new Random();
        
        //randomly generate food centers
        for( int i = 0; i < numPatches; i++ )
            world[ generator.nextInt( width ) ][ generator.nextInt( height ) ].setFood( true );
            
        //populate area of food sizes
        ArrayList<posInfo> temp = new ArrayList<>();
        for( int i = 0; i < patchSize - 1; i++){
            
            //setup current size of list as we build patchsize
            for( int x = 0; x < width; x++ ){
                for( int y = 0; y < height; y++ ){
                    
                    //
                    if( x - 1 >= 0 )
                        if( world[ ( x - 1 ) % width ][ y ].getFood() ) 
                            temp.add( new posInfo( x, y ) );
                    //
                    if( x + 1 < width )
                        if( world[ ( x + 1 ) % width ][ y ].getFood() )
                            temp.add( new posInfo( x, y ) );
                    //
                    if( y - 1 >= 0 )
                        if( world[ x ][ ( y - 1) % height ].getFood() )
                            temp.add( new posInfo( x,y ) );
                    //
                    if( y + 1 < height )
                        if( world[ x ][ ( y + 1) % height ].getFood() )
                                temp.add( new posInfo( x, y ) );
                }
            }
            
            for( posInfo pT : temp)
                world[ pT.getX() ][ pT.getY() ].setFood(true);
            
            temp.clear();
        }
    }
    
    protected int getWidth(){ return width; }
    
    protected int getHeight(){ return height; }
       
    protected void runSimulation( int tickNumber ){
        
        System.out.println("Starting Simulation of a community of " + community.size() + " forgers");
        
        //Setup animals in initial places
        for( animal individual : community ){
            if( !checkSite( individual, individual.setStartLocation( getWidth(), getHeight() ) ) ){
                System.out.println("setStartLocation Reset");
                queue.add( individual );
            }
        }
        
        while( !queue.isEmpty() ){
            if( checkSite( queue.get(0), queue.get(0).setStartLocation( getWidth() , getHeight() )) ){
                queue.remove(0);
            }

            else
                queue.add( queue.remove(0) );
        }
        
        //finalize movement into new space
        for( animal individual : community ){
            if( individual.isAlive() )
                finalizeSite( individual );
        }
                
        //feed
        for( animal individual : community ){
            if( individual.isAlive() )
                updateSite( individual, tickNumber );
        }
                
        //Run simulation
        for( int i = 0; i < tickNumber; i++ ){
            
            //System.out.println("Day: " + i );
            
//            System.out.println(tempLocationOccupied.size() + ":" + tempCurrentLocation.size() );
//            System.out.println(locationOccupied.size() + ":" + currentLocation.size() );
            
            queue.clear();
            
            for( animal individual : community )
                if( individual.isAlive() ){
                    if( !checkSite( individual, individual.runNextStep( tickNumber ) ) )
                        queue.add( individual );
                }
            
            
            while( !queue.isEmpty() ){
                
                if( checkSite( queue.get(0), queue.get(0).rerunNextStep() ) ){
                    queue.remove(0);
                }
                
                else
                    queue.add( queue.remove(0) );
            }
            
            //finalize movement into new space
            for( animal individual : community ){
                if( individual.isAlive() )
                    finalizeSite( individual );
            }
            
            //feed
            for( animal individual : community ){
                if( individual.isAlive() )
                    updateSite( individual, tickNumber );
            }
        }
    }
    
    private boolean checkSite( animal individual, int[] coordinates ){
        
        checksPerformed++;
        
        int x = coordinates[0];
        int y = coordinates[1];
        int animalID = individual.getID();
        
        //System.out.println( "Next: " + animalID + ":" + x + ":" + y );
        
        //make sure they are inside the grid area
        if( x >= getWidth() || x < 0 || y >= getHeight() || y < 0 )
            return false;

        //check to see if the location is already occupied
        if( tempLocationOccupied.containsKey( world[ x ][ y ] ) ){
            //System.out.println("2+ Individuals in the same place! ");
            
            contests++;
            
            //get the current occupant ID & remove from list
            int occupantID = tempLocationOccupied.remove( world[ x ][ y ] );
            tempCurrentLocation.remove( occupantID );
            
            //run defense check
            if( getDefense( occupantID ) > getDefense( animalID ) ){
                tempLocationOccupied.put( world[ x ][ y ], occupantID );
                tempCurrentLocation.put( occupantID, world[ x ][ y ] );
                return false;
            }
                
            if ( getDefense( occupantID ) == getDefense( animalID ) ){
                queue.add( findIndividual( occupantID ) );
                return false;
            }
            
            if( getDefense( occupantID ) < getDefense( animalID ) )
                queue.add( findIndividual( occupantID ) );
        }
        
        //
        tempCurrentLocation.put( animalID, world[ x ][ y ] );
        tempLocationOccupied.put( world[ x ][ y ], animalID );
        
        return true;
    }
    
    private void finalizeSite( animal individual ){
        
//        System.out.println("------------------------------------");
//        System.out.println("Num Individuals: " + community.size() );
//        System.out.println("Num Locations: " + currentLocation.size() );
//        System.out.println("Num Occupanies: " + locationOccupied.size() );
//        System.out.println("Num Temp Locations: " + tempCurrentLocation.size() );
//        System.out.println("Num Temp Occupanies: " + tempLocationOccupied.size() );
        
        //set animal current location        
        //set location occupany and remove occupany from pervious location
//        if( tempCurrentLocation.containsKey( individual.getID() ) ){
            locationOccupied.remove( currentLocation.remove( individual.getID() ) );

            currentLocation.put( individual.getID(), tempCurrentLocation.get(individual.getID() ) );
            locationOccupied.put( tempCurrentLocation.get(individual.getID()), individual.getID() );

            tempLocationOccupied.remove( tempCurrentLocation.remove( individual.getID() ) );
//        }
//        
//        else{
//            if( !locationOccupied.containsKey( individual.getID() ) )
//                System.out.println( individual.getID() );
//        }
            
    }
    
//    private void clearTemp(){
//        
////        System.out.println(tempLocationOccupied.size() + ":" + tempCurrentLocation.size() );
////        System.out.println(locationOccupied.size() + ":" + currentLocation.size() );
//
//        //Clear the temps
//        tempLocationOccupied.clear();
//        tempCurrentLocation.clear();
//    }
    
    private void updateSite( animal individual, int tickNumber ){
        
        //set the visited flags
        setMostVisited( currentLocation.get(individual.getID() ).setVisited( true, tickNumber ) );
        
        //dool out food stores
        if( currentLocation.get( individual.getID() ).getFood() )
            individual.giveFood();
    }
    
    private int getDefense( int animalID ){
        return findIndividual( animalID ).defend();
    }
    
    private animal findIndividual( int animalID ){
        
        for( animal individual : community )
            if( individual.getID() == animalID )
                return individual;
        
        return null;
    }
    
    private void setMostVisited( int m_visited ){
        if( m_visited > mostVisited )
            mostVisited = m_visited;
    }
    
    protected void printWorld(){
        
        //Top border
        for( int i = -1; i < width; i++ ){
            System.out.print("_");
        }
        
        System.out.println("_");

        //Print single row 
        for( int i = 0; i < height; i++ ){
            
            System.out.print("|");
            
            //print each spot in single row
            for( int j = 0; j < width; j++ ){
                System.out.print( world[j][i] );
            }
                
            System.out.println("|");
        }
        
        //Bottom border
        for( int i = -1; i < width; i++ ){
            System.out.print("_");
        }
        
        System.out.println("_");
    }
    
    protected BufferedImage getWorldImage(){
        int maximum_x = 1000;
        int maximum_y = maximum_x * ( height / width );

        double scale_x = (double)maximum_x / (double)width;
        double scale_y = (double)maximum_y / (double)height;

        /*System.out.println( maximum_x + ":" + maximum_y);
        System.out.println( scale_x + ":" + scale_y);*/

        BufferedImage b_image = new BufferedImage( width, height, BufferedImage.TYPE_3BYTE_BGR );
        BufferedImage p_image = new BufferedImage( maximum_x, maximum_y, BufferedImage.TYPE_3BYTE_BGR );

        for( int x = 0; x < width; x++)
            for( int y = 0; y < height; y++ )
                b_image.setRGB( x, y, getColor(x,y) );

        AffineTransform resize = new AffineTransform();
        resize.scale( scale_x , scale_y );

        AffineTransformOp scaleOp = new AffineTransformOp(resize, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

        p_image = scaleOp.filter(b_image, p_image);

        System.out.println( "Most Visited: " + mostVisited );
        
        return p_image;
    }
    
    private int getColor( int x, int y ){
        Color temp;
        
        if( world[x][y].getNumVisits() == 0 && !world[x][y].getFood() )
            temp = new Color( 135, 135, 135 );
        
        else if( world[x][y].getNumVisits() > 0 && world[x][y].getFood() )
            temp = new Color( ( 255 * ( world[x][y].getNumVisits() ) ) / mostVisited, ( 255 * ( world[x][y].getFood()? 1 : 0 ) ), 
                    ( ( 255 * ( world[x][y].getNumVisits() ) ) / mostVisited + ( 255 * ( world[x][y].getFood()? 1 : 0 ) ) ) / 4 );

        else
            temp = new Color( ( 255 * ( world[x][y].getNumVisits() ) ) / mostVisited, ( 255 * ( world[x][y].getFood()? 1 : 0 ) ), 0 );
        
        return temp.getRGB();
    }
    
    protected void writeWorld( String filename ){
             
        System.out.println("Checks Performed: " + checksPerformed );
        System.out.println("Contents Performed: " + contests );
        
        try{
            File worldWriter = new File( filename );
            ImageIO.write( getWorldImage(), "jpg", worldWriter );
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
