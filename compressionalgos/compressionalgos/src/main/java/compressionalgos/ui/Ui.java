/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.ui;
import compressionalgos.logic.Logic;
import java.util.*;

/**
 *
 * @author aleksi
 */
public class Ui {
    // for testing purposes
    // feel free to change the file paths --
    static String testSource = "src/main/java/testing/simpleTextFile.txt";
    static String testDest = "src/main/java/testing/testFileOut";
    static String testDecDest = "src/main/java/testing/testFileBack";
    static boolean full = true;
    // -- for testing purposes
    
    Scanner scanner;
    private Logic logic;
    
    /**
     * 
     * @param scanner
     * @param logic
     */
    public Ui(Scanner scanner, Logic logic, boolean full) {
        this.scanner = scanner;
        this.logic = logic;
        this.full = full;
    }
    
    /**
     * Launch user interface
     */
    public void launch() {
        // The scanner class seems to sometimes interact badly with gradle and 
        // NetBeans. 
        if (full) {
            System.out.println("Source path: ");
            String source = scanner.nextLine();
            logic.setSource(source);
            System.out.println("Available algorithms: (1) Huffman compress"
                    + "\n (2) Huffman decompress");
            String algoChoice = scanner.nextLine();
            System.out.println("Output path: ");
            String output = scanner.nextLine();
            logic.setOutput(output);
        }
        if (!full) {
            logic.setSource(testSource);
            logic.setOutput(testDest);
            logic.runAlgo("1");
            logic.setSource(testDest.concat(".hf"));
            logic.setOutput(testDecDest);
            logic.runAlgo("2");
        }
    }
}
