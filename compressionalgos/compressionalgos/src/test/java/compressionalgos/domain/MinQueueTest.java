/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.domain;

import compressionalgos.domain.MinQueue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

/**
 *
 * @author aleksi
 */
public class MinQueueTest {
    ArrayList<String> s1;
    MinQueue q1;
    
    public MinQueueTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        s1 = new ArrayList<>();
        q1 = new MinQueue<String>();
        for (int i = 0; i < 26; i++) {
            s1.add(Integer.toString(i));
        }
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of put method, of class MinQueue.
     */
    @Test
    public void testPut() {
        System.out.println("put");
        PriorityQueue<String> expected = new PriorityQueue<>();
        s1.forEach(s -> q1.put(s));
        s1.forEach(s -> expected.add(s));
        int correctOrder = 0;
        while (!q1.isEmpty()) {
            if (q1.poll() == expected.poll()) {
                correctOrder++;
            }
        }
        assertEquals("Only " + correctOrder + " out of " + s1.size() 
                + " in correct order", s1.size(), correctOrder);
    }
    
    @Test
    public void testPut_2() {
        System.out.println("put2");
        q1.put("1");
        q1.put("1");
        q1.put("2");
        q1.put("1");
        String result = (String)q1.poll();
        String expected = "1";
        assertEquals(expected, result);
    }

    /**
     * Test of poll method, of class MinQueue.
     */
    @Test
    public void testPoll() {
        System.out.println("poll");
        q1.put("1");
        q1.put("2");
        String expected = "1";
        String result = (String)q1.poll();
        assertEquals(expected, result);
    }

    /**
     * Test of isEmpty method, of class MinQueue.
     */
    @Test
    public void testIsEmpty() {
        System.out.println("isEmpty");
        q1.put("1");
        q1.poll();
        boolean result = q1.isEmpty();
        assertTrue(result);
    }

    /**
     * Test of hasTwo method, of class MinQueue.
     */
    @Test
    public void testHasTwo() {
        System.out.println("hasTwo");
        MinQueue instance = new MinQueue();
        instance.put("1");
        instance.put("2");
        boolean result_1 = instance.hasTwo();
        instance.poll();
        boolean result_2 = instance.hasTwo();
        assertTrue(result_1);
        assertFalse(result_2);
    }
    
}
