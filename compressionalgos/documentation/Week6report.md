# Week 6

[Time report](https://github.com/aleksinuora/compressionalgos/blob/master/compressionalgos/documentation/Time%20report.md)

## Progress report

Huffman algorithm for compressing and unpacking simple 8-bit text files is 
working as specified.

Turns out HashMaps were entirely unnecessary and got
replaced by regular arrays. PriorityQueue implemented. All external libraries
replaced with custom implementations , except in I/O, ui and test classes.

Unit testing done for all but one relevant class. Testing was very helpful
for bug fixing.

## Next week

- Have a look at LZW. If it seems doable in a couple of days go for it, but
prioritize finishing Huffman.
- Write tests for the main Huffman class.
- Implement performance testing
