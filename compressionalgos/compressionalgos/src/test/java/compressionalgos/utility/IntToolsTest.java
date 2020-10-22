/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.utility;

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
public class IntToolsTest {
    private static IntTools instance;
    
    public IntToolsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        instance = new IntTools();
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
     * Test of getBitCount method, of class IntTools.
     */
    @Test
    public void testGetBitCount() {
        System.out.println("getBitCount");
        int integer = 0b101010;
        int res = instance.getBitCount(integer);
        int exp = 6;
        assertEquals(exp, res);
        int integer2 = -1;
        int res2 = instance.getBitCount(integer2);
        int exp2 = 32;
        assertEquals(exp2, res2);
        int integer3 = 259;
        int res3 = instance.getBitCount(integer3);
        int exp3 = 9;
        assertEquals(exp3, res3);
    }
    
    /**
     * Test of max method, of class IntTools.
     */
    @Test
    public void testMax() {
        System.out.println("max");
        int a = -2;
        int b = 2;
        int res = instance.max(a, b);
        int exp = 2;
        assertEquals(exp, res);
    }
}
