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
public class MinQueue<E extends Comparable<E>> {
    private QNode head;
    
    public MinQueue() {
        this.head = null;
    }
    
    public void put(E object) {
        if (this.head == null) {
            this.head = new QNode(object);
            return;
        }
        QNode newNode = new QNode(object);
        QNode currentNode = this.head;
        QNode previousNode = null;
        if (currentNode.object.compareTo(object) >= 0) {
            this.head = newNode;
            this.head.next = currentNode;
            return;
        }
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
    
    public boolean isEmpty() {
        return this.head == null;
    }
    
    public boolean hasTwo() {
        if (this.head != null && this.head.next != null) {
            return true;
        }
        return false;
    }
}
