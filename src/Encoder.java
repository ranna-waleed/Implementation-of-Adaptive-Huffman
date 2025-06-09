package src;

public class Encoder {
    private HuffmanTree tree;
    private StringBuilder encodedBits;
    private int charCount = 0;
    
    public Encoder() {
        tree = new HuffmanTree();
        encodedBits = new StringBuilder();
    }
    
    public String encode(String message) {
        encodedBits = new StringBuilder();
        
        for (int i = 0; i < message.length(); i++) {
            char symbol = message.charAt(i);
            String code = tree.encode(symbol);
            encodedBits.append(code);
            charCount++;
            
            // Update the tree with this symbol
            tree.update(symbol);
            
            // Display the required output format
            System.out.println("\nAfter encoding the character '" + symbol + "' (character #" + charCount + 
                              "), the compressed stream is '" + encodedBits.toString() + "'");
            System.out.println("The tree contains mainly the following nodes:");
            System.out.println(tree);
        }
        
        return encodedBits.toString();
    }
    
    public HuffmanTree getTree() {
        return tree;
    }
}