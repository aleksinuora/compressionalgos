/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.domain;

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
public class HuffNodeTest {
    
    public HuffNodeTest() {
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
     * Test of compareTo method, of class HuffNode.
     */
    @Test
    public void testCompareTo() {
        System.out.println("compareTo");
        HuffNode node = new HuffNode();
        node.freq = 1;
        HuffNode instance = new HuffNode();
        instance.freq = -1;
        int expResult = -1;
        int result = instance.compareTo(node);
        assertEquals(expResult, result);
    }
    
}
