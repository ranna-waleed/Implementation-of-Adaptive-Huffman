package Visualization;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Queue;

// Import our classes from src package
import src.HuffmanTree;
import src.Node;

public class HuffmanTreeVisualizer extends JPanel {
    private HuffmanTree tree;
    private Map<Node, Point> nodePositions;
    private Set<Node> highlightedNodes;
    private int nodeSize = 50;
    private int horizontalGap = 80;
    private int verticalGap = 80;
    private int fontHeight = 14;
    

    public HuffmanTreeVisualizer(HuffmanTree tree) {
        this.tree = tree;
        this.nodePositions = new HashMap<>();
        this.highlightedNodes = new HashSet<>();
        
        setPreferredSize(new Dimension(1000, 600));
        setBackground(Color.WHITE);
    }
    
    public void updateTree(HuffmanTree tree) {
        this.tree = tree;
        repaint();
    }
    // Update the tree and highlight the specified node
    public void highlightNode(Node node) {
        highlightedNodes.clear();
        if (node != null) {
            highlightedNodes.add(node);
        }
        repaint();
    }
    
    // Update the tree and highlight two nodes
    // This method is used to highlight the NYT node and the newly added node
    public void highlightNodes(Node node1, Node node2) {
        highlightedNodes.clear();
        if (node1 != null) highlightedNodes.add(node1);
        if (node2 != null) highlightedNodes.add(node2);
        repaint();
    }
    
    // Update the tree and clear highlighted nodes
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (tree == null || tree.getRoot() == null) {
            return;
        }
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Calculate positions
        nodePositions.clear();
        calculateNodePositions(tree.getRoot(), getWidth() / 2, 50, getWidth() / 4);
        
        // Draw edges
        drawEdges(g2d, tree.getRoot());
        
        // Draw nodes
        drawNodes(g2d);
    }
    
    private void calculateNodePositions(Node node, int x, int y, int offset) {
        if (node == null) return;
        
        nodePositions.put(node, new Point(x, y));
        
        // Calculate positions for children with updated horizontal offset
        int nextOffset = Math.max(horizontalGap, offset / 2);
        
        if (node.getLeftChild() != null) {
            calculateNodePositions(node.getLeftChild(), x - offset, y + verticalGap, nextOffset);
        }
        
        if (node.getRightChild() != null) {
            calculateNodePositions(node.getRightChild(), x + offset, y + verticalGap, nextOffset);
        }
    }
    
    private void drawEdges(Graphics2D g, Node node) {
        if (node == null) return;
        
        Point nodePoint = nodePositions.get(node);
        
        if (nodePoint == null) return;
        
        if (node.getLeftChild() != null) {
            Point leftPoint = nodePositions.get(node.getLeftChild());
            if (leftPoint != null) {
                g.setColor(Color.BLACK);
                g.setStroke(new BasicStroke(2.0f));
                g.drawLine(nodePoint.x, nodePoint.y, leftPoint.x, leftPoint.y);
                
                // Draw edge label
                int midX = (nodePoint.x + leftPoint.x) / 2;
                int midY = (nodePoint.y + leftPoint.y) / 2;
                g.drawString("0", midX - 5, midY - 5);
                
                drawEdges(g, node.getLeftChild());
            }
        }
        
        if (node.getRightChild() != null) {
            Point rightPoint = nodePositions.get(node.getRightChild());
            if (rightPoint != null) {
                g.setColor(Color.BLACK);
                g.setStroke(new BasicStroke(2.0f));
                g.drawLine(nodePoint.x, nodePoint.y, rightPoint.x, rightPoint.y);
                
                // Draw edge label
                int midX = (nodePoint.x + rightPoint.x) / 2;
                int midY = (nodePoint.y + rightPoint.y) / 2;
                g.drawString("1", midX + 5, midY - 5);
                
                drawEdges(g, node.getRightChild());
            }
        }
    }
    
    private void drawNodes(Graphics2D g) {
        for (Map.Entry<Node, Point> entry : nodePositions.entrySet()) {
            Node node = entry.getKey();
            Point point = entry.getValue();
            
            // Determine node colors
            if (highlightedNodes.contains(node)) {
                g.setColor(Color.RED);
            } else if (node.isNYT()) {
                g.setColor(Color.BLUE);
            } else if (node.isLeaf()) {
                g.setColor(Color.GREEN);
            } else {
                g.setColor(Color.LIGHT_GRAY);
            }
            
            // Draw node circle
            g.fillOval(point.x - nodeSize/2, point.y - nodeSize/2, nodeSize, nodeSize);
            g.setColor(Color.BLACK);
            g.drawOval(point.x - nodeSize/2, point.y - nodeSize/2, nodeSize, nodeSize);
            
            // Draw node information
            String nodeText;
            if (node.isNYT()) {
                nodeText = "NYT";
            } else if (node.isLeaf()) {
                nodeText = "'" + node.getSymbol() + "'";
            } else {
                nodeText = "*";
            }
            
            // Node ID and weight
            String weightText = "W:" + node.getWeight();
            String idText = "#" + node.getNodeNumber();
            
            // Center text
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(nodeText);
            int weightWidth = fm.stringWidth(weightText);
            int idWidth = fm.stringWidth(idText);
            
            g.drawString(nodeText, point.x - textWidth/2, point.y);
            g.drawString(weightText, point.x - weightWidth/2, point.y + fontHeight);
            g.drawString(idText, point.x - idWidth/2, point.y - fontHeight);
        }
    }
    
    public static void main(String[] args) {
        // Create a swing application to display the tree
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Adaptive Huffman Tree Visualizer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            
            // Create a state container that will be used across lambda expressions
            class EncoderState {
                HuffmanTree tree = new HuffmanTree();
                Queue<Character> charQueue = new LinkedList<>();
            }
            
            final EncoderState state = new EncoderState();
            
            // Create components
            final HuffmanTreeVisualizer visualizer = new HuffmanTreeVisualizer(state.tree);
            
            // Control panel
            JPanel controlPanel = new JPanel();
            JTextField inputField = new JTextField(20);
            JButton encodeButton = new JButton("Encode");
            JButton stepButton = new JButton("Step");
            JLabel statusLabel = new JLabel("Enter text to encode");
            
            controlPanel.add(new JLabel("Input:"));
            controlPanel.add(inputField);
            controlPanel.add(encodeButton);
            controlPanel.add(stepButton);
            
            // Layout
            frame.setLayout(new BorderLayout());
            frame.add(new JScrollPane(visualizer), BorderLayout.CENTER);
            frame.add(controlPanel, BorderLayout.NORTH);
            frame.add(statusLabel, BorderLayout.SOUTH);
            
            // Encode button logic
            encodeButton.addActionListener(e -> {
                String input = inputField.getText();
                if (input.isEmpty()) {
                    statusLabel.setText("Please enter some text!");
                    return;
                }
                
                // Reset and prepare for stepping
                state.tree = new HuffmanTree();
                visualizer.updateTree(state.tree);
                
                state.charQueue.clear();
                for (char c : input.toCharArray()) {
                    state.charQueue.add(c);
                }
                
                statusLabel.setText("Ready to encode: " + input);
                inputField.setEnabled(false);
                encodeButton.setEnabled(false);
                stepButton.setEnabled(true);
            });
            
            // Step button logic
            stepButton.addActionListener(e -> {
                if (state.charQueue.isEmpty()) {
                    statusLabel.setText("Encoding complete!");
                    inputField.setEnabled(true);
                    encodeButton.setEnabled(true);
                    stepButton.setEnabled(false);
                    return;
                }
                
                char nextChar = state.charQueue.poll();
                String code = state.tree.encode(nextChar);
                
                // Get node for highlighting before update
                Node nodeBeforeUpdate = state.tree.getNodeForSymbol(nextChar);
                
                // Update tree
                state.tree.update(nextChar);
                
                // Get node after update for highlighting
                Node nodeAfterUpdate = state.tree.getNodeForSymbol(nextChar);
                
                visualizer.highlightNodes(nodeBeforeUpdate, nodeAfterUpdate);
                visualizer.updateTree(state.tree);
                
                statusLabel.setText("Encoded '" + nextChar + "' as: " + code + " | Remaining: " + state.charQueue.size());
            });
            
            // Initial state
            stepButton.setEnabled(false);
            
            // Pre-load with default text
            inputField.setText("aacbdad");
            
            frame.setVisible(true);
        });
    }
}