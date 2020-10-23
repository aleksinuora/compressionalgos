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
    static String testSource = "testing/alice29.txt";
    static String testDest = "testing/testFileOut";
    static String testDecDest = "testing/testFileBack";
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
                    + "\n (9) Run performance tests"
                    + "\n (0) To exit program");
            String algoChoice = scanner.nextLine();
            if (algoChoice.equals("9")) {
                System.out.println("\nRunning performance tests");
                logic.runAlgo(algoChoice);
            } else if (algoChoice.equals("0")) {
                System.out.println("Exiting.");
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
            System.out.println("Run with parameter '-f' or manually switch 'full'"
                    + " in the main method to true"
                    + "for full program. If no"
                    + " parameters are "
                    + "present, the program will run some algorithms with "
                    + "preset dummy inputs and no outputs.\n");
            logic.setSource(testSource);
            logic.setOutput(testDest);
            logic.runAlgo("3");
            logic.setSource(testDest + ".lzw");
            logic.setOutput(testDecDest);
            logic.runAlgo("4");
        }
    }
}
