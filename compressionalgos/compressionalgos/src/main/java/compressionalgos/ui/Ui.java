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
    static String testSource = "testing/simpleTextFile.txt";
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
            while(true) {
                System.out.println("\nAvailable commands:\n (1) Huffman compress"
                        + "\n (2) Huffman decompress" 
                        + "\n (3) LZW compress" 
                        + "\n (4) LWZ decompress" 
                        + "\n (7) Huffman test"
                        + "\n (8) LZW test"
                        + "\n (9) Run performance tests"
                        + "\n (0) To exit program"
                        + "\n(Note: before running tests, make sure the "
                        + "'/testing/'-directory with all the test files is "
                        + "properly located in the same directory as "
                        + "compressionalgos.jar.)");
                String algoChoice = scanner.nextLine();
                if (algoChoice.equals("9") || algoChoice.equals("8") || algoChoice.equals("7")) {
                    System.out.println("\nRunning tests");
                    logic.runAlgo(algoChoice);
                } else if (algoChoice.equals("0")) {
                    System.out.println("Exiting.");
                    break;
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
