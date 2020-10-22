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
public class BitString {
    private static final long LONGMASK = 0xFFFFFFFFL;
    private static final short INTMASK = 0xFF;
    private final IntTools intTools = new IntTools();
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
     * @param capacity specifies an initial byte capacity for the BitString
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
     * @param byteArray self-explanatory
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
     * Add a byte to the BitString as individual bits. Leading zeroes are not
     * preserved. Note: this means 0-bytes don't add anything.
     * @param bits a byte to be added
     */
    public void addByte(byte bits) {
        for (int i = intTools.getBitCount(bits & INTMASK) - 1; i >= 0; i--) {
            add(((bits & INTMASK) >> i) % 2 == 1); 
        }
    }
        
    /**
     * Add a byte with leading zeroes to pad up to full 8 bits.
     * @param bits a byte to be added
     */
    public void addWholeByte(byte bits) {
        // Bit shift by i to move individual bits to rightmost position,
        // one by one, starting with the leftmost bit. If the examined bit is 1,
        // the shifted byte will have an odd value, i.e. (bits)mod2 == 1
        // -> add(true), and vice versa for 0 bit/even value.
        for (int i = 7; i >= 0; i--) {
            add(((bits & INTMASK) >> i) % 2 == 1);
        }
    }
    
    /**
     * Add an int value as bits. Leading zeroes are not preserved.
     * @param bits the int value to be added
     */
    public void addInt(int bits) {
        for (int i = intTools.getBitCount(bits) - 1; i >= 0; i--) {
            if ((((bits & LONGMASK) >> i) % 2) == 1) {
                this.add(true);
            } else {
                this.add(false);
            }
        }
    }
    
    /**
     * Add an int value as bits, with enough leading zeroes to fill in the given
     * byte size.
     * @param bits int to be added as bits
     * @param byteSize desired byte size
     */
    public void addInt(int bits, int byteSize) {
        int zeroes = byteSize - intTools.getBitCount(bits);
        for (int i = 0; i < zeroes; i++) {
            add(false);
        }
        addInt(bits);
    }
    
    /**
     * Get an int value representation of this bit string. Only converts the
     * first 32 available bits.
     * @return int value of this bit string
     */
    public int getInt() {
        int value = 0;        
        for (int i = 0; i < intTools.min((int)bitCount, 32); i++) {
            value = value << 1;
            if (getBit(i)) {
                value++;
            }
        }
        return value;
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
        if (arrayIndex == bitCount / 8 && bCMod != 0) {
            shiftedByte = byteArray[arrayIndex] >> (bCMod - 1 - indexMod);
            return (shiftedByte % 2 != 0);
        }
        // if target bit is in a full byte:
        shiftedByte = byteArray[arrayIndex] >> (7 - (indexMod));
        return (shiftedByte % 2 != 0);
    }
    
    /**
     * Get a string of n bits, starting from a given index (inclusive). Returns
     * the string as a new BitString.
     * @param start starting index (inclusive)
     * @param length number of bits to be copied
     * @return new BitString object
     */
    public BitString getBits(long start, long length) {
        BitString bits = new BitString((int)(length / 8));
        for (long i = 0; i < length; i++) {
            bits.add(this.getBit(start + i));
        }
        return bits;
    }
    
    /**
     * Switch the bit at the given index.
     * @param index 
     */
    public void switchBit(long index) {
        if (index >= bitCount) {
            throw new ArrayIndexOutOfBoundsException("Index " + index 
                    + " out of bounds for length " + bitCount);
        }
        byte bitmask = 1;
        int arrayIndex = (int)index / 8;
        int bCMod = (int)bitCount % 8;
        int indexMod = (int)index % 8;
        // If target bit is in the last byte and the last byte isn't full:
        if (arrayIndex == bitCount / 8 && bCMod != 0) {            
            bitmask = (byte)(bitmask << (bCMod - indexMod - 1));
        } else {
            bitmask = (byte)(bitmask << (7 - indexMod));
        }
        byteArray[arrayIndex] = (byte)(byteArray[arrayIndex] ^ bitmask);
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
            newByte = (byte)((newByte & INTMASK) << 1);
            if (getBit(start + i)) {
                newByte++;
            }
        }
        return newByte;
    }
    
    /**
     * Get byte from the given index of the byte array.
     * @param index byte array index
     * @return byte from the given index
     */
    public byte getByte(int index) {
        return makeByte((long)(index * 8));
    }
    
    /**
     * Concatenate this BitString with another one. Adds all of the given
     * BitString to the end of this one.
     * @param nextString BitString to be added
     */
    public void concatenate(BitString nextString) {
        for (long i = 0; i < nextString.bitCount; i++) {
            this.add(nextString.getBit(i));
        } 
    }
    
    /**
     * Reset the whole BitString.
     */
    public void clear() {
        this.capacity = 2;
        this.byteArray = new byte[capacity];
        this.bitCount = 0;
        this.byteIndex = 0;
        this.padBits = 0;
    }
    
    /**
     * Clear the last bit in this BitString.
     */
    public void removeLast() {
        if (bitCount == 0) {
            return;
        }
        byteIndex = (int)((bitCount - 1) / 8);
        byteArray[byteIndex] = (byte)(byteArray[byteIndex] >> 1);
        bitCount = bitCount - 1;
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
    
    public String bytesToString() {
        String string = "";
        for (int i = 0; i < bitCount / 8; i++) {
            string = string + (byteArray[i / 8] + " ");
        }
        return string;
    }
}
