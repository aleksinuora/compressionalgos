/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.utility;

import java.util.Comparator;
import compressionalgos.domain.HuffNode;

/**
 * See below
 * @author aleksi
 */
public class HuffNodeComparator implements Comparator<HuffNode> {

    /**
     * Comparator class for HuffNode
     * @param a
     * @param b
     * @return negative value if node a is smaller than b, 0 if equals, positive value otherwise
     */
    public int compare(HuffNode a, HuffNode b) {
        return Long.compare(a.freq, b.freq);
    }
}
