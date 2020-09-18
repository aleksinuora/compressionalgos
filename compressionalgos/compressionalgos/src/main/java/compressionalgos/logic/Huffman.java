/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.logic;
import compressionalgos.domain.*;
import compressionalgos.io.Io;
import java.util.*;


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
    
    /**
     *
     * @param bytes byte[]-object
     */
    public Huffman(byte[] bytes) {
        this.bytes = bytes;
        this.bytesize = bytes.length;
        this.freqMap = new HashMap<>();
        this.nodeQueue = new PriorityQueue<>();
        this.root = null;
    }
    
    /**
     * Compresses the byte[] given in constructor into a Huffman Tree.
     * @return Huffman Tree as object
     */
    public Object compress() {
        // compression goes here
        buildTree();
        
        // todo: write HuffTree object to file
        return null;
    }
    
    private void buildTree() {
        for (byte b: bytes) {
            freqMap.putIfAbsent(b, 0);
            freqMap.put(b, freqMap.get(b) + 1);
        }
        for (byte b: bytes) {
            HuffNode node = new HuffNode();
            node.value = b;
            node.freq = freqMap.get(b);
            node.left = null;
            node.right = null;
            node.parent = null;
            nodeQueue.add(node);
        }
        while (nodeQueue.size() > 1) {
            HuffNode first = nodeQueue.poll();
            HuffNode second = nodeQueue.poll();
            HuffNode node = new HuffNode();
            node.left = first;
            node.right = second;
            nodeQueue.add(node);
            root = node;
        }
    }
    
    /**
     * Decompresses a Huffman tree into the original file
     * @return byte[]
     */
    public byte[] decompress() {
        // decompression goes here
        return bytes;
    }
}
