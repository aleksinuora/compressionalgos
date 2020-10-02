/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.logic;

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
public class HuffmanTest {
    
    public HuffmanTest() {
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
     * Test of compress method, of class Huffman.
     */
    @Test
    public void testCompress() {
        System.out.println("compress");
        Huffman instance = null;
        byte[] expResult = null;
        byte[] result = instance.compress();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of decompress method, of class Huffman.
     */
    @Test
    public void testDecompress() {
        System.out.println("decompress");
        Huffman instance = null;
        instance.decompress();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of buildByteArray method, of class Huffman.
     */
    @Test
    public void testBuildByteArray() {
        System.out.println("buildByteArray");
        Huffman instance = null;
        byte[] expResult = null;
        byte[] result = instance.buildByteArray();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFileType method, of class Huffman.
     */
    @Test
    public void testGetFileType() {
        System.out.println("getFileType");
        Huffman instance = null;
        String expResult = "";
        String result = instance.getFileType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
