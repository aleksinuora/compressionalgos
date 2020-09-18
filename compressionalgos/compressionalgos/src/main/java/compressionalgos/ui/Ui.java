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
    // for testing purposes  --
    static String testSource = "src/main/java/testing/simpleTextFile.txt";
    static String testDest = "src/main/java/testing/testObject";
    static boolean testing = true;
    // -- for testing purposes
    
     Scanner scanner;
    private Logic logic;
    
    /**
     *
     * @param scanner
     * @param logic
     */
    public Ui(Scanner scanner, Logic logic) {
        this.scanner = scanner;
        this.logic = logic;
    }
    
    /**
     *
     */
    public void launch() {
        System.out.println("Source path: ");
        String source = scanner.next();
        logic.setSource(source);
        System.out.println("Available algorithms: (1) Huffman compress");
        String algoChoice = scanner.next();
        System.out.println("Output path: ");
        String output = scanner.next();
        logic.setOutput(output);
        // for testing purposes --
        if (testing) {
            logic.setSource(testSource);
            logic.setOutput(testDest);
        }
        // -- for testing purposes
        logic.runAlgo(algoChoice);
    }
}
