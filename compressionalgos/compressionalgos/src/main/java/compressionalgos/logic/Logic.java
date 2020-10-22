/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.logic;
import compressionalgos.io.Io;

/**
 * Software logic class
 * @author aleksi
 */
public class Logic {
    private String source;
    private String output;
    private Io io;
    
    /**
     * Constructor, default values
     */
    public Logic() {
        this.source = "";
        this.output = "";
        this.io = new Io();
    }
    
    /**
     * Run algorithm of choice on selected file
     * @param choice 
     * @return
     */
    public boolean runAlgo(String choice) {
        switch (choice) {
            case "0": 
                return false;
            case "1": 
                HuffCompress();
                return true;
            case "2":
                HuffDecompress();
                return true;
            case "3":
                LZWCompress();
                return true;
            case "4":
                LZWDecompress();
                return true;
        }
        
        return true;
    }
    
    private void HuffCompress() {
        Huffman huffman = new Huffman(io.readBytesFromFile(source), source);
        io.writeByteArrayToFile((output + ".hf"), huffman.compress());
    }
    
    private void HuffDecompress() {
        Huffman huffman = new Huffman(io.readBytesFromFile(source), source);
        huffman.decompress();
        byte[] byteArray = huffman.buildByteArray();
        io.writeByteArrayToFile(output.concat(huffman.getFileType()), byteArray);
    }
    
    private void LZWCompress() {
        LZW lzw = new LZW(io.readBytesFromFile(source), source);
        io.writeByteArrayToFile(output + ".lzw", lzw.compress());
    }
    
    private void LZWDecompress() {
        LZW lzw = new LZW(io.readBytesFromFile(source), source);
        lzw.decompress();
        byte[] byteArray = lzw.getOutPut();
        io.writeByteArrayToFile(output + (lzw.getFileType()), byteArray);
    }
    
    /**
     * Set source file path
     * @param source file path
     */
    public void setSource(String source) {
        this.source = source;
    }
    
    /**
     * Get current source file path
     * @return source path as String
     */
    public String getSource() {
        return this.source;
    }
    
    /**
     * Set output file path and name
     * @param output file path and name
     */
    public void setOutput(String output) {
        this.output = output;
    }
    
    /**
     * Get current output file path and name
     * @return file path and name
     */
    public String getOutput() {
        return this.output;
    }
}
