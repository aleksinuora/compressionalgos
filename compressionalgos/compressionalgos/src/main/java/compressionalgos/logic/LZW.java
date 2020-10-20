/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.logic;
import compressionalgos.domain.*;
import compressionalgos.utility.*;

/**
 *
 * @author aleksi
 */
public class LZW {
    private final StringTools stringTools = new StringTools();
    private final IntTools intTools = new IntTools();
    // Input bytes.
    private byte[] bytes;
    private final String source;
    private BitString output;
    // Current size of bytes.
    private int byteSize;
    private Dictionary dictionary;
    private int byteIndex;
    private final Maths maths = new Maths();
    
    public LZW(byte[] bytes, String source) {
        this.bytes = bytes;
        this.source = source;
        this.output = new BitString(bytes.length);
        this.byteSize = 9;
        this.dictionary = new Dictionary();
        this.byteIndex = 1;        
    }
    
    public byte[] compress() {
        makeHeader();
        buildCode();
        return output.getArray(true);
    }
    
    private void makeHeader() {
        // Add 'LZW' to header.
        output.addWholeByte((byte)0b01001100);
        output.addWholeByte((byte)0b01011010);
        output.addWholeByte((byte)0b01010111);
        // Add 3 bytes to signify original file suffix
        String[] suffix = stringTools.split(source, '.', false);
        byte[] suffixBytes = suffix[suffix.length - 1].getBytes();
        for (int i = 0; i < suffixBytes.length; i++) {
            output.addWholeByte(suffixBytes[i]);
        }
        // Fill up to 3 bytes if the suffix was short
        for (int i = 0 + suffixBytes.length; i < 3; i++) {
            output.addWholeByte((byte)0);
        }
    }
    
    private void buildCode() {
        BitString buffer = new BitString(2);
        buffer.addWholeByte(bytes[0]);
        int dictionaryIndex = 256;
        int nextString;
        int nextCode;
        // Iterate through the source byte array.
        for (int i = 1; i < bytes.length; i++) {
            // Set the next string to [previous string] + [next byte].
            nextString = (buffer.getInt() << 8) | (bytes[i] & 0xFF);
            // Search the dictionary for a code associated with the next string.
            // All 8-bit strings are their own codes.
            nextCode = dictionary.getCode(nextString);
            // If a code already exists for the given string, set the current
            // string to the [code found]+[next byte] pair and continue. If not,
            // add a dictionary entry for the new string and set the string
            // buffer to the [next byte] value. Output previous string from buffer.
            if (nextCode != Integer.MIN_VALUE) {
                buffer.clear();
                buffer.addInt(nextCode, byteSize);
            } else {
                dictionary.add(nextString, dictionaryIndex);
                // Increment code word bit count if necessary.
                if (intTools.getBitCount(dictionaryIndex) 
                        < intTools.getBitCount(dictionaryIndex + 1)) {
                    byteSize++;
                }
                dictionaryIndex++;
                // Mark the next byte. 0 for 8-bit byte (raw value), 
                // 1 for n-bit coded byte.
                if (buffer.getBitCount() == 8) {
                    output.add(false);
                } else {
                    output.add(true);
                }
                output.concatenate(buffer);
                buffer.clear();
                buffer.addWholeByte(bytes[i]);
                
            }
        }
        // Mark the last byte as well. The algorithm works in such a way that
        // one last byte will remain in the buffer after iteration.
        if (buffer.getBitCount() == 8) {
            output.add(false);
        } else if (buffer.getBitCount() > 8) {
            output.add(true);
        }
        output.concatenate(buffer);
    }
    
    public void decompress() {
        byteIndex = 0;
        try {
            readHeader();
        } catch (Exception e) {
            System.out.println(e);
        }
        decode();
    }
    
    private void readHeader() throws Exception {
        byte[] expected = new byte[]{
            (byte)0b01001100,
            (byte)0b01011010,
            (byte)0b01010111
        };
        String filetype = "."; 
        for (int i = 0; i < 3; i++) {
            if (bytes[byteIndex] != expected[byteIndex]) {
                throw new Exception("Not a valid LZW file");
            }
            if (bytes[byteIndex] != 0) {
                filetype = filetype + (char)bytes[byteIndex + 3];
            }
            byteIndex++;
        }
        if (filetype.length() == 1) {
            filetype = "";
        }
    }
    
    private void decode() {
        BitString buffer = new BitString(2);
        BitString input = new BitString(bytes);
        BitString nextCode = new BitString(2);
        int dictionaryIndex = 256;
        long bitIndex = byteIndex * 8;
        while (bitIndex < input.getBitCount()) {
            // If the next bit is 0, the following byte is a raw 8-bit byte.
            if (!input.getBit(bitIndex++)) {
                nextCode.concatenate(input.getBits(bitIndex, 8));
                bitIndex += 8;
            } else {
                nextCode.concatenate(input.getBits(bitIndex, byteSize));
                bitIndex += byteSize;
            }
            buffer.concatenate(nextCode);
            
        }
    }
}
