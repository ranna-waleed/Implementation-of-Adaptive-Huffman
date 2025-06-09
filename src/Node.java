package src;

public class Node {
    private char symbol;
    private int weight;
    private int nodeNumber;
    private Node parent;
    private Node leftChild;
    private Node rightChild;
    private boolean isNYT;

    public Node(char symbol, int weight, int nodeNumber) {
        this.symbol = symbol;
        this.weight = weight;
        this.nodeNumber = nodeNumber;
        this.parent = null;
        this.leftChild = null;
        this.rightChild = null;
        this.isNYT = false;
    }

    public boolean isLeaf() {
        return leftChild == null && rightChild == null;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public int getWeight() {
        return weight;
    }

    public void incrementWeight() {
        this.weight++;
    }

    public int getNodeNumber() {
        return nodeNumber;
    }

    public void setNodeNumber(int nodeNumber) {
        this.nodeNumber = nodeNumber;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public Node getRightChild() {
        return rightChild;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }

    public boolean isNYT() {
        return isNYT;
    }

    public void setNYT(boolean isNYT) {
        this.isNYT = isNYT;
    }

    @Override
    public String toString() {
        if (isNYT) {
            return "NYT";
        } else if (isLeaf()) {
            return "'" + symbol + "'";
        } else {
            return "*";
        }
    }
}