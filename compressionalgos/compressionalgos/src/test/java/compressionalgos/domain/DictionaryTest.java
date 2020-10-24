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
public class DictionaryTest {
    private static Dictionary largeDictionary1 = new Dictionary();
    private static Dictionary largeDictionary2 = new Dictionary();
    private static int largeSize = 9999;
    
    public DictionaryTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        for (int i = 1; i < largeSize; i++) {
            largeDictionary1.add(i, 255 + i);
            largeDictionary2.add(255 + i, i);
        }
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
     * Test of add method, of class Dictionary.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        Dictionary instance = new Dictionary();
        int exp = 99999;
        for (int i = 0; i < exp; i++) {
            instance.add(i + 256, i);
        }
        int res = 0;
        for (int i = 0; i < exp; i++) {
            if (instance.getValue(i + 256) == i) {
                res++;
            }
        }
        assertEquals(exp, res);
    }

    /**
     * Test of getCode method, of class Dictionary.
     */
    @Test
    public void testGetValue() {
        System.out.println("getCode");
        Dictionary instance = new Dictionary();
        int exp = 10000;
        for (int i = 0; i < exp; i++) {
            instance.add(i * 256, i);
        }
        int res = 0;
        for (int i = 0; i < exp; i++) {
            if (instance.getValue(i * 256) == i) {
                res++;
            }
        }
        assertEquals(exp, res);
        int exp2 = 1;
        int res2 = instance.getValue(exp2);
        assertEquals(exp2, res2);
    }

    /**
     * Test of getString method, of class Dictionary.
     */
    @Test
    public void testGetString() {
        System.out.println("getString");
        Dictionary instance = new Dictionary();
        int key = 0;
        instance.add(256, 1);
        instance.add(257, 0b10000000000000010);
        int res = instance.getString(257).getInt();
        int exp = 0b100000010;
        assertEquals(exp, res);
    }

    /**
     * Test of getKey method, of class Dictionary.
     */
    @Test
    public void testGetKey() {
        System.out.println("getKey");
        int res = largeDictionary2.getKey(256);
        int exp = 511;
        assertEquals(exp, res);
    }
    
    /**
     * Test of getString method, of class Dictionary.
     */
    
    
}
