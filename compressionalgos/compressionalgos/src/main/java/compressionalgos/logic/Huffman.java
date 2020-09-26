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
    private HuffNode root;
    private BitSet binaryTree;
    private int binIndex;
    
    /**
     * Basic constructor
     * @param bytes byte[]-object
     */
    public Huffman(byte[] bytes) {
        this.bytes = bytes;
        this.freqMap = new HashMap<>();
        this.nodeQueue = new PriorityQueue<>(new HuffNodeComparator());
        this.root = null;
        this.binaryTree = new BitSet();
        this.binIndex = 0;
    }
    
    /**
     * Compresses the byte[] given in constructor with Huffman coding.
     * @return Compressed file as object
     */
    public Object compress() {
        // Build frequency map for bytes
        buildFreqMap();
        // Build Huffman tree     
        buildFreqTree();
        // Build a binary representation of the tree
        buildTreeBin(root);
        // Append binaryTree with 1 to signify root node
        binaryTree.set(binIndex);
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
     * Build Huffman tree out of freqMap
     */
    private void buildFreqTree() {       
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
     * Build binary presentation of Huffman tree.
     * Variable binaryTree will contain a binary representation of the
     * Huffman tree. Format: [node type][byte value].
     * Node type: 0 for leaf, 1 for internal.
     * Byte value will only follow a leaf node, i.e 0.
     * @param node root node
     */
    private void buildTreeBin(HuffNode node) {
        if (node == null) {
            return;
        }
        if (node.left == null && node.right == null) {
            // Write 0 to mark leaf node, followed by byte value of node in 8 bits
            binIndex++;
            // Convert node's byte value into bit string and append it to binaryTree
            String bin = Integer.toBinaryString((node.value & 0xFF) + 256).substring(1);
            for (int i = 0; i < 8; i++) {
                binIndex++;
                if (bin.charAt(i) == '1') {
                    binaryTree.set(binIndex);
                }
            }
            return;
        }
        // Write 1 if node is internal
        binaryTree.set(binIndex++);
        if (node.left != null) {
            buildTreeBin(node.left);
        }
        if (node.right != null) {
            buildTreeBin(node.right);
        }
    }
    
    private void buildCode(HuffNode node, byte bits) {
        
    }
    
    /**
     * Decompresses a Huffman tree into the original file
     * @return byte[]
     */
    public byte[] decompress() {
        // decompression goes here
        root = new HuffNode();
        binIndex = binaryTree.length() - 1;
        if (binIndex < 1) {
            return null;
        } 
        buildBinTree(root);
        return bytes;
    }
    
    /**
     * Build Huffman tree from binary representation
     */
    private void buildBinTree(HuffNode node) {
        // read node type bit; if node type is 0(leaf), write next 8 bits to value as byte
        if (!binaryTree.get(binIndex--)) {
            binIndex -= 8;
            node.value = binaryTree.get(binIndex, binIndex + 8).toByteArray()[0];
            return;
        }
        node.left = new HuffNode();
        buildBinTree(node.left);
        node.right = new HuffNode();
        buildBinTree(node.right);
    }
}
