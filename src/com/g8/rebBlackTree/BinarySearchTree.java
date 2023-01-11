package com.g8.rebBlackTree;

public interface BinarySearchTree<T> extends BinaryTree {

    Node searchNode(T key);
    void insertNode(T key);
    void deleteNode(T key);
}