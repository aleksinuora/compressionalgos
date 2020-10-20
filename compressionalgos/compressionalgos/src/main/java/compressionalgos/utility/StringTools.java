/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.utility;

/**
 * String manipulation class
 * @author aleksi
 */
public class StringTools {    
    
    /**
     * Splits a string at the given markers. Returns a String array.
     * @param string to be split
     * @param marker char at which to split
     * @param includeMarkers if true: markers are included at the start of the
     * next substring, otherwise discarded
     * @return split string as String array
     */
    public String[] split(String string, char marker, boolean includeMarkers) {
        int markers = 0;
        // Count substrings
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == marker) {
                markers++;
            }
        }
        // Initialize String array with non-nulls
        String[] sArray = new String[markers + 1];
        for (int i = 0; i < sArray.length; i++) {
            sArray[i] = new String("");
        }
        // Fill String array with substrings
        int arrayIndex = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) != marker) {
                sArray[arrayIndex] = sArray[arrayIndex] + string.charAt(i);
            } else {
                arrayIndex++;
                if (includeMarkers) {
                    sArray[arrayIndex] = sArray[arrayIndex] + string.charAt(i);
                }
            }
        }
        return sArray;
    }
}
