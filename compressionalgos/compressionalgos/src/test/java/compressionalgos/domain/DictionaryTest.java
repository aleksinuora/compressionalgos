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
    
    public DictionaryTest() {
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
     * Test of add method, of class Dictionary.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        Dictionary instance = new Dictionary();
        int exp = 1224;
        for (int i = 0; i < exp; i++) {
            instance.add(i * 256, i);
        }
        int res = 0;
        for (int i = 0; i < exp; i++) {
            if (instance.getCode(i * 256) == i) {
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
        int exp = 12;
        for (int i = 0; i < exp; i++) {
            instance.add(i * 256, i);
        }
        int res = 0;
        for (int i = 0; i < exp; i++) {
            if (instance.getCode(i * 256) == i) {
                res++;
            }
        }
        assertEquals(exp, res);
        int exp2 = 1;
        int res2 = instance.getCode(exp2);
        assertEquals(exp2, res2);
    }
    
}
