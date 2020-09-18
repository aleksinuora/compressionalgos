package compressionalgos.io;
import java.io.*;

/**
 *Class for reading and writing objects and bytes from/to files
 * 
 * @author aleksi
 */
public class Io {
    
    /**
     * Read bytes from file
     * @param path to target file
     * @return file content as byte array
     */
    public byte[] readBytesFromFile(String path) {        
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
     * Read Object from file
     * @param path to target file
     * @return file content as Object
     */
    public Object readObjectFromFile(String path) {
        try {
            FileInputStream fileIn = new FileInputStream(path);
            BufferedInputStream buffIn = new BufferedInputStream(fileIn);
            ObjectInputStream objIn = new ObjectInputStream(buffIn);
            Object obj = objIn.readObject();
            objIn.close();
            return obj;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Write a byte array to file
     * @param path to target file
     * @param bytes Object to be written
     * @return true if successful, false if not
     */
    public boolean writeByteArrayToFile(String path, byte[] bytes) {
        try {
            File file = new File(path);
            FileOutputStream fileOut = new FileOutputStream(file);
            BufferedOutputStream buffOut = new BufferedOutputStream(fileOut);
            buffOut.write(bytes);
            buffOut.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
        
    /**
     * Write an Object to file
     * @param path to destination file
     * @param object to write
     * @return true if successful
     */
    public boolean writeObjectToFile(String path, Object object) {
        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            BufferedOutputStream buffOut = new BufferedOutputStream(fileOut);
            ObjectOutputStream objOut = new ObjectOutputStream(buffOut);
            objOut.writeObject(object);
            objOut.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
