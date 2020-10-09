/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.domain;

/**
 * Priority queue implementation. Takes any set of comparable objects and orders
 * them by priority, lowest first. If two objects have the same priority the
 * last one added gets placed first.
 * @author aleksi
 * @param <E> comparable object type
 */
public class MinQueue<E extends Comparable<E>> {
    private QNode head;
    
    /**
     * Default constructor. Creates an empty MinQueue.
     */
    public MinQueue() {
        this.head = null;
    }
    
    /**
     * Adds the given object to the queue, placing it according to priority.
     * @param object object to be added
     */
    public void put(E object) {
        QNode newNode = new QNode(object);
        // if the queue empty, set the added object as the first one:
        if (this.head == null) {
            this.head = newNode;
            return;
        }
        QNode currentNode = this.head;
        QNode previousNode = null;
        // if object has lower priority than the current head,
        // set object as new head:
        if (currentNode.object.compareTo(object) >= 0) {
            this.head = newNode;
            this.head.next = currentNode;
            return;
        }
        // go through the linked list until object has lower priority than the
        // next object:
        while (currentNode.object.compareTo(object) < 0) {
            if (currentNode.next == null) {
                currentNode.next = newNode;
                return;
            }
            previousNode = currentNode;
            currentNode = currentNode.next;
        }
        previousNode.next = newNode;
        newNode.next = currentNode;
    }
    
    /**
     * Removes and returns the first element from the queue.
     * @return first object
     */
    public E poll() {
        if (this.head == null) {
            return null;
        }
        QNode returnNode = this.head;
        if (this.head.next == null) {
            this.head = null;
        } else {
            this.head = this.head.next;
        }
        return (E)returnNode.object;
    }
    
    /**
     * Checks whether the queue is empty.
     * @return true if empty, false if not
     */
    public boolean isEmpty() {
        return this.head == null;
    }
    
    /**
     * Checks whether the queue has at least two elements.
     * @return true if yes, false if not
     */
    public boolean hasTwo() {
        if (this.head != null && this.head.next != null) {
            return true;
        }
        return false;
    }
}
