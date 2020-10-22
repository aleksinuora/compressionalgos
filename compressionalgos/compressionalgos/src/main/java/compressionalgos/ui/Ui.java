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
    static String testSource = "src/main/java/testing/Latin-Lipsum.txt";
    static String testDest = "src/main/java/testing/testFileOut";
    static String testDecDest = "src/main/java/testing/testFileBack";
    private boolean full;
    // -- for testing purposes
    
    Scanner scanner;
    private Logic logic;
    
    /**
     * 
     * @param scnr
     * @param bln
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
            System.out.println("Available algorithms: (1) Huffman compress"
                    + "\n (2) Huffman decompress" 
                    + "\n (3) LZW compress" 
                    + "\n (4) LWZ decompress" 
                    + "\n (9) Run performance tests");
            String algoChoice = scanner.nextLine();
            if (algoChoice.equals("9")) {
                System.out.println("\nRunning performance tests");
                logic.runAlgo(algoChoice);
            } else {
                System.out.println("Source path: ");
                String source = scanner.nextLine();
                logic.setSource(source);
                System.out.println("Output path: ");
                String output = scanner.nextLine();
                logic.setOutput(output);
                logic.runAlgo(algoChoice);
            }
        }
        // Set full to false in Main if you want to skip the Ui and use hard coded
        // values for testing etc.
        if (!full) {
            logic.runAlgo("9");
//            logic.setSource(testDest + ".lzw");
//            logic.setOutput(testDecDest);
//            logic.runAlgo("4");
        }
    }
}
