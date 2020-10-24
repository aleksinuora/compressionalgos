/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.utility;

/**
 * Re-implementation of System.arraycopy for byte arrays.
 * @author aleksi 
 */
public class ArrayCopy {
    
    /**
     * Copies values from the given source byte array to the specified 
     * destination array.
     * @param src source array
     * @param srcPos starting index of bytes to be copied from
     * @param dest destination array
     * @param destPos starting index of bytes to be copied to
     * @param length number of bytes to be copied
     */
    public void copyByteArray(byte[] src, int srcPos, byte[] dest, int destPos, int length) {
        for (int i = 0; i < length; i++) {
            dest[destPos + i] = src[srcPos + i];
        }
    }
}
