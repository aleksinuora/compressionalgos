/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.utility;

import java.util.Arrays;
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
public class ArrayCopyTest {
    
    public ArrayCopyTest() {
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
     * Test of copyByteArray method, of class ArrayCopy.
     */
    @Test
    public void testCopyByteArray() {
        System.out.println("copyByteArray");
        byte[] src = new byte[]{
            1,
            2,
            3,
            4,
            5
        };
        int srcPos = 1;
        byte[] dest1 = new byte[10];
        byte[] dest2 = new byte[10];
        int destPos = 5;
        int length = 3;
        ArrayCopy instance = new ArrayCopy();
        instance.copyByteArray(src, srcPos, dest1, destPos, length);
        System.arraycopy(src, srcPos, dest2, destPos, length);
        assertTrue(Arrays.equals(dest1, dest2));
    }
    
}
