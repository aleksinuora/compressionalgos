/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.logic;
import compressionalgos.io.Io;
import compressionalgos.utility.StringTools;

/**
 * Software logic class
 * @author aleksi
 */
public class Logic {
    private final StringTools stringTools = new StringTools();
    private String testPath = "testing/";
    private String source;
    private String output;
    private Io io;
    
    /**
     * Constructor, default values
     */
    public Logic() {
        this.source = "";
        this.output = "";
        this.io = new Io();
    }
    
    /**
     * Run algorithm of choice on selected file
     * @param choice 
     * @return
     */
    public boolean runAlgo(String choice) {
        switch (choice) {
            case "0":
                return false;
            case "1": 
                HuffCompress();
                return true;
            case "2":
                HuffDecompress();
                return true;
            case "3":
                LZWCompress();
                return true;
            case "4":
                LZWDecompress();
                return true;
            case "7":
                HuffmanTest();
                return true;
            case "8":
                LZWTest();
                return true;
            case "9":
                performanceTesting();
                return true;
        }
        
        return true;
    }
    
    private void HuffCompress() {
        Huffman huffman = new Huffman(io.readBytesFromFile(source), source);
        io.writeByteArrayToFile((output + ".hf"), huffman.compress());
    }
    
    private void HuffDecompress() {
        Huffman huffman = new Huffman(io.readBytesFromFile(source), source);
        huffman.decompress();
        byte[] byteArray = huffman.buildByteArray();
        io.writeByteArrayToFile((output + (huffman.getFileType())), byteArray);
    }
    
    private void LZWCompress() {
        LZW lzw = new LZW(io.readBytesFromFile(source), source);
        io.writeByteArrayToFile((output + ".lzw"), lzw.compress());
    }
    
    private void LZWDecompress() {
        LZW lzw = new LZW(io.readBytesFromFile(source), source);
        lzw.decompress();
        byte[] byteArray = lzw.getOutPut();
        io.writeByteArrayToFile((output + (lzw.getFileType())), byteArray);
    }
    
//    #######################
//    # Performance testing #
//    #######################
    private void performanceTesting() {
        long[][] test1;
        long[][] test2;
        test1 = testAlgorithm("LZW");
        test2 = testAlgorithm("Huffman");
        System.out.println("\nLZW compilation");
        printResults(test1);
        System.out.println("\nHuffman compilation");
        printResults(test2);
    }
    
    private void LZWTest() {
        long[][] test;
        test = testAlgorithm("LZW");
        printResults(test);
    }
    
    private void HuffmanTest() {
        long[][] test;
        test = testAlgorithm("Huffman");
        printResults(test);
    }
    
    private long[][] testAlgorithm(String algorithm) {
        // Set to true to delete temporary files after testing.
        boolean del = true;
        long[][] results = new long[8][6];
        long[] averages = new long[6];
        results[0] = testFile("test1.txt", algorithm, del);                
        results[1] = testFile("test2.txt", algorithm, del);        
        results[2] = testFile("test3.txt", algorithm, del);        
        results[3] = testFile("test4.txt", algorithm, del);        
        results[4] = testFile("alice29.txt", algorithm, del);        
        results[5] = testFile("plrabn12.txt", algorithm, del);        
        results[6] = testFile("Huffman_tree_2.png", algorithm, del);        
        results[7] = testFile("imageSampleBW.jpg", algorithm, del);
        
        return results;
    }
    
    private void printResults(long[][] results) {
        long[] averages = new long[6];
        for (int i = 3; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                averages[i] += results[j][i];
            }
            averages[i] /= 6;
        }
        
        averages[2] = 0;
        for (int i = 0; i < 8; i++) {
            averages[2] = averages[2] + results[i][2];
        }
        
        System.out.println(   "####################\n"
                            + "# Compiled results #\n"
                            + "####################\n\n"
                            + averages[2] + " files out of 8 succesfully compressed and decompressed.\n"
                            + "\n*The next values are only for the first six text files.\n"
                            + "The .png and .jpg files were tested mostly to see if they\n"
                            + " would work at all. Given how efficiently compressed the formats\n"
                            + " already are, there's little point in benchmarking with them.*\n\n"
                            + "Average space reduction: " + averages[3] + "%.\n"
                            + "Average speed to compress: " + averages[4] + "ns/byte.\n"
                            + "Average speed to decompress: " + averages[5] + "ns/byte.\n"
                            + "\nEfficiencies by various input size categories"
                            + "\n#############################################\n"
                            + "input size (bytes)       | " + results[0][6] + " | " 
                            + results[1][6] + " |" + results[2][6] + " | " 
                            + results[3][6] + " |\n-----------------------------"
                            + "------------------------------\n"
                            + "comp speed (ns/byte)     | " + results[0][4] 
                            + "    " + results[1][4] + "    " + results[2][4] 
                            + "      " + results[3][4] + "\n" 
                            + "decomp speed (ns/byte)   | " + results[0][5] 
                            + "    " + results[1][5] + "    " + results[2][5] 
                            + "     " + results[3][5] + "\n"
                            + "space reduction (%)      |  " + results[0][3] 
                            + "    " + results[1][3] + "    " + results[2][3] 
                            + "     " + results[3][3]);
    }
    
    /*
    results[i] =
    0: time to compress
    1: time to decompress
    2: verify files, 0 for failure, 1 for success
    3: space reduction
    4: speed/byte to compress
    5: speed/byte to decompress
    6: input size
    */
    private long[] testFile(String sourceFile, String algorithm, boolean deleteTemps) {
        long[] results = new long[7];
        setOutput(testPath + "tempComp");
        setSource(testPath + sourceFile);
        System.out.println("\n>Testing file: [" + sourceFile + "] in folder: [" 
                + testPath + "]");
        System.out.print(algorithm + " compression:\n");
        long t1 = 0;
        long t2 = System.nanoTime();
        if (algorithm == "Huffman") {
            HuffCompress();
        } else if (algorithm == "LZW") {
            LZWCompress();
        }
        t1 = System.nanoTime();
        long deltaT1 = t1 - t2;
        System.out.println("  " + deltaT1 + "ns to compress");
        results[0] = deltaT1;
        System.out.print(algorithm + " decompression:\n");
        String suffix = "";
        if (algorithm == "Huffman") {
            suffix = ".hf";
        } else if (algorithm == "LZW") {
            suffix = ".lzw";
        }
        setSource(testPath + "tempComp" + suffix);
        setOutput(testPath + "tempDec");
        t2 = System.nanoTime();
        if (algorithm == "Huffman") {
            HuffDecompress();
        } else if (algorithm == "LZW") {
            LZWDecompress();
        }
        t1 = System.nanoTime();
        long deltaT2 = t1 - t2;
        System.out.println("  " + deltaT2 + "ns to decompress");
        results[1] = deltaT2;
        String tempDecFile = "tempDec" + stringTools.getSuffix(sourceFile);
        System.out.println("Verifying file, compare: " + tempDecFile + " against " + sourceFile + ": ");
        if (io.filesMatch(testPath + tempDecFile, testPath + sourceFile)) {
            System.out.println("    decompressed file matches original");
            results[2] = 1;
        } else {
            System.out.println("bad match, something went wrong");
            results[2] = 0;
        }
        long size1 = io.getFileSize(testPath + sourceFile);
        long size2 = io.getFileSize(testPath + "tempComp" + suffix);
        long compression = size1 * 100 / size2;
        System.out.println("Original size:\n  " + size1 + " bytes");
        System.out.println("Compressed size:\n  " + size2 + " bytes");
        System.out.println("Space efficiency:\n  " + (size1 - size2) + " bytes (" 
                + compression + "% compression)");
        results[3] = compression;
        long compTime = deltaT1 / size1;
        long decTime = deltaT2 / size2;
        System.out.println("Time efficiency:\n  " + compTime 
                + "ns/byte to compress,\n  " + decTime + "ns/byte to decompress");
        results[4] = compTime;
        results[5] = decTime;
        results[6] = size1;
        if (deleteTemps) {
            io.deleteTempFiles(testPath + "tempComp" + suffix);
            io.deleteTempFiles(testPath + "tempDec" + stringTools.getSuffix(sourceFile));
        }
        return results;
    }
    
//    #######################
//    # Getters and setters #
//    #######################
    /**
     * Set source file path
     * @param source file path
     */
    public void setSource(String source) {
        this.source = source;
    }
    
    /**
     * Get current source file path
     * @return source path as String
     */
    public String getSource() {
        return this.source;
    }
    
    /**
     * Set output file path and name
     * @param output file path and name
     */
    public void setOutput(String output) {
        this.output = output;
    }
    
    /**
     * Get current output file path and name
     * @return file path and name
     */
    public String getOutput() {
        return this.output;
    }
}
