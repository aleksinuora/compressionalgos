###Study program
Computer Science, Bachelor studies

#Project specification
This project concerns the implementation and performance comparison of two or more compression algorithms, including at least Huffman coding and Lempel-Ziv compression.
All documentation, reports etc. will be in English. Programming will be done in Java.

##Initial estimate of implemented data structures
* Huffman tree
* Min heap
* Hash table
* Binary search tree
* Various utility functions

##I/O
###Compression
* Input: any type of file
* Output: a compressed file

###Decoding
* Input: a previously compressed file
* Output: the original, uncompressed file

###UI
* prompt for file source and destination paths

##Space/time complexity
Various sources cite between O(nlogn) and O(n) time complexity for both Huffman and Lempel-Ziv compression algorithms, depending on several factors. This seems to be
a reasonable benchmark.

Space complexities should be in the range of O(n). 

##Sources
https://en.wikipedia.org/wiki/Huffman_coding
https://en.wikipedia.org/wiki/Lempel%E2%80%93Ziv%E2%80%93Welch
https://stackoverflow.com/questions/6189765/big-o-complexities-of-algorithms-lzw-and-huffman
https://www.studytonight.com/data-structures/huffman-coding
https://sites.google.com/site/datacompressionguide/lzw
