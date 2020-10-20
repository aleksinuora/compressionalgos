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
public class MathsTest {
    Maths maths;
    
    public MathsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        maths = new Maths();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of pow method, of class Maths.
     */
    @Test
    public void testPow() {
        System.out.println("pow");
        int base = 4;
        int exponent = 3;
        int exp = 64;
        int res = maths.pow(base, exponent);
        assertEquals(exp, res);
    }
    
}
