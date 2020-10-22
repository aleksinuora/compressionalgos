/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.logic;
import compressionalgos.domain.MinQueue;
import compressionalgos.domain.BitString;
import compressionalgos.domain.*;
import compressionalgos.utility.*;


/**
 * Huffman coding implementation
 * @author aleksi
 */
public class Huffman {
    private byte[] bytes;    
    private int[] freqMap;
    private BitString[] dictionary;    
    private MinQueue<HuffNode> nodeQueue;
    private HuffNode root;
    private long binIndex;
    private BitString binTree;
    private BitString binCode;
    private final String source;
    private String fileType;
    private int byteSize;
    private long treeBits;
    
    /**
     * Basic constructor
     * @param bytes byte[]-object
     * @param source file path of the source file
     */
    public Huffman(byte[] bytes, String source) {
        this.bytes = bytes;
        this.freqMap = new int[256];
        this.dictionary = new BitString[256];        
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
        BitString code = new BitString();
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
            freqMap[b & 0xFF]++;
        }        
    }
    
    /**
     * Build Huffman tree out of freqMap.
     */
    private void buildFreqTree() {       
        // Create a leaf node for each unique byte
        for (int i = 0; i < freqMap.length; i++) {
            if (freqMap[i] > 0) {
                HuffNode node = new HuffNode();
                node.value = (byte)(i & 0xFF);
                node.freq = freqMap[i];
                nodeQueue.put(node);
            }
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
     * Byte value will only follow a leaf node, i.e 0, and comes in full 8 bits.
     * @param node root node
     */
    private void buildTreeBin(HuffNode node, BitString codeword) {
        if ((node.left == null && node.right == null)) {
            // Write 0 to mark leaf node, followed by byte value of node in 8 bits
            binTree.add(false);            
            // Convert node's byte value into bit string and append it to binCode
            binTree.addWholeByte((byte)(node.value & 0xFF));
            int index = (node.value & 0xFF);
            dictionary[index] = new BitString();
            dictionary[index].concatenate(codeword);
            return;
        }
        // Add 1 to tree binary if node is internal
        binTree.add(true);
        // Add 1 to codeword to mark left turn
        if (node.left != null) {
            codeword.add(true);
            binTree.add(true);
            buildTreeBin(node.left, codeword);
            codeword.removeLast();
        }    
        if (node.right != null) {
            binTree.add(false);
            codeword.add(false);
            buildTreeBin(node.right, codeword);
            codeword.removeLast();
        }            
    }
    
    /**
     * Build coded binary string of the original file with the Huffman dictionary
     * and append it to the output binary string.
     */
    private void buildCode() {
        for (int i = 0; i < bytes.length; i++) {
            binCode.concatenate(dictionary[(bytes[i] & 0xFF)]);
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
        this.binIndex = 10 * 8;
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
        // Read byte size of original file from bytes [5->9]
        // and convert to int by bit shifting and addition
        byteSize = 0;
        byteSize = ((bytes[5] & 0xFF) << 24) | 
            ((bytes[6] & 0xFF) << 16) | 
            ((bytes[7] & 0xFF) << 8 ) | 
            ((bytes[8] & 0xFF));
        this.treeBits = bytes[9];
    }
    
    /**
     * Build Huffman tree from binary representation.
     */
    private void buildTreeFromBin(HuffNode node) {
        // Read node type bit; if node type is 0(leaf), write next 8 bits to 
        // value as byte
        if (binCode.getBit(binIndex++) == false) {
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
        binIndex += treeBits;        
        for (int i = 0; i < byteSize; i++) {
            BitString path = new BitString();
            originalBytes[i] = buildCodeFromBin(root, path);
        }
        return originalBytes;
    }

    private byte buildCodeFromBin(HuffNode node, BitString path) {
        if (node.left == null && node.right == null) {
            return node.value;
        }
        if (binCode.getBit(binIndex++)) {
            path.add(true);
            return buildCodeFromBin(node.left, path);
        } else {
            path.add(false);
            return buildCodeFromBin(node.right, path);
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
