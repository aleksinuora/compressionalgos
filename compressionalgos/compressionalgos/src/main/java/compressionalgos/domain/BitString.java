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
public class BitString {
    private int capacity;
    private byte[] byteArray;
    private long bitCount;
    private int byteIndex;
    private int padBits;
    
    /**
     * Basic constructor. Creates a byte array with length 1.
     */
    public BitString() {
        this.capacity = 1;
        this.byteArray = new byte[1];
        this.bitCount = 0;
        this.byteIndex = 0;
        this.padBits = 0;
    }
    
    /**
     * Constructs a byte array with the specified initial capacity.
     * @param capacity initial capacity for the byte array
     */
    public BitString(int capacity) {
        this.capacity = capacity;
        this.byteArray = new byte[capacity];
        this.byteIndex = 0;
        this.bitCount = 0;
        this.padBits = 0;
    }
    
    /**
     * Constructs a BitString from the given byte array.
     * @param byteArray this becomes the new BitString
     */
    public BitString(byte[] byteArray) {
        this.byteArray = byteArray;
        this.capacity = byteArray.length;
        this.bitCount = byteArray.length * 8;
        this.byteIndex = byteArray.length - 1;
        this.padBits = 0;
    }
    
    /**
     * Adds a single bit to the end of the BitString.
     * @param bit to be added
     */
    public void add(boolean bit) {
        bitCount++;
        checkSize();
        byteIndex = (int)(bitCount-1) / 8;
        int byteNew = byteArray[byteIndex];
        // Bit shift the byte by 1 to effectively append a 0
        byteNew = byteNew << 1;
        // Change the appended bit to 1 if appropriate
        if (bit) {
            byteNew++;
        }
        byteArray[byteIndex] = (byte)byteNew;
    }
    
    /**
     * Add a whole byte of bits to the BitString.
     * @param bits a byte of bits to be added
     */
    public void addByte(byte bits) {
        String bin = Integer.toBinaryString(bits);
        for (int i = 0; i < bin.length(); i++) {
            add(bin.charAt(i) == '1');
        }
    }
    
    /**
     * Look at the first bit in the BitString.
     * @return value of the first bit as boolean
     */
    public boolean getFirstBit() {
        int shiftedByte;
        if (bitCount < 8) {
            shiftedByte = byteArray[0] >> bitCount - 1;
            if (shiftedByte % 2 == 0) {
                return false;
            }
            return true;
        }
        
        shiftedByte = byteArray[0] >> 7;
        if (shiftedByte % 2 == 0) {
            return false;
        }
        return true;
    }
    
    /**
     * Look at the last bit in the BitString.
     * @return value of the last bit as boolean
     */
    public boolean getLastBit() {
        return getBit(bitCount - 1);
    }
    
    /**
     * Look at the bit at the specified index of the BitString.
     * @param index index of the bit, as long 
     * @return
     */
    public boolean getBit(long index) {
        if (index >= bitCount) {
            throw new ArrayIndexOutOfBoundsException("Index " + index 
                    + " out of bounds for length " + bitCount);
        }
        int shiftedByte;
        int arrayIndex = (int)index / 8;
        int bCMod = (int)bitCount % 8;
        int indexMod = (int)index % 8;
        // if target bit is in the last byte and the last byte isn't full:
        if (arrayIndex == index / 8 && bCMod != 0) {
            shiftedByte = byteArray[arrayIndex] >> (bCMod - 1 - indexMod);
            return (shiftedByte % 2 != 0);
        }
        // if target bit is in a full byte:
        shiftedByte = byteArray[arrayIndex] >> (7 - (indexMod));
        return (shiftedByte % 2 != 0);
    }
    
    /**
     * Get the whole BitString as an array.
     * @param pad pads the last byte with trailing zeroes if true
     * @return this BitString as byte[]
     */
    public byte[] getArray(boolean pad) {
        if (pad) {
            pad();
        }
        return byteArray;
    }
    
    /**
     * Extract 8 bits from the BitString into a new byte, starting from a 
     * specified index.
     * @param start the index of the first bit to be extracted
     * @return new byte containing 8 bits
     */
    public byte makeByte(long start) {
        if (start + 7 > bitCount) {
            throw new ArrayIndexOutOfBoundsException("Index " + start + "+7 out of bounds for length " + bitCount);
        }
        byte newByte = 0;
        for (int i = 0; i < 8; i++) {
            newByte = (byte)((newByte & 0xFF) << 1);
            if (getBit(start + i)) {
                newByte++;
            }
        }
        return newByte;
    }
    
    /**
     * Empty the whole BitString.
     */
    public void clear() {
        this.capacity = 8;
        this.byteArray = new byte[1];
        this.bitCount = 0;
        this.byteIndex = 0;
    }
    
    private void checkSize() {
        if (bitCount > (long)byteArray.length * 8) {
            resize();
        }
    }
    
    private void resize() {
        if (capacity == 0) {
            capacity = 8;
        }
        byte[] biggerArray = new byte[capacity*2];
        System.arraycopy(byteArray, 0, biggerArray, 0, capacity);
        byteArray = biggerArray;
        capacity *= 2;
    }
    
    /**
     * Get the number of bits in this BitString.
     * @return number of bits as long
     */
    public long getBitCount() {
        return bitCount;
    }
    
    /**
     * Get the number of bytes in this BitString.
     * @return number of bytes written to, as int
     */
    public int length() {
        int length = (int)bitCount/8;
        return length;
    }
    
    /**
     * Pad the last byte in this BitString with trailing zeroes.
     */
    public void pad() {
        while(bitCount % 8 > 0) {
            add(false);
            this.padBits++;
        }
    }
    
    /**
     * Getter for number of padded bits.
     * @return number of padded bits
     */
    public int getPadBits() {
        return this.padBits;
    }
}
