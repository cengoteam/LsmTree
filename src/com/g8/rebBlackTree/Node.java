package com.g8.rebBlackTree;
import com.g8.model.Record;
public class Node {
    Record data;

    Node left;
    Node right;
    Node parent;

    boolean color;

    public Node(Record data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Node{" +
                "data=" + data +
                ", left=" + left +
                ", right=" + right +
                ", parent=" + parent +
                ", color=" + color +
                '}';
    }
}
