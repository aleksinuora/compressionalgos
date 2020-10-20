/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.utility;

/**
 * Utility class with mathematical functions.
 * @author aleksi
 */
public class Maths {
    
    /**
     * Raise an int to the power of a positive exponent.
     * @param base value to be raised
     * @param exponent power to be raised to
     * @return 
     */
    public int pow(int base, int exponent) {
        int result = 1;
        for (int i = 0; i < exponent; i++) {
            result *= base;
        }
        return result;
    }
}
