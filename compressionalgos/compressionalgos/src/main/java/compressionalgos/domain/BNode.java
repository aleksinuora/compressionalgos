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
    public BNode left;
    public BNode right;
    public int key;
    public int code;
    
    public BNode(int key, int code) {
        this.key = key;
        this.code = code;
        this.left = null;
        this.right = null;
    }
}
