/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.logic;
import compressionalgos.io.Io;

/**
 *
 * @author aleksi
 */
public class Logic {
    private String source;
    private String output;
    private Io io;
    
    /**
     *
     */
    public Logic() {
        this.source = "";
        this.output = "";
        this.io = new Io();
    }
    
    /**
     *
     * @param choice
     * @return
     */
    public boolean runAlgo(String choice) {
        switch (choice) {
            case "0": 
                return false;
            case "1": 
                runHuffman();
                return true;
        }
        
        return true;
    }
    
    private void runHuffman() {
        Huffman huffman = new Huffman(io.readBytesFromFile(source));
        io.writeObjectToFile(output, huffman.compress());
    }
    
    /**
     *
     * @param source
     * @return
     */
    public boolean setSource(String source) {
        this.source = source;
        
        return true;
    }
    
    /**
     *
     * @return
     */
    public String getSource() {
        return this.source;
    }
    
    /**
     *
     * @param output
     * @return
     */
    public boolean setOutput(String output) {
        this.output = output;
        
        return true;
    }
    
    /**
     *
     * @return
     */
    public String getOutput() {
        return this.output;
    }
}
