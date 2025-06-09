package test;

import src.Encoder;
import src.Decoder;

public class AdaptiveHuffmanTest {
    public static void main(String[] args) {
        System.out.println("=========== Adaptive Huffman Coding Test Suite ===========\n");
        
        // Test cases
        runTest("hello");
        runTest("adaptive huffman coding");
        runTest("AAAAABBBBCCCDDE");
        runTest("abcde");
        runTest("mississippi");
        runTest(""); // Empty string test
        
        System.out.println("============= All tests completed =============");
    }
    
    private static void runTest(String message) {
        System.out.println("Test case: \"" + message + "\"");
        
        // Encode
        Encoder encoder = new Encoder();
        String encoded = encoder.encode(message);
        
        // Decode
        Decoder decoder = new Decoder();
        String decoded = decoder.decode(encoded);
        
        // Calculate compression ratio if possible
        String compressionInfo = "";
        if (!message.isEmpty()) {
            double originalBits = message.length() * 8;
            double compressedBits = encoded.length();
            double ratio = compressedBits / originalBits;
            compressionInfo = String.format("Compression ratio: %.2f%%", ratio * 100);
        }
        
        // Print results
        System.out.println("Original: " + message);
        System.out.println("Encoded : " + encoded);
        System.out.println("Decoded : " + decoded);
        if (!message.isEmpty()) {
            System.out.println(compressionInfo);
        }
        System.out.println("Result  : " + (message.equals(decoded) ? "PASS" : "FAIL"));
        System.out.println("--------------------------------------------------\n");
    }
}