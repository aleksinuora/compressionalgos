/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.domain;
import compressionalgos.utility.IntTools;

/**
 *
 * @author aleksi
 */
public class Dictionary {
    private final IntTools intTools = new IntTools();
    BNode root;
    
    public Dictionary() {
        this.root = new BNode(0, 0);
    }
    
    public void add(int value, int code) {
        // Storing 8-bit and below key:code pairs would have no benefit. Instead,
        // the code is for short keys is assumed to be the same as the key.
        if (intTools.getBitCount(value) < 9) {
            return;
        }
        this.root = addNode(root, value, code);
    }
    
    private BNode addNode(BNode node, int value, int code) {
        if (node == null) {
            return new BNode(value, code);
        }
        if (value < node.key) {
            node.left = addNode(node.left, value, code);
        } else if (value > node.key) {
            node.right = addNode(node.right, value, code);
        // Don't add a new node if value already exists in the tree.    
        } else {
            return node;
        }
        return node;
    }
    
    public int getCode(int key) {
        // Storing 8-bit and below key:code pairs would have no benefit. Instead,
        // the code is for short keys is assumed to be the same as the key.
        if (intTools.getBitCount(key) < 9) {
            return key;
        }
        return search(root, key);
    }
    
    private int search(BNode node, int key) {
        if (node == null) {
            return Integer.MIN_VALUE;
        }
        if (node.key == key) {
            return node.code;
        }
        if (node.key > key) {
            return search(node.left, key);
        }
        if (node.key < key) {
            return search(node.right, key);
        }
        return Integer.MIN_VALUE;
    }
}
