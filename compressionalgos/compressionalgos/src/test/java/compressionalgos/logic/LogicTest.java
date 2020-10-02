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
public class LogicTest {
    
    public LogicTest() {
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
     * Test of runAlgo method, of class Logic.
     */
    @Test
    public void testRunAlgo() {
        System.out.println("runAlgo");
        String choice = "";
        Logic instance = new Logic();
        boolean expResult = false;
        boolean result = instance.runAlgo(choice);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSource method, of class Logic.
     */
    @Test
    public void testSetSource() {
        System.out.println("setSource");
        String source = "";
        Logic instance = new Logic();
        instance.setSource(source);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSource method, of class Logic.
     */
    @Test
    public void testGetSource() {
        System.out.println("getSource");
        Logic instance = new Logic();
        String expResult = "";
        String result = instance.getSource();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setOutput method, of class Logic.
     */
    @Test
    public void testSetOutput() {
        System.out.println("setOutput");
        String output = "";
        Logic instance = new Logic();
        instance.setOutput(output);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getOutput method, of class Logic.
     */
    @Test
    public void testGetOutput() {
        System.out.println("getOutput");
        Logic instance = new Logic();
        String expResult = "";
        String result = instance.getOutput();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
