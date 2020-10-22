/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.utility;

/**
 * Tools for integer manipulation.
 * @author aleksi
 */
public class IntTools {
    
    /**
     * Get the number of significant bits in the given integer.
     * @param integer int value to examine
     * @return number of significant bits as int
     */
    public int getBitCount(int integer) {
        int bits = 0;
        for (int i = 0; i < 32; i++) {
            if (integer == 0) {
                break;
            }
            bits++;
            integer = (int)((integer & (0xFFFFFFFFL)) >> 1);
        }
        return bits;
    }
    
    /**
     * Compare two values and return the larger one.
     * @param a value 1
     * @param b value 2
     * @return the larger value
     */
    public int max(int a, int b) {
        if (a >= b) {
            return a;
        } else {
            return b;
        }
    }
    
    /**
     * Compare two values and return the smaller one.
     * @param a value 1
     * @param b value 2
     * @return the smaller value
     */
    public int min(int a, int b) {
        if (a <= b) {
            return a;
        } else {
            return b;
        }
    }
}
