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
        Io instance = new Io();
        Object expResult = null;
        Object result = instance.ReadBytesFromFile(path);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of WriteObjectToFile method, of class Io.
     */
    @Test
    public void testWriteObjectToFile() {
        System.out.println("WriteObjectToFile");
        String path = tempFolder.getRoot().getPath() + "/test";
        Object obj = null;
        Io instance = new Io();
        boolean expResult = true;
        boolean result = instance.WriteObjectToFile(path, obj);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
