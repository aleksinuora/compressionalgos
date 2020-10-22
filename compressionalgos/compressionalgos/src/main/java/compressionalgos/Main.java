
package compressionalgos;
import java.util.Scanner;
import compressionalgos.ui.Ui;
import compressionalgos.logic.Logic;
/**
 * Main class. Run with argument -full to get full functionality. Otherwise
 * the program will run automatically with the supplied file paths 
 * (simpleTextFile.txt, testFileOut.hf and testFileBack.txt).
 * @author aleksi
 */
public class Main {

    static String testSource = "src/main/java/testing/simpleTextFile.txt";
    static String testDest = "src/main/java/testing/testFileOut.hf";
    /**
     * @param args -f for full functionality
     */
    public static void main(String[] args) {
        // Set to false if you want to skip the Ui and use hardcoded inputs.
        boolean full = false;
        if (args.length > 0) {
            full = args[0].equals("-f");
        }
        Scanner sc = new Scanner(System.in);
        Logic logic = new Logic();
        Ui ui = new Ui(sc, logic, full);
        ui.launch();
    }
    
}
