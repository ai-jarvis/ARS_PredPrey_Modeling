/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package animalforgingevolutionofgoaldirectedcognition;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

/**
 *
 * @author wkentkovac
 */
public class GifOfEvolution {
    
    private ArrayList< BufferedImage > gifSequence;
    final private int delay, loop_delay;
    final private boolean loop;
    
    public GifOfEvolution( int delay, boolean loop, int loop_delay){
        this.delay = delay;
        this.loop = loop;
        this.loop_delay = loop_delay;
        
        gifSequence = new ArrayList<>();
    }
    
    protected void addImage( BufferedImage image ){
        gifSequence.add( image );
    }
    
    protected void writeGIF( String filename ){
        try{
            ImageOutputStream wout = new FileImageOutputStream( new File( filename) );
            GifSequenceWriter gout = new GifSequenceWriter( wout, gifSequence.get(0).getType(), delay, loop );
            
            for(BufferedImage bi : gifSequence )
                gout.writeToSequence( bi );
            
            gout.close();
            wout.close();
        }
        
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
