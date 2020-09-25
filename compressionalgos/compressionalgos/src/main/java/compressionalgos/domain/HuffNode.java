/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.domain;
import java.util.Comparator;


/**
 *
 * @author aleksi
 */
public class HuffNode implements java.io.Serializable{

    /**
     * Frequency of byte
     */
    public long freq;

    /**
     * Byte value
     */
    public byte value;

    /**
     * Left child of node
     */
    public HuffNode left;

    /**
     * Right child of node
     */
    public HuffNode right;

    /**
     * Parent node
     */
    public HuffNode parent;
    
    /**
     * Constructor, default values
     */
    public HuffNode() {
        this.freq = 0;
        this.value = 127;
        this.left = null;
        this.right = null;
    }
}