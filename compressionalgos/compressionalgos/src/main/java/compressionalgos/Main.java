
package compressionalgos;
import java.util.*;
import compressionalgos.io.Io;
import compressionalgos.domain.*;
import compressionalgos.ui.Ui;
import compressionalgos.logic.Logic;
import java.io.*;
import java.nio.file.*;
/**
 *
 * @author aleksi
 */
public class Main {

    static String testSource = "src/main/java/testing/simpleTextFile.txt";
    static String testDest = "src/main/java/testing/testObject";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Logic logic = new Logic();
        Ui ui = new Ui(sc, logic);
        ui.launch();
    }
    
}
