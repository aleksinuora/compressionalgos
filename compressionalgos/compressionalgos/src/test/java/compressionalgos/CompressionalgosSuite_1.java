/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author aleksi
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({compressionalgos.ui.UiSuite.class, compressionalgos.utility.UtilitySuite.class, compressionalgos.io.IoSuite.class, compressionalgos.domain.DomainSuite.class, compressionalgos.MainTest.class, compressionalgos.logic.LogicSuite.class})
public class CompressionalgosSuite_1 {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}
