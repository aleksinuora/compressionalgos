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
    private final static boolean debug1 = false;
    private final static boolean debug2 = false;
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
            } else {
                // Mark the next byte. 0 for 8-bit byte (raw value), 
                // 1 for n-bit coded byte.
                if (buffer.getBitCount() == 8) {
                    output.add(false);
                } else {
                    output.add(true);
                }
                output.concatenate(buffer);                
                // Increment code word byte size if necessary. Max byteSize == 15.
                if (intTools.getBitCount(dictionaryIndex) 
                        < intTools.getBitCount(dictionaryIndex + 1) && (byteSize < 15)) {
                    // Output n 1's to mark a byte size increase, where n
                    // is the old byteSize. Skip the last byte of the n-bit range
                    // to avoid collisions between dictionary keys and byte size
                    // markers. I.e., the last 9-bit dictionaryIndex is 510,
                    // the next one will be 512 (10 bits) and so on.
                    // The increment code also needs to be marked as a code by
                    // a prepended 1, so let's write (byteSize + 1) * (1).
                    for (int j = 0; j < byteSize + 1; j++) {
                        
                        if (debug1) {
                            System.out.print("1");
                        }
                        
                        output.add(true);
                    }
                    dictionaryIndex++;
                    byteSize++;
                    
                    if (debug2) {
                        System.out.println("\nbyte size increased, next index: " + dictionaryIndex + " new byteSize: " + byteSize);
                    }
                    
                }                               
//                if (debug)
//                    {
//                    System.out.println("output: " + (buffer.getInt()) 
//                            + ", byte size: " + buffer.getBitCount() 
//                            + ", dictionary index: " + dictionaryIndex);
//                }
                if (intTools.getBitCount(dictionaryIndex + 1) < 16) {
                    dictionary.add(nextString, dictionaryIndex++);
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
        output.addWholeByte(padBits);
        
        if (debug2) {
            System.out.println("Final dI after comp: " + dictionaryIndex);
        }
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
        byte[] fileType = new byte[3];
        for (int i = 0; i < 3; i++) {
            if (bytes[i] != expected[i]) {
                throw new Exception("Not a valid LZW file");
            }
            if (bytes[i] != 0) {
                fileType[i] = bytes[i + 3];
            }
        }
        filetype = "." + new String(fileType);
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
            // Check dictionaryIndex. If it's at maximum value for it's current
            // byteSize range, skip to the next value. This is done to avoid
            // indexes with all 1's. The actual check works by comparing the
            // bit count of the NEXT value to byteSize; if it's larger, the 
            // previous value must be all 1's.
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
                bitIndex += byteSize;
                // 1. Check for byte size increment code ([byteSize] long 
                //  series of 1-bits).
                // 2. If there's a byte size increment, clear nextCode and continue.
                if (increaseByteSize(nextCode, byteSize)) {
                    
                    if (debug2) {
                        System.out.print("\nnC: " + nextCode.bitsToString() + " Byte size increased, new byte size: ");
                    }                    
                    
                    byteSize++;
                    
                    if (debug2) {
                        System.out.println(byteSize + "\n");;
                    }
                    
                    nextCode.clear();
                    dictionaryIndex++;
                    continue;
//                    nextCode.concatenate(input.getBits(bitIndex, byteSize));
                }
            }            
            if (dictionary.getValue(nextCode.getInt()) == Integer.MAX_VALUE) {
                // EXCEPTION: NEXT INPUT IS A KEY BUT HAS NO DICTIONARY ENTRY
                // 1. Concatenate the last byte from the latest dictionary entry 
                //  to buffer. The dictionary returns an int value but using 
                //  the addWholeByte method
                //  ensures that only the last 8 bits are added to buffer.
                // 2. Add this new value to the dictionary.
                // 3. Output the string derived from the new value, 
                //  clean up and continue.
                // But first: if current dictionaryIndex is at maximum value for
                // curent byteSize range (i.e.: n ones where n is byteSize), 
                // roll back to previous index.
                if (intTools.getBitCount(dictionaryIndex + 1) > byteSize) {
                    dictionaryIndex--;
                    
                    if (debug2) {
                        System.out.println("dictionary rollback at " + dictionaryIndex);
                    }
                }
                // If the previous index is < 256, no entries have been added
                // -> the previous value we are looking for can only be a raw 
                // byte. Which one? The last one that was written to output,
                // which is currently in the buffer. Otherwise, add the last
                // byte from the last dictionary entry value to buffer.
                if (dictionaryIndex - 1 < 256) {
                    buffer.addWholeByte(buffer.getInt());
                } else {
                    buffer.addWholeByte((dictionary.getValue(dictionaryIndex - 1)));
                }                
                dictionary.add(nextCode.getInt(), buffer.getInt());
                
                if (debug1) {
                    System.out.println("\ne" + nextCode.getInt() + ":" + buffer.bitsToString() + " added to dictionary");
                }
                
                dictionaryIndex++;
                buffer.clear();
                buffer.concatenate(nextCode);
                nextCode.clear();
                output.concatenate(dictionary.getString(buffer.getInt()));
                
                if (debug1) {
                    System.out.println("Exception, buffer: " 
                            + buffer.getInt()); 
                }                               
                continue;
            }
            // If nextCode is a code word and no exceptions occurred, write the
            // whole raw byte string encoded by nextCode to nextString. The
            // first byte of nextString then gets concatenated to buffer. Buffer
            // should now have the value from the previous iteration + first
            // byte from the next output string.
            //  If the dictionary has a key for this buffered byte pair, write out 
            // nextString. Otherwise, add buffer value to dictionary.
            nextString.concatenate(dictionary.getString(nextCode.getInt()));
            buffer.concatenate(nextString.getBits(0, 8));
            if (dictionary.getKey(buffer.getInt()) != Integer.MAX_VALUE) {                
                output.concatenate(nextString);
                buffer.clear();
                buffer.concatenate(nextCode);
            } else {
                if (!(intTools.getBitCount(dictionaryIndex + 1) > 15)) {
                    dictionary.add(dictionaryIndex++, buffer.getInt());
                }
                
                if (debug1) {
                    System.out.println((dictionaryIndex - 1) + ":" + buffer.bitsToString() + " added to dictionary");
                }
                
                output.concatenate(nextString);
                buffer.clear();
                buffer.concatenate(nextCode);
            }
            
            if (debug1) {
                System.out.println("nC: " + nextCode.getInt() + " nS: " + nextString.getInt() 
                        + ", buffer: " + buffer.getInt() + ", byte: " 
                        + (bitIndex / 8) + "." + (bitIndex % 8) 
                        + ", byte size: " + byteSize);
            }
            nextCode.clear();
            nextString.clear();
        }
        
        if (debug2) {
            System.out.println("Final dI after decomp: " + dictionaryIndex);
            int key = 32766;
            System.out.println("Dictionary entry for " + key + " ([code]+[last byte]): " + dictionary.getValue(key) + ":" + Integer.toBinaryString(dictionary.getValue(key)));
            System.out.println("Dictionary value for " + key + "([full bytes]): " + dictionary.getString(key).bitsToString());
            int value = 0b0100010101110100;
            System.out.println("Dictionary key for value " + value + ": " + dictionary.getKey(value));
        }
    }

    private boolean increaseByteSize(BitString code, int byteSize) {
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
        byte[] temp = output.getArray(true);
        for (int i = 0; i < outputBytes.length; i++) {
            outputBytes[i] = temp[i];
        }
        return outputBytes;
    }
}
