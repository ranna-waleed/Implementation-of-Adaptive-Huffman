package src;

public class Decoder {
    private HuffmanTree tree;
    private int charCount = 0;
    
    public Decoder() {
        tree = new HuffmanTree();
    }
    
    public String decode(String encodedBits) {
        StringBuilder decodedMessage = new StringBuilder();
        int[] position = new int[1]; // Using array to pass by reference
        
        //while loop to decode the message
        // The loop continues until all bits are processed
        while (position[0] < encodedBits.length()) {
            Node node = tree.decode(encodedBits, position);
            
            if (node.isNYT()) {
                // Read the next 8 bits for the ASCII character
                if (position[0] + 8 <= encodedBits.length()) {
                    String asciiCode = encodedBits.substring(position[0], position[0] + 8);
                    position[0] += 8;
                    char symbol = (char) Integer.parseInt(asciiCode, 2);
                    decodedMessage.append(symbol);
                    tree.update(symbol);
                }
            } else {
                char symbol = node.getSymbol();
                decodedMessage.append(symbol);
                tree.update(symbol);
            }
            
            charCount++;//Increment the character count each time a new symbol is decoded
            System.out.println("\nAfter decoding character #" + charCount + 
                              " ('" + decodedMessage.charAt(decodedMessage.length()-1) + 
                              "'), the current decoded message is '" + decodedMessage.toString() + "'");
            System.out.println("The tree contains mainly the following nodes:");
            System.out.println(tree);
        }
        
        return decodedMessage.toString();
    }
    
    public HuffmanTree getTree() {
        return tree;
    }
}