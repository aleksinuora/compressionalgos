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
    private PriorityQueue<HuffNode> nodeQueue;
    private HuffNode root;
    private long binIndex;
    private BitString binTree;
    private BitString binCode;
    private String source;
    private String fileType;
    private int byteSize;
    
    /**
     * Basic constructor
     * @param bytes byte[]-object
     * @param source file path of the source file
     */
    public Huffman(byte[] bytes, String source) {
        this.bytes = bytes;
        this.freqMap = new HashMap<>();
        this.dictionary = new HashMap<>();
        this.nodeQueue = new PriorityQueue<>(new HuffNodeComparator());
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
        byte code = 0;
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
            node.freq = first.freq + second.freq;
            node.left = first;
            node.right = second;
            nodeQueue.add(node);
        }
        
        root = nodeQueue.poll();
    }
    
    /**
     * Build code dictionary and binary presentation of Huffman tree.
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
        codeword <<= 1;
        if (node.left == null && node.right == null) {
            // Write 0 to mark leaf node, followed by byte value of node in 8 bits
            binTree.add(false);
            // Convert node's byte value into bit string and append it to binCode
            String bin = Integer.toBinaryString((node.value & 0xFF) + 256).substring(1);
            for (int i = 0; i < 8; i++) {
                if (bin.charAt(i) == '1') {
                    binTree.add(true);
                } else {
                    binTree.add(false);
                }
            }
            // Add the leaf's byte value to dictionary along with current codeword
            dictionary.put(node.value, codeword);
            return;
        }
        // Write 1 if node is internal
        binTree.add(true);
        codeword++;
        if (node.left != null) {
            buildTreeBin(node.left, codeword);
        }
        if (node.right != null) {
            buildTreeBin(node.right, codeword);
        }
    }
    
    /**
     * Build coded binary string of the original file with the Huffman dictionary
     * and append it to the output binary string.
     */
    private void buildCode() {
        for (int i = 0; i < bytes.length; i++) {
            binCode.add(dictionary.get(bytes[i]));
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
        binIndex = 9;
        byte code = 0;
        buildTreeFromBin(root, code);
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
        // Read byte size of original file from bytes [5->9]
        // and convert to int by bit shifting and addition
        byteSize = 0;
        for (int i = 0; i < 4; i++) {
            byteSize <<= 8;
            byteSize += bytes[5 + i];
        }
    }
    
    /**
     * Build dictionary and Huffman tree from binary representation.
     */
    private void buildTreeFromBin(HuffNode node, byte code) {
        // Append code with 0
        code <<= 1;
        // Read node type bit; if node type is 0(leaf), write next 8 bits to 
        // value as byte and add byte to dictionary with current code word as key
        if (!binCode.getBit(binIndex)) {
            node.value = binCode.makeByte(binIndex + 1);
            binIndex += 9;
            dictionary.put(code, node.value);
            return;
        }
        binIndex++;
        // Write 1 if node is internal
        code++;
        node.left = new HuffNode();
        buildTreeFromBin(node.left, code);
        node.right = new HuffNode();
        buildTreeFromBin(node.right, code);
    }
    
    /**
    * Build the original file as a byte array.
    * @return byte[] the original file as byte array
    */
    public byte[] buildByteArray() {
        byte[] originalBytes = new byte[byteSize];
        for (int i = 0; i < byteSize; i++) {
            originalBytes[i] = bytes[9 + i];
        }
        return originalBytes;
    }
    
    /**
     * Getter for lugging around the original file type of a compressed file.
     * @return String original file type
     */
    public String getFileType() {
        return "." + fileType;
    }
}
