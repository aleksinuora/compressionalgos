/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.domain;

import compressionalgos.domain.BitString;
import compressionalgos.utility.*;
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
    private IntTools intTools = new IntTools();
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
     * Try adding lots of 1's
     */
    @Test
    public void testAdd_boolean_3() {
        System.out.println("add 3");
        BitString instance = new BitString();
        for (int i = 0; i < 128; i++) {
            instance.add(true);
        }
        int correct = 0;
        for (int i = 0; i < 128; i++) {
            if (instance.getBit((long)i)) {
                correct++;
            }
        }
        assertEquals("Only " + correct + " out of " + 128 + " bits were correct"
                , 128, correct);
    }

    /**
     * Test of addByte method, of class BitString.
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
     * Test of addByte method, of class BitString
     */
    @Test
    public void testAddByte_byte_2() {
        System.out.println("addByte2");
        byte bits = 0b1;
        BitString instance = new BitString();
        int byteCount = 24;
        for (int i = 0; i < byteCount; i++) {
            instance.addByte(bits);
        }
        int result = 0;
        for (int i = 0; i < (byteCount); i++) {
            if (instance.getBit(i)) {
                result++;
            }
        }
        int expected = byteCount;
        assertEquals(result, expected);
    }
    
    /**
     * Test of addWholeByte method, of class BitString.
     */
    @Test
    public void testAddWholeByte_byte() {
        System.out.println("addWholeByte");
        byte bits = 0b01010101;
        BitString instance = new BitString();
        instance.addWholeByte(bits);
        int res = 0;
        for (int i = 0; i < 8; i++) {
            if (instance.getBit(i) == ((i % 2) == 1)) {
                res++;
            }
        }
        int exp = 8;
        assertEquals(exp, res);
        BitString instance2 = new BitString();
        byte bits2 = -86;
        instance2.addWholeByte(bits2);
        int res2 = (byte)instance2.getInt();
        int exp2 = -86;
        assertEquals(exp2, res2);
    }
    
    /**
     * Test of addInt method, of class BitString.
     */
    @Test
    public void testAddInt_int() {
        System.out.println("addInt");
        int test = 0b11111111000000001111111100000000;
        BitString instance = new BitString(4);
        instance.addInt(test);
        int res = 0;
        for (int i = 0; i < 32; i++) {
            if ((i < 8) || ((i > 15) && (i < 24))) {
                if (instance.getBit(i)) {
                    res++;
                }
            } else {
                if (!instance.getBit(i)) {
                    res++;
                }
            }
        }
        int exp = 32;
        assertEquals(exp, res);
    }
    
    /**
     * Test of addInt method, of class BitString.
     */
    @Test
    public void testAddInt_int2() {
        System.out.println("addInt2");
        int test = 1;
        int res = 0;
        int res2 = 0;
        int exp = 9;
        BitString instance = new BitString();
        instance.addInt(test, exp);
        res = (int)instance.getBitCount();
        assertEquals(exp, res);
        for (int i = 0; i < exp - 1; i++) {
            if (!instance.getBit(i)) {
                res2++;
            }
        }
        if (instance.getBit(exp - 1)) {
            res2++;
        }
        assertEquals(exp, res2);
    }
    
    /**
     * Test of addInt method, of class BitString.
     */
    @Test
    public void testAddInt_3() {
        System.out.println("addInt3");
        BitString instance = new BitString();
        instance.addInt(259);
        int res = instance.getInt();
        int exp = 259;
        assertEquals(exp, res);
    }
    
    /**
     * Test of getInt method, of class BitString.
     */
    @Test
    public void testGetInt() {
        System.out.println("getInt");
        int test = 0b11111111000000001111111100000000;
        BitString instance = new BitString();
        instance.addInt(test);
        int res = instance.getInt();
        int exp = -16711936;
        assertEquals(exp, res);
        BitString instance2 = new BitString();
        instance2.addInt(Integer.MAX_VALUE);
        int res2 = instance2.getInt();
        int exp2 = Integer.MAX_VALUE;
        assertEquals(exp2, res2);
    }
    
    /**
     * Test of addWholeByte method, of class BitString.
     */
    @Test
    public void testAddWholeByte_byte2() {
        System.out.println("addWholeByte_2");
        byte bits = 0b1;
        BitString instance = new BitString();
        instance.addWholeByte((byte)bits);
        int res = 0;
        for (int i = 0; i < 7; i++) {
            if (!instance.getBit(i)) {
                res++;
            }
        }
        if (instance.getLastBit()) {
            res++;
        }
        int exp = 8;
        assertEquals(exp, res);
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
     * Test of getBits method, of class BitString.
     */
    @Test
    public void testGetBits() {
        System.out.println("getBits");
        BitString instance = new BitString();
        int size = 256;
        int start = 8;
        int length = 120;
        for (int i = 0; i < size; i++) {
            instance.add(i % 2 == 1);
        }
        BitString temp = new BitString();
        temp.concatenate(instance.getBits(start, length));
        int res = (int)temp.getBitCount();
        int res2 = 0;
        for (int i = 0; i < res; i++) {
            if (instance.getBit(i + start) == temp.getBit(i)) {
                res2++;
            }
        }
        assertEquals(length, res);
        assertEquals(length, res2);
    }
    
    /**
     * Test of switchBit method, of class BitString.
     */
    @Test
    public void testSwitchBit() {
        System.out.println("switchBit");
        BitString instance = new BitString();
        int target = 21;
        for (int i = 0; i < 25; i++) {
            instance.add(false);
        }
        instance.switchBit(target);
        assertTrue(instance.getBit(target));
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
     * Test of concatenate method, of class BitString.
     */
    @Test
    public void testConcatenate() {
        System.out.println("concatenate");
        BitString instance = new BitString();
        BitString temp = new BitString();
        temp.addInt(Integer.MAX_VALUE);
        instance.concatenate(temp);
        int res = instance.getInt();
        int exp = Integer.MAX_VALUE;
        assertEquals(exp, res);
    }
    /**
     * Test of concatenate method, of class BitString.
     */
    @Test
    public void testConcatenate2() {
        System.out.println("concatenate2");
        BitString instance = new BitString();
        BitString temp = new BitString();
        for (int i = 0; i < 99999; i++) {
            temp.addInt(i);
        }
        instance.concatenate(temp);
        byte[] res = instance.getArray(true);
        byte[] exp = temp.getArray(true);
        byte[] empty = new byte[0];
        assertArrayEquals(exp, res);
        assertFalse(Arrays.equals(empty, res));
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
        assert(instance.getArray(false).length == 2);
    }
    
    /**
     * Test for removeLast method, of class BitString.
     */
    @Test
    public void testRemoveLast() {
        System.out.println("removeLast");
        BitString instance = new BitString();
        for (int i = 0; i < 8; i++) {
            instance.add(true);
        }
        instance.add(false);
        instance.removeLast();
        boolean res = instance.getBit(instance.getBitCount() - 1);
        boolean exp = true;
        assertEquals(exp, res);
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
