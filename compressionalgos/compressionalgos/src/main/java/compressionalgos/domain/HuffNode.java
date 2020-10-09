/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.domain;


/**
 * Data structure for representing a Huffman tree
 * @author aleksi
 */
public class HuffNode implements Comparable<HuffNode>{

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
        this.parent = null;
    }
    
    /**
     * Basic comparator. Compares the frequency values of two HuffNode-objects.
     * Wraps Long.compare() into compareTo().
     * @param node HuffNode object that this node is compared against
     * @return int value of the comparison: 0 if equal, +x if this is larger
     * than node, -x if this is smaller
     */
    @Override
    public int compareTo(HuffNode node) {
        return Long.compare(this.freq, node.freq);
    }
}