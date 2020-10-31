# Implementation document

## General overview of algorithms

# Huffman coding

The Huffman compression algorithm builds a dictionary in the form of a binary tree, with leaves corresponding to input bytes, and then uses said tree to encode input as a binary string of
traversal paths. The binary tree is arranged such that the most common bytes have the shortest distances from the root node. For example, an input of "AAABBCD" would result in the following tree:

r: root
i: internal node

r

|\

| A

i

|\

i B

|\

C D

Marking right turns as 0 and left turns as 1, we would get the following code table:

| Byte | Code |
|------|------|
| A    | 0    |
| B    | 10   |
| C    | 111  |
| D    | 110  |

The structure of the binary tree is then encoded into the output file. Next, the original file is encoded according to the dictionary and added to the output. When decompressing, the binary tree is first recreated. Then, the encoded paths are used to traverse the tree and rebuild the original file.

# LZW (Lempel-Ziv-Welch) coding

The LZW algorithm relies on building a dictionary of repeating byte patterns and representing them with 9+ bit codewords. Initially, all 8 bit bytes are added to the dictionary. Whenever a new substring is encountered in the input, it gets added to the dictionary. Following occurrences of that substring get represented by the given codeword. When decoding, pretty much the same procedure in reverse is used to rebuild the dictionary. Any new codeword must encode a combination of previously encountered bytes.

The LZW algorithm saves a lot of space by using the input string as it's own de facto dictionary. Comparing it to the Huffman algorithm, there's no need to include a separate encoding tree or dictionary in the compressed file.

On the other hand, having a dictionary of all possible substrings would quickly eat up resources during coding and decoding. Substrings need to be added to the dictionary only as they are encountered and only up to a certain length. Compression efficiency is highly dependent on the maximum length of these substrings and their corresponding codewords. Some implementations use a fixed length of 9+ bits for the codewords, others dynamically alter the bit range as needed. The former approach means that every codeword has a fixed bit size, while the latter saves considerable space by only using as many bits as necessary for a given code.

## Particular implementation

# Huffman algorithm

Compression:
1. Read input bytes from file.
2. Write the frequency of each byte to an integer array.
3. Add the bytes as nodes into a priority queue, sorted by lowest frequency.
4. Poll two nodes from the queue at a time, until the queue is empty. For each node pair, create a new internal node. Add the node pair as the left and right child of the internal node, with the lower frequency one on the right. Set the frequency of the internal node as the sum of the node pair's frequencies. At the end of this step, all the nodes are linked into a binary tree sorted by frequency.
5. Do a recursive depth-first traversal of the binary tree. At each step, write out a 1-bit for a left turn, 0 for a right turn, 1 for an internal node and a 0 for a leaf node. Whenever a leaf node is encountered, also write out it's value.
6. On the same pass, add the paths to an integer array, using the leaf node values as an index. At the end of steps 5 and 6, we have the binary tree encoded into the output file and an array dictionary of codewords, indexed by input bytes.
7. Go through the original input bytes again and encode it to the output file using the dictionary created during step 6.

Decompression:
1. Read the first part of the input bytes from compressed file.
2. Create a root node. Starting with the root node, read bits one by one and recursively rebuild the binary tree. When a 1-bit is encountered, add a left child for the current node and vice versa for 0. If the following bit is a 1, the new node is internal -> recurse with the new node. If it's a 0, the new node is a leaf node -> set node value the next 8 bits and end recursion branch.
3. Read the rest of the input bytes from compressed file.
4. Traverse the tree created in step 2. Again, if the next input bit is a 1, take a left turn, right turn for a 0 and so on. When a leaf node is reached, write out it's value to output and start again until all the input bits are read.


# LZW algorithm

Compression:
1. Read input bytes from file. Add the first byte to buffer. Initialize a AVL-tree dictionary with all 8 bit values, with keys ranging from 0 to 255. Set the dictionary index to 256. Set current codeword byte size to 9.
2. If the value in the buffer + next input value is already in the dictionary:
	Set the buffer to the key that encoded [buffer + next input]. Go to 2.
   If not:
	Write the buffer to output. Add a node with value [buffer + next input] to the dictionary 		with the current dictionary index as it's key. Increment dictionary index by one.
3. If the current dictionary index in bit form is a series of ones, write it to output to mark a byte size increase. Increment dictionary index by 1 to avoid collisions between codewords and byte size markers.
4. While input bytes remain, go back to 2.
5. Write out the buffer.

Decompression:
Decompression is pretty much the same procedure as compression, except in reverse. The dictionary is built on the go, in the same fashion, and when a codeword is read the output gets fetched from the dictionary.

Problems:
The LZW algorithm currently doesn't detect null bytes correctly, limiting it's usefulness to 8-bit file formats.

# Performance

Performance tests were run with the test files found in "/testing" folder. These can easily be repeated from within the program. Compiled results in chart form can be found here:
https://github.com/aleksinuora/compressionalgos/tree/master/compressionalgos/documentation/PerformanceTestCharts.
