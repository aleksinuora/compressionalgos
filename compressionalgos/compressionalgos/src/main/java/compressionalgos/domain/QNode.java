/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.domain;

/**
 * Linked list-type node, used by MinQueue.
 * @author aleksi
 * @param <E> comparable object
 */
public class QNode<E extends Comparable<E>> {

    /**
     * Node value.
     */
    public E object;

    /**
     * Link to the next node in the list.
     */
    public QNode next;
    
    /**
     * Constructs a node out of the given object.
     * @param object
     */
    public QNode (E object) {
        this.object = object;
    }
}
