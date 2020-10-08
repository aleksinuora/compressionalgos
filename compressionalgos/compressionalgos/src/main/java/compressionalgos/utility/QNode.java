/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.utility;

/**
 *
 * @author aleksi
 */
public class QNode<E extends Comparable<E>> {
    public E object;
    public QNode next;
    
    public QNode (E object) {
        this.object = object;
    }
}
