package compressionalgos.io;
import java.io.*;

/**
 *Class for reading and writing objects from/to files
 * 
 * @author aleksi
 */
public class Io {
    
    /**
     * Read from file
     * @param path path to target file
     * @return target file as byte array
     */
    public byte[] ReadBytesFromFile(String path) {        
        try {
            File file = new File(path);
            FileInputStream fileIn = new FileInputStream(path);
            BufferedInputStream buffIn = new BufferedInputStream(fileIn);
            DataInputStream dataIn = new DataInputStream(buffIn);
            byte[] bytes = new byte[(int)file.length()];
            dataIn.readFully(bytes);
            dataIn.close();
            return bytes;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Write an object to file
     * @param path Path to target directory
     * @param obj Object to be written
     * @return true if successful, false if not
     */
    public boolean WriteObjectToFile(String path, Object obj) {
        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            BufferedOutputStream buffOut = new BufferedOutputStream(fileOut);
            ObjectOutputStream objectOut = new ObjectOutputStream(buffOut);
            objectOut.writeObject(obj);
            objectOut.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
