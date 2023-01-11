package com.g8.rebBlackTree;
import com.g8.model.MovieRecord;
import com.g8.model.Record;

import java.security.spec.RSAOtherPrimeInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * A red-black tree implementation with <code>int</code> keys.
 *
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class RedBlackTree extends BaseBinaryTree implements BinarySearchTree<Record> {

    static final boolean RED = false;
    static final boolean BLACK = true;

    @Override
    public Node searchNode(Record key) {
        Node node = root;
        while (node != null) {
            if (key.compareByKey(node.data) == 0) {
                return node;
            } else if (key.compareByKey(node.data) < 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }

        return null;
    }

    public Record searchRecordByKey(String searchKey){
        Node found = null;
        return (found = searchNodeByKey(searchKey)) == null ? null : found.data;
    }

    public List<Record> searchByKeyRange(String start, String end){
        List<Record> results = new ArrayList<>();


        if(start != null && end != null){
            Node startNode = searchNodeByKey(start);
            Node endNode = searchNodeByKey(end);
            getNodesBetween(root,startNode, endNode, results);

        }
        else if(start == null){
            Node endNode = searchNodeByKey(end);
            if(endNode == null){
                return null;
            }
            getSmallerNodes(root,endNode,results);
            results.add(endNode.data);
        }
        else{
            Node startNode = searchNodeByKey(start);
            if(startNode == null){
                return null;
            }
            getLargerNodes(root, startNode, results);
            results.add(startNode.data);
        }
        return results;

    }

    // -- Insertion ----------------------------------------------------------------------------------

    @Override
    public void insertNode(Record key) {
        Node node = root;
        Node parent = null;
        boolean isUpdate = false;

        // Traverse the tree to the left or right depending on the key
        while (node != null) {
            parent = node;
            if (key.compareByKey(node.data) < 0) {
                node = node.left;
            } else if (key.compareByKey(node.data)>0) {
                node = node.right;
            }else{
                isUpdate = true;
                break;
            }
        }
        Node newNode = new Node(key);
        newNode.color = RED;
        if(isUpdate){
            // update new node
            node.data = newNode.data;
        }else{
            // Insert new node

            if (parent == null) {
                root = newNode;
            } else if (key.compareByKey(parent.data)< 0 ) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }
            newNode.parent = parent;
        }


        fixRedBlackPropertiesAfterInsert(newNode);
    }

    private void fixRedBlackPropertiesAfterInsert(Node node) {
        Node parent = node.parent;

        // Case 1: Parent is null, we've reached the root, the end of the recursion
        if (parent == null) {
            node.color = BLACK;
            return;
        }

        // Parent is black --> nothing to do
        if (parent.color == BLACK) {
            return;

        }

        // From here on, parent is red
        Node grandparent = parent.parent;


        // Get the uncle (aszmay be null/nil, in which case its color is BLACK)
        Node uncle = getUncle(parent);

        // Case 3: Uncle is red -> recolor parent, grandparent and uncle
        if (uncle != null && uncle.color == RED) {
            parent.color = BLACK;
            grandparent.color = RED;
            uncle.color = BLACK;

            // Call recursively for grandparent, which is now red.
            // It might be root or have a red parent, in which case we need to fix more...
            fixRedBlackPropertiesAfterInsert(grandparent);
        }

        // Note on performance:
        // It would be faster to do the uncle color check within the following code. This way
        // we would avoid checking the grandparent-parent direction twice (once in getUncle()
        // and once in the following else-if). But for better understanding of the code,
        // I left the uncle color check as a separate step.

        // Parent is left child of grandparent
        else if (parent == grandparent.left) {
            // Case 4a: Uncle is black and node is left->right "inner child" of its grandparent
            if (node == parent.right) {
                rotateLeft(parent);

                // Let "parent" point to the new root node of the rotated sub-tree.
                // It will be recolored in the next step, which we're going to fall-through to.
                parent = node;
            }

            // Case 5a: Uncle is black and node is left->left "outer child" of its grandparent
            rotateRight(grandparent);

            // Recolor original parent and grandparent
            parent.color = BLACK;
            grandparent.color = RED;
        }

        // Parent is right child of grandparent
        else {
            // Case 4b: Uncle is black and node is right->left "inner child" of its grandparent
            if (node == parent.left) {
                rotateRight(parent);

                // Let "parent" point to the new root node of the rotated sub-tree.
                // It will be recolored in the next step, which we're going to fall-through to.
                parent = node;
            }

            // Case 5b: Uncle is black and node is right->right "outer child" of its grandparent
            rotateLeft(grandparent);

            // Recolor original parent and grandparent
            parent.color = BLACK;
            grandparent.color = RED;
        }
    }

    private Node getUncle(Node parent) {
        Node grandparent = parent.parent;
        if (grandparent.left == parent) {
            return grandparent.right;
        } else if (grandparent.right == parent) {
            return grandparent.left;
        } else {
            throw new IllegalStateException("Parent is not a child of its grandparent");
        }
    }

    // -- Deletion -----------------------------------------------------------------------------------

    @Override
    public void deleteNode(Record key) {
        Node node = root;

        // Find the node to be deleted
        while (node != null && node.data.compareByKey(key) != 0  ) {
            // Traverse the tree to the left or right depending on the key
            if (key.compareByKey(node.data) < 0 ) {
                node = node.left;
            } else {
                node = node.right;
            }
        }

        // Node not found?
        if (node == null) {
            return;
        }

        // At this point, "node" is the node to be deleted

        // In this variable, we'll store the node at which we're going to start to fix the R-B
        // properties after deleting a node.
        Node movedUpNode;
        boolean deletedNodeColor;

        // Node has zero or one child
        if (node.left == null || node.right == null) {
            movedUpNode = deleteNodeWithZeroOrOneChild(node);
            deletedNodeColor = node.color;
        }

        // Node has two children
        else {
            // Find minimum node of right subtree ("inorder successor" of current node)
            Node inOrderSuccessor = findMinimum(node.right);

            // Copy inorder successor's data to current node (keep its color!)
            node.data = inOrderSuccessor.data;

            // Delete inorder successor just as we would delete a node with 0 or 1 child
            movedUpNode = deleteNodeWithZeroOrOneChild(inOrderSuccessor);
            deletedNodeColor = inOrderSuccessor.color;
        }

        if (deletedNodeColor == BLACK) {
            fixRedBlackPropertiesAfterDelete(movedUpNode);

            // Remove the temporary NIL node
            if (movedUpNode.getClass() == NilNode.class) {
                replaceParentsChild(movedUpNode.parent, movedUpNode, null);
            }
        }
    }

    private Node deleteNodeWithZeroOrOneChild(Node node) {
        // Node has ONLY a left child --> replace by its left child
        if (node.left != null) {
            replaceParentsChild(node.parent, node, node.left);
            return node.left; // moved-up node
        }

        // Node has ONLY a right child --> replace by its right child
        else if (node.right != null) {
            replaceParentsChild(node.parent, node, node.right);
            return node.right; // moved-up node
        }

        // Node has no children -->
        // * node is red --> just remove it
        // * node is black --> replace it by a temporary NIL node (needed to fix the R-B rules)
        else {
            Node newChild = node.color == BLACK ? new NilNode() : null;
            replaceParentsChild(node.parent, node, newChild);
            return newChild;
        }
    }

    private Node findMinimum(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }


    private void fixRedBlackPropertiesAfterDelete(Node node) {
        // Case 1: Examined node is root, end of recursion
        if (node == root) {
            node.color = BLACK;
            return;
        }

        Node sibling = getSibling(node);

        // Case 2: Red sibling
        if (sibling.color == RED) {
            handleRedSibling(node, sibling);
            sibling = getSibling(node); // Get new sibling for fall-through to cases 3-6
        }

        // Cases 3+4: Black sibling with two black children
        if (isBlack(sibling.left) && isBlack(sibling.right)) {
            sibling.color = RED;

            // Case 3: Black sibling with two black children + red parent
            if (node.parent.color == RED) {
                node.parent.color = BLACK;
            }

            // Case 4: Black sibling with two black children + black parent
            else {
                fixRedBlackPropertiesAfterDelete(node.parent);
            }
        }

        // Case 5+6: Black sibling with at least one red child
        else {
            handleBlackSiblingWithAtLeastOneRedChild(node, sibling);
        }
    }

    private void handleRedSibling(Node node, Node sibling) {
        // Recolor...
        sibling.color = BLACK;
        node.parent.color = RED;

        // ... and rotate
        if (node == node.parent.left) {
            rotateLeft(node.parent);
        } else {
            rotateRight(node.parent);
        }
    }

    private void handleBlackSiblingWithAtLeastOneRedChild(Node node, Node sibling) {
        boolean nodeIsLeftChild = node == node.parent.left;

        // Case 5: Black sibling with at least one red child + "outer nephew" is black
        // --> Recolor sibling and its child, and rotate around sibling
        if (nodeIsLeftChild && isBlack(sibling.right)) {
            sibling.left.color = BLACK;
            sibling.color = RED;
            rotateRight(sibling);
            sibling = node.parent.right;
        } else if (!nodeIsLeftChild && isBlack(sibling.left)) {
            sibling.right.color = BLACK;
            sibling.color = RED;
            rotateLeft(sibling);
            sibling = node.parent.left;
        }

        // Fall-through to case 6...

        // Case 6: Black sibling with at least one red child + "outer nephew" is red
        // --> Recolor sibling + parent + sibling's child, and rotate around parent
        sibling.color = node.parent.color;
        node.parent.color = BLACK;
        if (nodeIsLeftChild) {
            sibling.right.color = BLACK;
            rotateLeft(node.parent);
        } else {
            sibling.left.color = BLACK;
            rotateRight(node.parent);
        }
    }

    private void inOrderTraversal(Node root, List<Record> res) {
        if (root == null) return;
        inOrderTraversal(root.left, res);
        res.add(root.data);
        inOrderTraversal(root.right, res);
    }


    private void getNodesBetween(Node root, Node lower, Node upper, List<Record> res) {
        if (root == null) return;
        if(root.data.compareByKey(lower.data)> 0) getNodesBetween(root.left, lower, upper, res);
        if(root.data.compareByKey(lower.data) >= 0  && root.data.compareByKey(upper.data) <= 0) res.add(root.data);
        if(root.data.compareByKey(upper.data) < 0) getNodesBetween(root.right, lower, upper, res);
    }


    private void getSmallerNodes(Node root, Node target, List<Record> res) {
        if (root == null) return;
        getSmallerNodes(root.left, target, res);
        if(root.data.compareByKey(target.data) < 0 ) res.add(root.data);
        getSmallerNodes(root.right, target, res);
    }
    private void getLargerNodes(Node root, Node target, List<Record> res) {
        if (root == null) return;
        getLargerNodes(root.right, target, res);
        if(root.data.compareByKey(target.data) > 0) res.add(root.data);
        getLargerNodes(root.left, target, res);
    }

    private Node getSibling(Node node) {
        Node parent = node.parent;
        if (node == parent.left) {
            return parent.right;
        } else if (node == parent.right) {
            return parent.left;
        } else {
            throw new IllegalStateException("Parent is not a child of its grandparent");
        }
    }

    private Node searchNodeByKey(String searchKey){
        Record key = new MovieRecord(searchKey, 0);
        Node node = root;
        while (node != null) {
            if (key.compareByKey(node.data) == 0) {
                return node;
            } else if (key.compareByKey(node.data) < 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }

        return null;
    }

    private boolean isBlack(Node node) {
        return node == null || node.color == BLACK;
    }

    private static class NilNode extends Node {
        private NilNode() {
            super(null);
            this.color = BLACK;
        }
    }

    // -- Helpers for insertion and deletion ---------------------------------------------------------

    private void rotateRight(Node node) {
        Node parent = node.parent;
        Node leftChild = node.left;

        node.left = leftChild.right;
        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }

        leftChild.right = node;
        node.parent = leftChild;

        replaceParentsChild(parent, node, leftChild);
    }

    private void rotateLeft(Node node) {
        Node parent = node.parent;
        Node rightChild = node.right;

        node.right = rightChild.left;
        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }

        rightChild.left = node;
        node.parent = rightChild;

        replaceParentsChild(parent, node, rightChild);
    }

    private void replaceParentsChild(Node parent, Node oldChild, Node newChild) {
        if (parent == null) {
            root = newChild;
        } else if (parent.left == oldChild) {
            parent.left = newChild;
        } else if (parent.right == oldChild) {
            parent.right = newChild;
        } else {
            throw new IllegalStateException("Node is not a child of its parent");
        }

        if (newChild != null) {
            newChild.parent = parent;
        }
    }

    // -- For toString() -----------------------------------------------------------------------------

    @Override
    protected void appendNodeToString(Node node, StringBuilder builder) {
        builder.append(node.data).append(node.color == RED ? "[R]" : "[B]");
    }


}