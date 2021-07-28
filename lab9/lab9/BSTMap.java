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
        } else {
            keys.add(p.key);
            keySetHelper(keys, p.left);
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
            p = removeNode(p);
        } else if (compare > 0) {
            p.right = removeHelper(key, p.right);
        } else {
            p.left = removeHelper(key, p.left);
        }
        return p;
    }

    /** Remove the given node and keep the remaining tree structure. */
    private Node removeNode(Node p) {
        if (p == null) {
            return null;
        }
        if (p.left == null && p.right == null) {
            p = null;     // This is a leaf node
        } else if (p.left == null) {
            p = p.right;
        } else if (p.right == null) {
            p = p.left;
        } else {
            // Handle the case when the node has two branches.
            Node minNodeR = min(p.right);  // find the min of the right subtree
            minNodeR.left = p.left;
            minNodeR.right= p.right;
            p = minNodeR;
        }
        return p;
    }

    /** Return the minimum node of the tree. */
    private Node min(Node p) {
        if (p == null) {
            return null;
        } else if (p.left == null && p.right == null) {
            return p;
        } else if (p.left == null) {
            return min(p.right);
        } else {
            return min(p.left);
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
            removeHelper(key, root);
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
