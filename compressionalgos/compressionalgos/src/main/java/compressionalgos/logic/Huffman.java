/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.logic;
import compressionalgos.domain.*;
import compressionalgos.io.Io;
import compressionalgos.utility.*;
import java.util.*;
import java.math.BigInteger;


/**
 * Huffman coding implementation
 * @author aleksi
 */
public class Huffman {
    // preliminary implementation
    private byte[] bytes;
    private HashMap<Byte, Integer> freqMap;
    private PriorityQueue<HuffNode> nodeQueue;
    private int bytesize;
    private HuffNode root;
    private BitSet binary;
    private int binIndex;
    
    /**
     * Basic constructor
     * @param bytes byte[]-object
     */
    public Huffman(byte[] bytes) {
        this.bytes = bytes;
        this.bytesize = bytes.length;
        this.freqMap = new HashMap<>();
        this.nodeQueue = new PriorityQueue<>(new HuffNodeComparator());
        this.root = null;
        this.binary = new BitSet();
        this.binIndex = 0;
    }
    
    /**
     * Compresses the byte[] given in constructor into a Huffman Tree.
     * @return Huffman Tree as object
     */
    public Object compress() {
        // Build frequency map for bytes
        buildFreqMap();
        // Build Huffman tree     
        buildTree();
        // Build a code dictionary in Byte:Code format
        buildCodes(root);
        // todo: convert nodes into Object + keyword
        
        return null;
    }
    
    /**
     * Build frequency map of bytes in the byte array
     */    
    private void buildFreqMap() {
        for (byte b: bytes) {
            freqMap.putIfAbsent(b, 0);
            freqMap.put(b, freqMap.get(b) + 1);
        }        
    }
    
    /**
     * Build Huffman tree out of byte array
     */
    private void buildTree() {       
        // Create a leaf node for each byte
        for (byte b: bytes) {
            HuffNode node = new HuffNode();
            node.value = b;
            node.freq = freqMap.get(b);
            node.left = null;
            node.right = null;
            node.parent = null;
            nodeQueue.add(node);
        }
        
        // Create branches and link them to leaves
        while (nodeQueue.size() > 1) {
            HuffNode first = nodeQueue.poll();
            HuffNode second = nodeQueue.poll();
            HuffNode node = new HuffNode();
            node.left = first;
            node.right = second;
            nodeQueue.add(node);
        }
        
        root = nodeQueue.poll();
    }
    
    /**
     * Build binary codes by traversing the Huffman tree recursively
     * @param node root node
     */
    private void buildCodes(HuffNode node) {
        if (node == null) {
            return;
        }
        
        if (node.left == null && node.right == null) {
            // Write 0 to mark leaf node, followed by byte value of node in 8 bits
            binary.set(binIndex++, false);
            String bin = Integer.toBinaryString((node.value & 0xFF) + 256).substring(1);
            for (int i = 0; i < 8; i++) {
                if (bin.charAt(i) == '0') {
                    binary.set(binIndex++, true);
                } else {
                    binary.set(binIndex++, false);
                }
            }
            return;
        }
        
        binary.set(binIndex++, true);
        buildCodes(node.left);
        buildCodes(node.right);
    }
    
    
    /**
     * Decompresses a Huffman tree into the original file
     * @return byte[]
     */
    public byte[] decompress() {
        // decompression goes here
        buildTree();
        return bytes;
    }
    
}
