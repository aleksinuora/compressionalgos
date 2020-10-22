/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.domain;

/**
 *
 * @author aleksi
 */
public class BNode {

    /**
     * Left child node.
     */
    public BNode left;

    /**
     * Right child node.
     */
    public BNode right;

    /**
     * Next node in a linked list.
     */
    public BNode next;

    /**
     * Height value. Used to balance the binary tree.
     */
    public int height;

    /**
     * Key.
     */
    public int key;

    /**
     * Value.
     */
    public int value;
    
    /**
     * Basic constructor. Parameters are self-explanatory.
     * @param key 
     * @param value
     */
    public BNode(int key, int value) {
        this.key = key;
        this.value = value;
        this.left = null;
        this.right = null;
        this.next = null;
        this.height = 0;
    }

    /**
     * This constructor also links the node to another node, forming a linked 
     * list.
     * @param key
     * @param value
     * @param next next node
     */
    public BNode(int key, int value, BNode next) {
        this.key = key;
        this.value = value;
        this.left = null;
        this.right = null;
        this.next = next;
        this.height = 0;
    }
}
