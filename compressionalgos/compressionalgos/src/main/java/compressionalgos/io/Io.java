package compressionalgos.io;
import java.io.*;
import java.util.*;

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
            DataInputStream dataIn = new DataInputStream(fileIn);
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
     * Write a byte array to file
     * @param path to target file
     * @param bytes Object to be written
     * @return true if successful, false if not
     */
    public boolean writeByteArrayToFile(String path, byte[] bytes) {
        try {
            File file = new File(path);
            FileOutputStream fileOut = new FileOutputStream(file);
            DataOutputStream dataOut = new DataOutputStream(fileOut);
            dataOut.write(bytes);
            dataOut.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    /**
     * Utility method for reading a file size.
     * @param path file path
     * @return file size as long, expressed in bytes
     */
    public long getFileSize(String path) {
        try {
            File file = new File(path);
            return file.length();
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }
    
    public boolean filesMatch(String path1, String path2) {
        byte[] file1 = readBytesFromFile(path1);
        byte[] file2 = readBytesFromFile(path2);
        return Arrays.equals(file1, file2);
    }
    
    public void deleteTempFiles(String path) {
        File file = new File(path);
        file.delete();
    }
}
