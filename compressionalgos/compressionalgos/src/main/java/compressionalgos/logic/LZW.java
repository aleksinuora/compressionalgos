/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionalgos.logic;
import compressionalgos.domain.*;
import compressionalgos.utility.*;

/**
 * LZW compression and decompression. Initialize with input byte array.
 * Call compress() to return a compressed byte array.
 * Call decompress() to first read the header and decode a compressed file, then
 * getFileType() to recover original file type and finally getOutPut() for
 * the uncompressed byte array.
 * @author aleksi
 */
public class LZW {
    private final static boolean debug = false;
    private final static long LONGMASK = 0xFFFFFFFFL;
    private final StringTools stringTools = new StringTools();
    private final IntTools intTools = new IntTools();
    // Input bytes.
    private byte[] bytes;
    private final String source;
    private String filetype;
    private BitString output;
    // Current size of bytes.
    private int byteSize;
    private Dictionary dictionary;
    private int padBits;
    
    /**
     *
     * @param bytes
     * @param source
     */
    public LZW(byte[] bytes, String source) {
        this.bytes = bytes;
        this.source = source;
        this.output = new BitString(bytes.length);
        this.byteSize = 9;
        this.dictionary = new Dictionary();       
    }
/*
#######################
# Compression methods #
#######################
*/    

    /**
     * Compress the byte array given at initialization. Return a compressed
     * array.
     * @return compressed byte array
     */    
    public byte[] compress() {
        makeHeader();
        buildCode();
        return buildOutputArray();
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
            nextString = (buffer.getInt() << 8) + (bytes[i] & 0xFF);
            // Search the dictionary for a code associated with the next string.
            // All 8-bit strings are their own codes.
            nextCode = dictionary.getValue(nextString);
            // If a code already exists for the given string, set the current
            // string to the [code found]+[next byte] pair and continue. If not,
            // add a dictionary entry for the new string and set the string
            // buffer to the [next byte] value. Output previous string from buffer.
            if (nextCode != Integer.MAX_VALUE) {
                buffer.clear();
                buffer.addInt(nextCode, byteSize);
                continue;
            } else {
                // Mark the next byte. 0 for 8-bit byte (raw value), 
                // 1 for n-bit coded byte.
                if (buffer.getBitCount() == 8) {
                    output.add(false);
                } else {
                    output.add(true);
                }
                if (dictionaryIndex < 32768) {
                    dictionary.add(nextString, dictionaryIndex++);
                }
                // Increment code word byte size if necessary.
                if (intTools.getBitCount(dictionaryIndex) 
                        < intTools.getBitCount(dictionaryIndex + 1) && byteSize < 15) {
                    // Output n 1's to mark a byte size increase, where n
                    // is the old byteSize. Skip the last byte of the n-bit range
                    // to avoid collisions between dictionary keys and byte size
                    // markers. I.e., the last 9-bit dictionaryIndex is 510,
                    // the next one will be 512 (10 bits) and so on.
                    for (int j = 0; j < byteSize; j++) {
                        output.add(true);
                    }
                    dictionaryIndex++;
                    byteSize++;
                    
                    if (debug) {
                        System.out.println("byte size increased, code was: " + nextCode);
                    }
                    
                }               
                output.concatenate(buffer);
                
                if (debug)
                    {
                    System.out.println("output: " + (buffer.getInt()) 
                            + ", byte size: " + buffer.getBitCount() 
                            + ", dictionary index: " + dictionaryIndex);
                }
                
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
        output.pad();
        padBits = output.getPadBits();
        output.addWholeByte((byte)padBits);
    }
/*
#########################   
# Decompression methods #
#########################
*/
    
    /**
     * Read header, decode array given at initialization and build the output
     * array. Call getOutPut() to get the array.
     */
    public void decompress() {
        try {
            readHeader();
        } catch (Exception e) {
            System.out.println(e);
        }
        decode();
        bytes = buildOutputArray();
    }
    
    private void readHeader() throws Exception {
        // Read the first three bytes for LZW identifier. Throw exception if
        // not found.
        // Read original filetype in the same for-loop.
        byte[] expected = new byte[]{
            (byte)0b01001100,
            (byte)0b01011010,
            (byte)0b01010111
        };
        filetype = "."; 
        for (int i = 0; i < 3; i++) {
            if (bytes[i] != expected[i]) {
                throw new Exception("Not a valid LZW file");
            }
            if (bytes[i] != 0) {
                filetype = filetype + (char)bytes[i + 3];
            }
        }
        if (filetype.length() == 1) {
            filetype = "";
        }
        // Read the number of padded zeros from the last byte.
        padBits = bytes[bytes.length - 1];
    }
    
    /**
     * Get the original file type. Call only after decompress(). 
     * @return original file type in ".[suffix]" format
     */
    public String getFileType() {
        return filetype;
    }
    
    private void decode() {
        BitString buffer = new BitString(2);
        BitString input = new BitString(bytes);
        BitString nextCode = new BitString(2);
        BitString nextString = new BitString(2);
        BitString temp;
        int dictionaryIndex = 256;
        long bitIndex = 6 * 8 + 1;
        buffer.concatenate(input.getBits(bitIndex, 8));
        bitIndex += 8;
        output.concatenate(buffer);
        // The last byte is reserved for header information. The last couple of
        // bits are 0-7 padded zeroes. The last viable data byte is 1 + 8 bits
        // long at the least. 
        while (bitIndex < input.getBitCount() - 16 - padBits) {       
            // Read the type byte type bit.
            // If the next bit is 0, the following byte is a raw 8-bit byte
            // -> read the next 8 bits.
            // If it's a one, the byte is an n-bit code
            // -> read the next [byteSize] bits.
            if (!input.getBit(bitIndex++)) {
                nextCode.concatenate(input.getBits(bitIndex, 8));               
                bitIndex += 8;
            } else {
                nextCode.concatenate(input.getBits(bitIndex, byteSize));
                // 1. Check for byte size increment code ([byteSize] long 
                //  series of 1-bits).
                // 2. If there's a byte size increment, clear nextCode and read
                // the real data byte into it. It will always be a code word
                // after a byte size increment.
                if (increaseByteSize(nextCode)) {
                    bitIndex += byteSize;
                    byteSize++;
                    nextCode.clear();
                    nextCode.concatenate(input.getBits(bitIndex, byteSize));
                }
                bitIndex += byteSize;
            }
            nextString.concatenate(dictionary.getString(nextCode.getInt()));
            if (nextString.getInt() == Integer.MAX_VALUE) {
                // NO ENTRY FOR INPUT EXCEPTION COMES HERE
                // 1. Concatenate the last byte from the latest dictionary entry 
                //  to buffer. The dictionary returns an int value but using 
                //  the addByte method and casting the dictionary value as byte
                //  ensures that only the last 8 bits are added to buffer.
                // 2. Add this new value to the dictionary.
                // 3. Output the string derived from the new value, 
                //  clean up and continue.
                dictionaryIndex--;
                buffer.addWholeByte((byte)dictionary.getValue(dictionaryIndex));                
                dictionary.add(nextCode.getInt(), buffer.getInt());
                dictionaryIndex += 2;
                buffer.clear();
                buffer.concatenate(nextCode);
                nextCode.clear();
                output.concatenate(dictionary.getString(buffer.getInt()));
                
                if (debug) {
                    System.out.println("Exception, buffer: " 
                            + buffer.getInt()); 
                }               
                
                nextString.clear();
                continue;
            }
            buffer.addWholeByte((byte)(nextString.getInt()));
            if (dictionary.getKey(buffer.getInt()) != Integer.MAX_VALUE) {                
                output.concatenate(dictionary.getString(nextCode.getInt()));
                buffer.clear();
                buffer.concatenate(nextCode);
            } else {
                dictionary.add(dictionaryIndex++, buffer.getInt());
                output.concatenate(nextString);
                buffer.clear();
                buffer.concatenate(nextCode);
            }
            
            if (debug) {
                System.out.println("nC: " + nextCode.getInt() + " nS: " + nextString.getInt() 
                        + ", buffer: " + buffer.getInt() + ", byte: " 
                        + (bitIndex / 8) + "." + (bitIndex % 8) 
                        + ", byte size: " + byteSize);
            }
            nextCode.clear();
            nextString.clear();
        }
    }

    private boolean increaseByteSize(BitString code) {
        if (byteSize > 14) {
            return false;
        } 
        for (int i = 0; i < byteSize; i++) {
            if (!code.getBit(i)) {
                return false;
            }
        }
        return true;
    } 
    
    /**
     * Get the decompressed byte array. Call only after decompress().
     * @return decompressed byte array
     */
    public byte[] getOutPut() {
        return bytes;
    }
    
    private byte[] buildOutputArray() {
        byte[] outputBytes = new byte[(int)(output.getBitCount() / 8)];
        byte[] temp = output.getArray(false);
        for (int i = 0; i < outputBytes.length; i++) {
            outputBytes[i] = temp[i];
        }
        return outputBytes;
    }
}
