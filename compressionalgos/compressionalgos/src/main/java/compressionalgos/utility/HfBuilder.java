/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.utility;

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
    
    /**
     * Constructor.
     * @param source path of the original file
     * @param tree BitString representation of the Huffman tree
     * @param code BitString representation of the Huffman code
     * @param byteSize Byte size of the original file
     */
    public HfBuilder(String source, BitString tree, BitString code, int byteSize) {
        // reserving 2 bytes to identify as Huffman file, 3 bytes for original
        // filetype and 4 for byteSize
        tree.pad();
        code.pad();
        this.file = new byte[9 + tree.length()+code.length()];
        this.source = source;
        this.byteSize = byteSize;
        this.tree = new byte[tree.length()];
        System.arraycopy(tree.getArray(false), 0, this.tree, 0, tree.length());
        this.code = new byte[code.length()];
        System.arraycopy(code.getArray(false), 0, this.code, 0, code.length());
    }
    
    /**
     * Creates a Huffman file with a 9 byte header, Huffman tree and Huffman
     * coded file and compiles them into a byte[].
     * @return Huffman file as a byte[]
     */
    public byte[] getFile() {
        makeFile();
        return file;
    }
    
    private void makeHeader() {
        // add identifier "Hf" to start of bin array
        file[0] = 0b01001000;
        file[1] = 0b01100110;        
        // convert original file type to byte array and append to bin array
        String[] end = source.split("\\.");
        String fileType = end[end.length-1];
        byte[] fileTypeBytes = fileType.getBytes();
        for (int i = 0; i < fileTypeBytes.length; i++) {
            file[i + 2] = fileTypeBytes[i];
        }        
        // convert byteSize integer to byte array and append to bin array
        byte[] b = new byte[]{
            (byte)(byteSize >>> 24),
            (byte)(byteSize >>> 16),
            (byte)(byteSize >>> 8),
            (byte)(byteSize)
        };
        for (int i = 0; i < 4; i++) {
            file[i + 5] = b[i];
        }
    }
    
    private void makeFile() {
        makeHeader();
        System.arraycopy(tree, 0, file, 9, tree.length);
        System.arraycopy(code, 0, file, 9 + tree.length, code.length);
    }
}
