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
import java.util.*;
import static org.junit.Assert.*;

/**
 *
 * @author aleksi
 */
public class BitStringTest {
    private static String falseFail = "Should have been false but was true";
    private static String trueFail = "Should have been true but was false";
    
    public BitStringTest() {
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
     * Test of add method, of class BitString.
     */
    @Test
    public void testAdd_boolean() {
        System.out.println("add");
        BitString instance = new BitString();
        instance.add(false);
        instance.add(true);
        assertFalse("First bit: " + falseFail, instance.getFirstBit());
        assertTrue("Last bit: " + trueFail, instance.getLastBit());
        assertTrue("Should have been 2 but was " + instance.getBitCount(), instance.getBitCount()==2);
    }

    /**
     * Test of add method, of class BitString.
     */
    @Test
    public void testAdd_byte() {
        System.out.println("add");
        byte bits = 0b101;
        BitString instance = new BitString();
        instance.add(bits);
        System.out.println(Arrays.toString(instance.getArray(false)));
        assertTrue("First bit: " + trueFail, instance.getFirstBit());
        assertFalse("Second bit: " + falseFail, instance.getBit(1));
        assertTrue("Last bit: " + trueFail, instance.getLastBit());
    }

    /**
     * Test of getFirstBit method, of class BitString.
     */
    @Test
    public void testGetFirstBit() {
        System.out.println("getFirstBit");
        BitString instance = new BitString();
        boolean expResult = false;
        boolean result = instance.getFirstBit();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLastBit method, of class BitString.
     */
    @Test
    public void testGetLastBit() {
        System.out.println("getLastBit");
        BitString instance = new BitString();
        instance.add(true);
        instance.add(true);
        assertTrue(trueFail + ". Array: " 
                + Arrays.toString(instance.getArray(false)) 
                + ", bit count: " + instance.getBitCount(),
                instance.getLastBit());
        instance.add(false);
        assertFalse(falseFail, instance.getLastBit());
    }

    /**
     * Test of getBit method, of class BitString.
     */
    @Test
    public void testGetBit() {
        System.out.println("getBit");
        BitString instance = new BitString();
        for (int i = 0; i < 99; i++) {
            instance.add(i % 2 == 0);
        }
        int correctBit = 0;
        for (int i = 0; i < 99; i++) {
            if (i % 2 == 0 && instance.getBit(i)) {
                correctBit++;
            }
            if (i % 2 == 1 && !instance.getBit(i)) {
                correctBit++;
            }
            if (i < 99) System.out.println("test " + i + " " 
                    + instance.getBit(i));
        }
        assertEquals("Wrong bits: only " + correctBit 
                + " out of 99 were correct",
                99, correctBit);
    }

    /**
     * Test of getArray method, of class BitString.
     */
    @Test
    public void testGetArray() {
        System.out.println("getArray");
        boolean pad = false;
        BitString instance = new BitString();
        byte[] expResult = null;
        byte[] result = instance.getArray(pad);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of makeByte method, of class BitString.
     */
    @Test
    public void testMakeByte() {
        System.out.println("makeByte");
        long start = 0L;
        BitString instance = new BitString();
        byte expResult = 0;
        byte result = instance.makeByte(start);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clear method, of class BitString.
     */
    @Test
    public void testClear() {
        System.out.println("clear");
        BitString instance = new BitString();
        instance.clear();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBitCount method, of class BitString.
     */
    @Test
    public void testGetBitCount() {
        System.out.println("getBitCount");
        BitString instance = new BitString();
        long expResult = 0L;
        long result = instance.getBitCount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of length method, of class BitString.
     */
    @Test
    public void testLength() {
        System.out.println("length");
        BitString instance = new BitString();
        int expResult = 0;
        int result = instance.length();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of pad method, of class BitString.
     */
    @Test
    public void testPad() {
        System.out.println("pad");
        BitString instance = new BitString();
        instance.pad();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
