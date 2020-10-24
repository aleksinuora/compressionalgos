/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.domain;
import compressionalgos.utility.*;

/**
 * A dictionary class implementing an AVL tree for fast inserts and lookups.
 * @author aleksi
 */
public class Dictionary {
    private static final long LONGMASK = 0xFFFFFFFFL;
    private static final int INTMASK = 0xFF;
    private final IntTools intTools = new IntTools();
    private Dictionary keyset;
    private boolean maintainKeys;
    BNode root;
    
    /**
     * Basic constructor.
     */
    public Dictionary() {
        this.root = new BNode(0, 0);
        maintainKeys = true;
        this.keyset = new Dictionary(false);
    }
    
    private Dictionary(boolean maintainKeys) {
        this.root = new BNode(0, 0);
        this.maintainKeys = maintainKeys;
    }
    
    /**
     * Add a key:value pair to the dictionary. No duplicate keys allowed.
     * @param key keyword
     * @param value value associated with the keyword
     */
    public void add(int key, int value) {
        // Storing 8-bit and below key:value pairs would have no benefit. Instead,
        // the value for short strings is assumed to be the same as the key.
        if ((intTools.getBitCount(key) < 9) && (value > 0) && (key > 0)) {
            return;
        }
        this.root = addNode(root, key, value);
        // Add mirrored pair to keyset. If-clause is so that keyset doesn't try
        // to maintain yet another mirrored pair in it's own add method.
        if (maintainKeys) {
            keyset.add(value, key);
        }
    }
    
    // Recursive function utilized by the add method.
    private BNode addNode(BNode node, int key, int value) {
        if (node == null) {
            // If the value is of form [>8-bit code]+[8-bit byte]:
            // find the node referenced by [>8-bit code] and add a link to it
            // in the new node.
            // Note: in this implementation of LZW, codewords are at most 15 bits.
            // Therefore, a [>8-bit code]-value on it's own won't get mistaken
            // for a [code]+[byte] pair.
//            if (intTools.getBitCount(value) > 16) {
                return new BNode(key, value, searchNode(root, (int)((value & LONGMASK) >> 8)));
//            }
//            return new BNode(key, value);
        }
        // Don't add a new node if the key already exists in the tree.
        if (key == node.key) {
            return node;
        }
        if (key < node.key) {
            node.left = addNode(node.left, key, value);
        } else if (key > node.key) {
            node.right = addNode(node.right, key, value);
        }
        node.height = updateHeight(node);
        int balance = checkBalance(node);
        // Rebalance if necessary.
        if (balance > 1 && (node.left != null && key < node.left.key)) {
            return rightRotate(node); 
        }
        if (balance < -1 && (node.right != null && key > node.right.key)) {
            return leftRotate(node);
        }
        
        if (balance < -1 && (node.right != null && key < node.right.key)) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        if (balance > 1 && (node.left != null && key > node.left.key)) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        return node;
    }
    
    // Method for updating the height parameter of the given node.
    private int updateHeight(BNode node) {
        if (node == null) {
            return 0;
        }
        int a;
        int b;
        if (node.left == null) {
            a = 0;
        } else {
            a = node.left.height;
        }
        if (node.right == null) {
            b = 0;
        } else {
            b = node.right.height;
        }
        return 1 + intTools.max(a, b);
    }
    
    // Check the balance of the given node.
    private int checkBalance(BNode node) {
        return updateHeight(node.left) - updateHeight(node.right);
    }
    
    // Methods for performing rotations, pivoting on the given node.
    private BNode leftRotate(BNode node) {
        BNode newRoot = node.right;
        BNode newRightChild = newRoot.left;
        newRoot.left = node;
        node.right = newRightChild;
        node.height = updateHeight(node);
        newRoot.height = updateHeight(newRoot);
        return newRoot;
    }
    
    private BNode rightRotate(BNode node) {
        BNode newRoot = node.left;
        BNode newLeftChild = newRoot.right;
        newRoot.right = node;
        node.left = newLeftChild;
        node.height = updateHeight(node);
        newRoot.height = updateHeight(newRoot);
        return newRoot;
    }
    
    /**
     * Returns a string of all terminal bytes referenced by the given key.
     * Explanation: values can contain a [byte]+[byte] pair or a [key]+[byte]
     * pair. If the value contains a key, getString recursively looks up the value
     * referenced by that key as well, inserting it into the correct place in
     * the string.
     * @param key self-explanatory
     * @return all terminal bytes found, concatenated into a BitString
     */
    public BitString getString(int key) {
        BitString string = new BitString(2);
        if (intTools.getBitCount(key) < 9) {
            string.addWholeByte((byte)key);
            return string;
        }
        string.concatenate(buildString(new BitString(), searchNode(root, key)));
        if (string.getBitCount() == 0) {
            string.addInt(Integer.MAX_VALUE);
        }
        return string;
    }
    
    private BitString buildString(BitString string, BNode node) {
        if (node == null) {
            return string;
        }
        if (node.value == 0) {
            string.addWholeByte(0);
            return string;
        }
        string.concatenate(buildString(new BitString(2), node.next));
        // The last node in the linked list will have a 0-16 bit value, i.e.
        // a string of two raw bytes. Check if this node is the last. If yes,
        // append return value with the first byte. Otherwise, only write the
        // last 8 bits.
        if (intTools.getBitCount(node.value) < 17) {
            string.addWholeByte((byte)(node.value >> 8));
        }
        string.addWholeByte((byte)(node.value & INTMASK));
        return string;
    }
    
    /**
     * Get the value associated with the given key.
     * @param key self-explanatory
     * @return int value of the key:value pair, or MIN_VALUE if absent
     */
    public int getValue(int key) {
        // Check for small strings.
        if (intTools.getBitCount(key) < 9 && key > 0) {
            return key;
        }
        BNode node = searchNode(root, key);
        if (node == null) {
            return Integer.MAX_VALUE;
        }
        return searchNode(root, key).value;
    }
    
    private BNode searchNode(BNode node, int key) {
        if (node == null) {
            return null;
        }
        if (node.key == key) {
            return node;
        }
        if (node.key > key) {
            return searchNode(node.left, key);
        }
        if (node.key < key) {
            return searchNode(node.right, key);
        }
        return null;
    }
    
    /**
     * Search for a key with a value. The dictionary maintains a second,
     * value:key paired dictionary so the operation is as fast as a getValue.
     * @param value value associated with the wanted key
     * @return key associated with the key as int, or MIN_VALUE if absent
     */
    public int getKey(int value) {
        if (value == 0) {
            return 256;
        }
        return keyset.getValue(value);
    }
}
