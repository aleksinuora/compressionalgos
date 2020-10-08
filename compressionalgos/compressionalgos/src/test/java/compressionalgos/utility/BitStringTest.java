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
     * Try adding more booleans with add method, of class BitString
     */
    @Test
    public void testAdd_boolean_2() {
        System.out.println("add_2");
        BitString instance = new BitString();
        int bitCount = 128;
        for (int i = 0; i < bitCount; i++) {
            instance.add(i % 3 == 0);
        }
        byte[] bytes = instance.getArray(false);
        StringBuilder bString = new StringBuilder();
        for (int i = 0; i < bitCount / 8; i++) {
            String byteString = Integer.toBinaryString((bytes[i] & 0xFF) + 256).substring(1);
            bString.append(byteString);
        }
        int correct = 0;
        for (int i = 0; i < bitCount; i++) {
            if (i % 3 == 0 && bString.charAt(i) == '1') {
                correct++;
            }
            if (i % 3 != 0 && bString.charAt(i) == '0') {
                correct++;
            }
        }
        assertEquals("Wrong bits: only " + correct + " out of " + bitCount 
                + " were correct.", correct, bitCount);
    }

    /**
     * Test of add method, of class BitString.
     */
    @Test
    public void testAddByte_byte() {
        System.out.println("addByte");
        byte bits = 0b101;
        BitString instance = new BitString();
        instance.addByte(bits);
        byte[] bytes = instance.getArray(false);
        assertTrue("First byte: " + trueFail, bytes[0] == 0b101);
    }

    /**
     * Test of getFirstBit method, of class BitString.
     */
    @Test
    public void testGetFirstBit() {
        System.out.println("getFirstBit");
        BitString instance = new BitString();
        instance.add(true);
        for (int i = 0; i < 24; i++) {
            instance.add(false);
        }
        boolean result = instance.getFirstBit();
        assertTrue(trueFail, result);
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
        boolean result1 = instance.getLastBit();
        instance.add(false);
        boolean result2 = instance.getLastBit();
        assertFalse(falseFail, result2);
        assertTrue(trueFail, result1);
    }

    /**
     * Test of getBit method, of class BitString.
     */
    @Test
    public void testGetBit() {
        System.out.println("getBit");
        BitString instance = new BitString();
        int bitCount = 32;
        for (int i = 0; i < bitCount; i++) {
            instance.add(i % 2 == 0);
        }
        int correctBit = 0;
        for (int i = 0; i < bitCount; i++) {
            if (i % 2 == 0 && instance.getBit(i) == true) {
                correctBit++;
            }
            if (i % 2 == 1 && (instance.getBit(i) == false)) {
                correctBit++;
            }
        }
        assertEquals("Wrong bits: only " + correctBit 
                + " out of " + bitCount + " were correct",
                bitCount, correctBit);
    }

    /**
     * Test of getArray method, of class BitString.
     */
    @Test
    public void testGetArray() {
        System.out.println("getArray");
        BitString instance = new BitString();
        int length = 32;
        byte[] testBytes = new byte[length];
        for (int i = 0; i < length; i++) {
            byte testByte = -1;
            testBytes[i] = testByte;
            instance.addByte(testByte);
        }
        byte[] result = instance.getArray(false);
        int correctByte = 0;
        for(int i = 0; i < length; i++) {
            if (testBytes[i] == result[i]) {
                correctByte++;
            }
        }
        assertEquals("Input bytes don't match with output array: " 
                + (length - correctByte) + " out of " 
                + length + " bytes were wrong", length, correctByte);
    }

    /**
     * Test of makeByte method, of class BitString.
     */
    @Test
    public void testMakeByte() {
        System.out.println("makeByte");
        long start = 8L;
        BitString instance = new BitString();
        for (int i = 0; i < 32; i++) {
            instance.add(i % 2 == 0);
        }
        byte result = instance.makeByte(start);
        byte expResult = -86;
        assertEquals(expResult, result);
    }

    /**
     * Test of clear method, of class BitString.
     */
    @Test
    public void testClear() {
        System.out.println("clear");
        BitString instance = new BitString();
        for (int i = 0; i < 32; i++) {
            instance.add(i % 2 == 0);
        }
        instance.clear();
        assert(instance.length() == 0);
        assert(instance.getBitCount() == 0);
        assert(instance.getArray(false).length == 1);
    }

    /**
     * Test of getBitCount method, of class BitString.
     */
    @Test
    public void testGetBitCount() {
        System.out.println("getBitCount");
        BitString instance = new BitString();
        for (int i = 0; i < 9999; i++) {
            instance.add(true);
        }
        long expResult = 9999;
        long result = instance.getBitCount();
        assertEquals("Expected " + expResult + ", got " + result,expResult, result);
    }

    /**
     * Test of length method, of class BitString.
     */
    @Test
    public void testLength() {
        System.out.println("length");
        BitString instance = new BitString();
        for (int i = 0; i < 99; i++) {
            instance.add(true);
            instance.pad();
        }
        int expResult = 99;
        int result = instance.length();
        assertEquals("Wrong length: ", expResult, result);
    }

    /**
     * Test of pad method, of class BitString.
     */
    @Test
    public void testPad() {
        System.out.println("pad");
        BitString instance = new BitString();
        byte b = 1;
        instance.addByte(b);
        instance.pad();
        instance.add(true);
        instance.add(false);
        instance.pad();
        byte result = instance.getArray(false)[1];
        byte expResult = (byte)-128;
        assertEquals("Expected " + expResult + ", got " + result, expResult, result);
    }
    
}
