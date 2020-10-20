/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.utility;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aleksi
 */
public class StringToolsTest {
    private StringTools stringTools;
    
    public StringToolsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        this.stringTools = new StringTools();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of split method, of class StringTools.
     */
    @Test
    public void testSplit() {
        System.out.println("split");
        String string = "as.df.txt";
        char marker = '.';
        boolean includeMarkers = false;
        String[] res = stringTools.split(string, marker, includeMarkers);
        String[] exp = new String[]{
            "as",
            "df",
            "txt"};
        Assert.assertArrayEquals(exp, res);
        String[] res2 = stringTools.split(string, marker, true);
        String[] exp2 = new String[] {
            "as",
            ".df",
            ".txt"};
        Assert.assertArrayEquals(exp2, res2);
        
    }
    
}
