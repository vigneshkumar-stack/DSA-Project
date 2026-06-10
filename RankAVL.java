class Node {
    int key;
    int height;
    int size;
    Node left, right;

    Node(int key) {
        this.key = key;
        height = 1;
        size = 1;
    }
}

public class RankAVL {

    Node root;

    int height(Node n) {
        return (n == null) ? 0 : n.height;
    }

    int size(Node n) {
        return (n == null) ? 0 : n.size;
    }

    int max(int a, int b) {
        return (a > b) ? a : b;
    }

    Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = max(height(y.left), height(y.right)) + 1;
        x.height = max(height(x.left), height(x.right)) + 1;

        y.size = size(y.left) + size(y.right) + 1;
        x.size = size(x.left) + size(x.right) + 1;

        return x;
    }

    Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = max(height(x.left), height(x.right)) + 1;
        y.height = max(height(y.left), height(y.right)) + 1;

        x.size = size(x.left) + size(x.right) + 1;
        y.size = size(y.left) + size(y.right) + 1;

        return y;
    }

    int getBalance(Node n) {
        if (n == null)
            return 0;
        return height(n.left) - height(n.right);
    }

    Node insert(Node node, int key) {

        if (node == null)
            return new Node(key);

        if (key < node.key)
            node.left = insert(node.left, key);
        else if (key > node.key)
            node.right = insert(node.right, key);
        else
            return node;

        node.height = 1 + max(height(node.left),
                              height(node.right));

        node.size = 1 + size(node.left)
                      + size(node.right);

        int balance = getBalance(node);

        if (balance > 1 && key < node.left.key)
            return rightRotate(node);

        if (balance < -1 && key > node.right.key)
            return leftRotate(node);

        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    int kthElement(Node root, int k) {

        int leftSize = size(root.left);

        if (k == leftSize + 1)
            return root.key;

        if (k <= leftSize)
            return kthElement(root.left, k);

        return kthElement(root.right,
                          k - leftSize - 1);
    }

    void inorder(Node node) {
        if (node != null) {
            inorder(node.left);
            System.out.print(node.key + " ");
            inorder(node.right);
        }
    }

    public static void main(String[] args) {

        RankAVL tree = new RankAVL();

        int timestamps[] =
            {1001,1005,1003,1008,1012,
             1002,1007,1006,1004,1010};

        for (int x : timestamps)
            tree.root = tree.insert(tree.root, x);

        System.out.println("Inorder Traversal:");
        tree.inorder(tree.root);

        int k = 3;

        System.out.println("\n\n" +
            k + "-th most senior member is: " +
            tree.kthElement(tree.root, k));
    }
}