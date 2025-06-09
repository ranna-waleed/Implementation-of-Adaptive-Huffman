package src;

public class AdaptiveHuffman {
    public static void main(String[] args) {
        testAdaptiveHuffman("aacbdad");
        
        // Launch the visualizer
        Visualization.HuffmanTreeVisualizer.main(args);
    }
    
    public static void testAdaptiveHuffman(String message) {
        System.out.println("Original message: " + message);
        System.out.println("=== ENCODING PROCESS ===");
        
        // Encoding process
        Encoder encoder = new Encoder();
        String encoded = encoder.encode(message);
        System.out.println("\nFinal compressed stream: " + encoded);
        
        System.out.println("\n=== DECODING PROCESS ===");
        // Decoding process
        Decoder decoder = new Decoder();
        String decoded = decoder.decode(encoded);
        System.out.println("\nFinal decoded message: " + decoded);
        
        // Verification
        System.out.println("\nVerification: " + (message.equals(decoded) ? "SUCCESS" : "FAILURE"));
    }
}