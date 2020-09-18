/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.io;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.rules.TemporaryFolder;
import org.junit.Rule;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author aleksi
 */
public class ioTest {
    
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    
    public ioTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of ReadBytesFromFile method, of class Io.
     */
    @Test
    public void testReadBytesFromFile() throws IOException {
        System.out.println("ReadBytesFromFile");
        final File tempFile = tempFolder.newFile("tempFile.txt");
        String path = tempFile.getPath();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))){
            bw.write("a");
        } catch (IOException e) {
            e.printStackTrace();
        }        
        Io instance = new Io();
        int expResult = 97;
        byte[] result = instance.readBytesFromFile(path);
        int resValue = result[0];
        assertEquals(expResult, resValue);
    }

    /**
     * Test of WriteObjectToFile method, of class Io.
     */
    @Test
    public void testWriteByteArrayToFile() {
        System.out.println("WriteByteArrayToFile");
        String path = tempFolder.getRoot().getPath() + "/test";
        byte[] bytes = new byte[0];
        Io instance = new Io();
        boolean expResult = true;
        boolean result = instance.writeByteArrayToFile(path, bytes);
        assertEquals(expResult, result);
    }
    
}
