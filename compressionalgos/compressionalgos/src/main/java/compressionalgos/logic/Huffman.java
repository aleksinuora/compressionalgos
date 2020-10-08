/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.logic;
import compressionalgos.domain.*;
import compressionalgos.utility.*;
import java.util.*;


/**
 * Huffman coding implementation
 * @author aleksi
 */
public class Huffman {
    // preliminary implementation
    private byte[] bytes;
    private HashMap<Byte, Integer> freqMap;
    private HashMap<Byte, Byte> dictionary;
    private MinQueue<HuffNode> nodeQueue;
    private HuffNode root;
    private long binIndex;
    private BitString binTree;
    private BitString binCode;
    private String source;
    private String fileType;
    private int byteSize;
    private long treeBits;
    private long codeBits;
    
    /**
     * Basic constructor
     * @param bytes byte[]-object
     * @param source file path of the source file
     */
    public Huffman(byte[] bytes, String source) {
        this.bytes = bytes;
        this.freqMap = new HashMap<>();
        this.dictionary = new HashMap<>();
        this.nodeQueue = new MinQueue<>();
        this.root = null;
        this.binIndex = 0;
        this.binTree = new BitString(bytes.length/2);
        this.binCode = new BitString(bytes.length / 2);
        this.source = source;
    }
    
    /**
     * Compresses the byte[] given in constructor with Huffman coding.
     * @return Compressed file as object
     */
    public byte[] compress() {
        // Build frequency map for bytes
        buildFreqMap();
        // Build Huffman tree     
        buildFreqTree();
        // Build a binary representation of the tree, append to binCode,
        // build dictionary on the same pass
        byte code = 1;
        buildTreeBin(root, code);
        // Build Huffman code with dictionary, append to binCode
        buildCode();
        // Compile into a Huffman file-formatted byte array
        HfBuilder hf = new HfBuilder(source, binTree, binCode, bytes.length);
        
        return hf.getFile();
    }
    
    /**
     * Build frequency map of bytes in the byte array.
     */    
    private void buildFreqMap() {
        for (byte b: bytes) {
            freqMap.putIfAbsent(b, 0);
            freqMap.put(b, freqMap.get(b) + 1);
        }        
    }
    
    /**
     * Build Huffman tree out of freqMap.
     */
    private void buildFreqTree() {       
        // Create a leaf node for each unique byte
        for (byte b: freqMap.keySet()) {
            HuffNode node = new HuffNode();
            node.value = b;
            node.freq = freqMap.get(b);
            nodeQueue.put(node);
        }        
        // Create branches and link them to leaves
        while (nodeQueue.hasTwo()) {
            HuffNode first = nodeQueue.poll();
            HuffNode second = nodeQueue.poll();
            HuffNode node = new HuffNode();
            node.freq = first.freq + second.freq;
            node.left = second;
            node.right = first;
            nodeQueue.put(node);
        }        
        root = nodeQueue.poll();
    }
    
    /**
     * Build code dictionary and binary representation of Huffman tree.
     * Variable binaryTree will contain a binary representation of the
     * Huffman tree. Format: [node type][byte value].
     * Node type: 0 for leaf, 1 for internal.
     * Byte value will only follow a leaf node, i.e 0.
     * @param node root node
     */
    private void buildTreeBin(HuffNode node, byte codeword) {
        if (node == null) {
            return;
        }        
        if (node.left == null && node.right == null) {
            // Write 0 to mark leaf node, followed by byte value of node in 8 bits
            binTree.add(false);            
            // Convert node's byte value into bit string and append it to binCode
            String bin = Integer.toBinaryString((node.value & 0xFF) + 256).substring(1);
            
            System.out.println(Integer.toBinaryString(codeword));
            System.out.println(bin);
            
            for (int i = 0; i < 8; i++) {
                binTree.add(bin.charAt(i) == '1');
            }
            // Add the leaf's byte value to dictionary along with current codeword
            dictionary.put(node.value, codeword);
            return;
        }
        // Append codeword with 0
        codeword = (byte)((codeword & 0xFF) << 1);
        // Add 1 to tree binary if node is internal
        binTree.add(true);
        if (node.left != null) {
            binTree.add(true);
            codeword++;
            buildTreeBin(node.left, codeword);
        }    
        if (node.right != null) {
            binTree.add(false);
            codeword--;
            buildTreeBin(node.right, codeword);
        }            
    }
    
    /**
     * Build coded binary string of the original file with the Huffman dictionary
     * and append it to the output binary string.
     */
    private void buildCode() {
        for (int i = 0; i < bytes.length; i++) {
            binCode.addByte(dictionary.get(bytes[i]));
        }
    }
    
    /**
     * Decompresses a Huffman tree into the original file.
     */
    public void decompress() {
        // decompression goes here
        binCode = new BitString(bytes);
        root = new HuffNode();
        readHeader();
        binIndex = 13 * 8;
        buildTreeFromBin(root);        
    }
    
    /**
     * Extract header information from the start of a Huffman-compressed file.
     */
    private void readHeader() {
        // Read original filetype from bytes[2->4] and convert to String
        byte[] fileTypeBytes = new byte[]{
            bytes[2],
            bytes[3],
            bytes[4]
        };
        fileType = new String(fileTypeBytes);
        // Read byte size of original file from bytes [6->10]
        // and convert to int by bit shifting and addition
        byteSize = 0;
        for (int i = 0; i < 4; i++) {
            byteSize = byteSize << 8;
            byteSize += bytes[5 + i];
        }
        this.treeBits = bytes[9] * 8 + bytes[10];
        this.codeBits = bytes[11] * 8 + bytes[12];
    }
    
    /**
     * Build Huffman tree from binary representation.
     */
    private void buildTreeFromBin(HuffNode node) {
        // Read node type bit; if node type is 0(leaf), write next 8 bits to 
        // value as byte and add byte to dictionary with current code word as key
        if (!binCode.getBit(binIndex++)) {
            node.value = binCode.makeByte(binIndex);
            binIndex += 8;
            
            return;
        }
        if (binCode.getBit(binIndex++)) {
            node.left = new HuffNode();
            buildTreeFromBin(node.left);
        }
        if (!binCode.getBit(binIndex++)) {
            node.right = new HuffNode();
            buildTreeFromBin(node.right);
        }
    }
    
    /**
    * Build the original file as a byte array.
    * @return byte[] the original file as byte array
    */
    public byte[] buildByteArray() {
        byte[] originalBytes = new byte[byteSize];
        binIndex = 13 * 8 + treeBits;
        for (int i = 0; i < byteSize; i++) {
            // Skip leading 1 (thanks, Java)
            binIndex++;
            originalBytes[i] = buildCodeFromBin(root);
            
            System.out.println(Integer.toBinaryString(originalBytes[i]));
        }
        return originalBytes;
    }

    private byte buildCodeFromBin(HuffNode node) {
        if (node.left == null && node.right == null) {
            return node.value;
        }
        if (binCode.getBit(binIndex++)) {
            return buildCodeFromBin(node.left);
        } else {
            return buildCodeFromBin(node.right);
        }
    }    
    
    /**
     * Getter for lugging around the original file type of a compressed file.
     * @return String original file type
     */
    public String getFileType() {
        return "." + fileType;
    }
}
