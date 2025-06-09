package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HuffmanTree {
    private Node root;
    private Node nytNode;
    private Map<Character, Node> symbolToNode;
    private List<Node> nodeList;
    private int nextNodeNumber;

    public HuffmanTree() {
        nextNodeNumber = 512; // Starting with a large number to decrement
        nytNode = new Node('\0', 0, nextNodeNumber--);
        nytNode.setNYT(true);
        root = nytNode;
        symbolToNode = new HashMap<>();
        nodeList = new ArrayList<>();
        nodeList.add(nytNode);
    }

    //If character was seen before, return its Huffman code (path in tree)
    public String encode(char symbol) {
        if (symbolToNode.containsKey(symbol)) {
            return getPathToNode(symbolToNode.get(symbol));
        } else {
            // Character not seen before: NYT path + 8-bit ASCII
            return getPathToNode(nytNode) + getBinaryRepresentation(symbol);
        }
    }

    //Returns the 8-bit binary representation of the char
    private String getBinaryRepresentation(char c) {
        return String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');
    }

    //Start from the node and move up to the root
    private String getPathToNode(Node node) {
        StringBuilder path = new StringBuilder();
        Node current = node;
        
        while (current != root) {
            Node parent = current.getParent();
            if (parent.getLeftChild() == current) {
                path.append('0');
            } else {
                path.append('1');
            }
            current = parent;
        }
        
        return path.reverse().toString();
    }

    //Update the tree structure
    public void update(char symbol) {
        if (!symbolToNode.containsKey(symbol)) {
            addNewSymbol(symbol);//it’s a new symbol add it
        } else {
            updateExistingSymbol(symbol);// it’s a already exist increment weights and update tree
        }
    }

    private void addNewSymbol(char symbol) {
        // Create new leaf node for the symbol
        Node newLeaf = new Node(symbol, 1, nextNodeNumber--);
        
        // Create new internal node to replace NYT
        Node newInternal = new Node('\0', 0, nextNodeNumber--);
        
        // Set up relationships
        newInternal.setLeftChild(nytNode);
        newInternal.setRightChild(newLeaf);
        
        // Update parent relationships
        if (nytNode.getParent() != null) {
            Node nytParent = nytNode.getParent();
            if (nytParent.getLeftChild() == nytNode) {
                nytParent.setLeftChild(newInternal);
            } else {
                nytParent.setRightChild(newInternal);
            }
            newInternal.setParent(nytParent);
        } else {
            // NYT was the root
            root = newInternal;
        }
        
        nytNode.setParent(newInternal);
        newLeaf.setParent(newInternal);
        
        // Update node tracking
        symbolToNode.put(symbol, newLeaf);
        nodeList.add(newLeaf);
        nodeList.add(newInternal);
        
        // Update weights by sliding up the tree
        Node current = newLeaf;
        while (current != null) {
            if (current != newLeaf) { // Skip the new leaf since we already set its weight to 1
                current.incrementWeight();
            }
            checkAndSwap(current);
            current = current.getParent();
        }
    }


    //For existing symbols, just go up the tree, swap if needed, and increment weights
    private void updateExistingSymbol(char symbol) {
        Node leafNode = symbolToNode.get(symbol);
        Node current = leafNode;
        
        while (current != null) {
            checkAndSwap(current);
            current.incrementWeight();
            current = current.getParent();
        }
    }

    private void checkAndSwap(Node node) {
        if (node == root) {//no swap for root
            return;
        }
        //Find the highest-numbered node with the same weight
        Node highestNode = findHighestNodeWithSameWeight(node.getWeight());
        
        if (highestNode != null && highestNode != node && 
            highestNode != node.getParent() && node != highestNode.getParent() &&
            highestNode.getNodeNumber() > node.getNodeNumber()) {
                
            // Perform the swap
            swapNodes(node, highestNode);
        }
    }

    private Node findHighestNodeWithSameWeight(int weight) {
        Node highest = null;
        for (Node node : nodeList) {
            if (node.getWeight() == weight && !node.isNYT()) {
                if (highest == null || node.getNodeNumber() > highest.getNodeNumber()) {
                    highest = node;
                }
            }
        }
        return highest;
    }

    private void swapNodes(Node a, Node b) {
        // Don't swap if they're in a parent-child relationship
        if (a.getParent() == b || b.getParent() == a) {
            return;
        }
        
        // Keep parent references
        Node aParent = a.getParent();
        Node bParent = b.getParent();
        
        // Update parent's children references
        if (aParent.getLeftChild() == a) {
            aParent.setLeftChild(b);
        } else {
            aParent.setRightChild(b);
        }
        
        if (bParent.getLeftChild() == b) {
            bParent.setLeftChild(a);
        } else {
            bParent.setRightChild(a);
        }
        
        // Update nodes' parent references
        a.setParent(bParent);
        b.setParent(aParent);
        
        // Swap node numbers (order values)
        int tempNumber = a.getNodeNumber();
        a.setNodeNumber(b.getNodeNumber());
        b.setNodeNumber(tempNumber);
    }

    public Node decode(String bits, int[] position) {
        Node current = root;//Start from root
        
        while (!current.isLeaf() && position[0] < bits.length()) {
            char bit = bits.charAt(position[0]++);
            current = (bit == '0') ? current.getLeftChild() : current.getRightChild();
        }
        
        return current;//Stop at a leaf node
    }

    public Node getRoot() {
        return root;
    }

    public Node getNYTNode() {
        return nytNode;
    }

    public Node getNodeForSymbol(char symbol) {
        return symbolToNode.get(symbol);
    }

    //Tree Representation
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        
        // First, add all leaf nodes (symbols)
        for (Map.Entry<Character, Node> entry : symbolToNode.entrySet()) {
            Node node = entry.getValue();
            String code = getPathToNode(node);
            result.append("symbol '").append(node.getSymbol())
                  .append("' with code ").append(code)
                  .append(" and count ").append(node.getWeight())
                  .append("\n");
        }
        
        // Then, add NYT node
        String nytCode = getPathToNode(nytNode);
        result.append("NYT node with code ").append(nytCode)
              .append(" and count ").append(nytNode.getWeight())
              .append("\n");
        
        // Now, print the entire tree (all nodes in a readable way)
        printTree(root, result, "");
        
        return result.toString();
    }

    private void printTree(Node node, StringBuilder result, String code) {
        if (node == null) return;
        
        if (node.isNYT()) {
            result.append("NYT: ").append(code).append(" (weight: ").append(node.getWeight()).append(")\n");
        } else if (node.isLeaf()) {
            result.append("'").append(node.getSymbol()).append("': ")
                  .append(code).append(" (weight: ").append(node.getWeight()).append(")\n");
        }
        
        printTree(node.getLeftChild(), result, code + "0");
        printTree(node.getRightChild(), result, code + "1");
    }
}