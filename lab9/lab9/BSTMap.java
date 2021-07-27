package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        }
        if (key.compareTo(p.key) == 0) {
            return p.value;
        } else if (key.compareTo(p.key) > 0) {
            return getHelper(key, p.right);
        } else {
            return getHelper(key, p.left);
        }
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            p = new Node(key, value);
            size += 1;
        } else if (key.compareTo(p.key) > 0) {
            p.right = putHelper(key, value, p.right);
        } else if (key.compareTo(p.key) < 0) {
            p.left = putHelper(key, value, p.left);
        }
        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        root = putHelper(key, value, root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        keySetHelper(keys, root);
        return keys;
    }

    /** Returns a set of keys in the subtree rooted in p. */
    private void keySetHelper(Set<K> keys, Node p) {
        if (p == null) {
            return;
        }
        if (p.left != null) {
            keys.add(p.left.key);
            keySetHelper(keys, p.left);
        }
        if (p.right != null) {
            keys.add(p.right.key);
            keySetHelper(keys, p.right);
        }
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
        V value = get(key);
        if (value != null) {
            root = removeHelper(key, root);
            size -= 1;
        }
        return value;
    }

    /** Delete the node of KEY in the subtree rooted in P.
     * Return the root.
     */
    private Node removeHelper(K key, Node p) {
        if (p == null) {
            return null;
        }
        int compare = key.compareTo(p.key);
        if (compare == 0) {
            if (p.left == null && p.right == null) {
                // If the key is in a leaf, simply delete it.
                return null;
            } else if (p.left == null) {
                // If the key has no left branch, return the right branch.
                return p.right;
            } else if (p.right == null) {
                // If the key has no right branch, return the left branch.
                return p.left;
            } else {
                Node minRightP = min(p.right);
                p.key = minRightP.key;
                p.value = minRightP.value;
                removeMin(minRightP);
                return p;
            }
        } else if (compare > 0) {
            return p.right = removeHelper(key, p.right);
        } else {
            return p.left = removeHelper(key, p.left);
        }
    }

    /** Return the minimum node of the subtree. */
    private Node min(Node p) {
        if (p == null) {
            return null;
        } else if (p.right == null) {
            return p;
        } else {
            return min(p.right);
        }
    }

    /** Remove the minimum node.
     * This is used as a helper method for the remove
     * method, therefore no size change happens here. */
    private void removeMin(Node minNode) {
        if (minNode.right == null) {
            minNode = null;
        } else {
            minNode = minNode.right;
        }
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        V currentValue = get(key);
        if (currentValue.equals(value)) {
            root = removeHelper(key, root);
            size -= 1;
            return value;
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
