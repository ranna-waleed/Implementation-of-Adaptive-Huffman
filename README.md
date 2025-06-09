# Implementation-of-Adaptive-Huffman
This project implements Adaptive Huffman Coding, a dynamic compression algorithm that updates the Huffman tree in real time during encoding and decoding. Key features include:

Real-time tree adaptation and symbol encoding

Efficient handling of new (NYT) and repeating symbols

JavaFX-based tree visualization

Compression ratio reporting

Developed in Java, the project includes full support for compression/decompression and visual demonstration.

# How to Run
# Compile
javac --module-path [javafx-lib] --add-modules javafx.controls,javafx.fxml \
      src/*.java visualization/*.java

# Run
java --module-path [javafx-lib] --add-modules javafx.controls,javafx.fxml \
     -cp src AdaptiveHuffman

Replace [javafx-lib] with the path to your local JavaFX library.

#vHow It Works

- Encoding:

If the symbol is new:

Output code for the NYT (Not Yet Transmitted) node

Output 8-bit ASCII of the new symbol

If the symbol exists:

Output the symbol's current Huffman code

The tree is updated dynamically to reflect symbol frequency.

- Decoding:

Traverse the tree using incoming bits

If NYT node is hit: read next 8 bits and insert new symbol

Update tree after every symbol

- Visualization:

JavaFX GUI shows the tree structure with:

Light green: NYT nodes

Light blue: Symbol leaves

White: Internal nodes

Edges labeled '0' (left) and '1' (right)
