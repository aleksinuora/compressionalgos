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

/**
 *
 * @author aleksi
 */
public class IoTest {
    
    public IoTest() {
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
     * Test of readBytesFromFile method, of class Io.
     */
    @Test
    public void testReadBytesFromFile() {
        System.out.println("readBytesFromFile");
        String path = "";
        Io instance = new Io();
        byte[] expResult = null;
        byte[] result = instance.readBytesFromFile(path);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readObjectFromFile method, of class Io.
     */
    @Test
    public void testReadObjectFromFile() {
        System.out.println("readObjectFromFile");
        String path = "";
        Io instance = new Io();
        Object expResult = null;
        Object result = instance.readObjectFromFile(path);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of writeByteArrayToFile method, of class Io.
     */
    @Test
    public void testWriteByteArrayToFile() {
        System.out.println("writeByteArrayToFile");
        String path = "";
        byte[] bytes = null;
        Io instance = new Io();
        boolean expResult = false;
        boolean result = instance.writeByteArrayToFile(path, bytes);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of writeObjectToFile method, of class Io.
     */
    @Test
    public void testWriteObjectToFile() {
        System.out.println("writeObjectToFile");
        String path = "";
        Object object = null;
        Io instance = new Io();
        boolean expResult = false;
        boolean result = instance.writeObjectToFile(path, object);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
