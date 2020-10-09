/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.utility;

import compressionalgos.domain.BitString;

/**
 * Builds a properly formatted byte array out of a Huffman tree and code
 * @author aleksi
 */
public class HfBuilder {
    private String source;
    private int byteSize;
    private byte[] file;
    private byte[] tree;
    private byte[] code;
    private int treePadding;
    
    /**
     * Constructor.
     * @param source path of the original file
     * @param tree BitString representation of the Huffman tree
     * @param code BitString representation of the Huffman code
     * @param byteSize Byte size of the original file
     */
    public HfBuilder(String source, BitString tree, BitString code, int byteSize) {
        // Reserving bytes [0-1] to identify as Huffman file, bytes [2-4] for original
        // filetype, [5-8] for byteSize and [9] for number of trailing zeroes
        // added to pad Huffman tree binary representation.
        // pad binary representations of the Huffman tree and code to full bytes:
        tree.pad();
        code.pad();
        this.file = new byte[10 + tree.length()+code.length()];
        this.source = source;
        this.byteSize = byteSize;
        this.tree = new byte[tree.length()];
        System.arraycopy(tree.getArray(false), 0, this.tree, 0, tree.length());
        this.treePadding = tree.getPadBits();
        this.code = new byte[code.length()];
        System.arraycopy(code.getArray(false), 0, this.code, 0, code.length());
    }
    
    /**
     * Creates a Huffman file with a 13 byte header, Huffman tree and Huffman
     * coded file and compiles them into a byte[].
     * @return Huffman file as a byte[]
     */
    public byte[] getFile() {
        makeFile();
        return file;
    }
    
    private void makeFile() {
        makeHeader();
        System.arraycopy(tree, 0, file, 10, tree.length);
        System.arraycopy(code, 0, file, 10 + tree.length, code.length);
    }
    
    private void makeHeader() {
        // add identifier "Hf" to start of bin array:
        file[0] = 0b01001000;
        file[1] = 0b01100110;        
        // convert original file type to byte array and append to bin array:
        String[] end = source.split("\\.");
        String fileType = end[end.length-1];
        byte[] fileTypeBytes = fileType.getBytes();
        System.arraycopy(fileTypeBytes, 0, file, 2, fileTypeBytes.length);
        // convert byteSize integer to byte array and append to bin array:
        byte[] b = new byte[]{
            (byte)(byteSize >>> 24),
            (byte)(byteSize >>> 16),
            (byte)(byteSize >>> 8),
            (byte)byteSize
        };
        // append number of tree binary padding to bin array:
        System.arraycopy(b, 0, file, 5, 4);
        file[9] = (byte)this.treePadding;
    }
}
