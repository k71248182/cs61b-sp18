public class ArrayDeque<T> {

    private T[] items;
    private int size;
    private int start;
    private int end;

    /** Constructor */
    public ArrayDeque() {
        items = (T []) new Object[8];
        size = 0;
        start = 0;
        end = 0;
    }

    /** Helper function:
     * compute the index immediately before a given index. */
    private int prevIndex(int index) {
        if (index - 1 > 0) {
            return index - 1;
        }
        return index - 1 + items.length;
    }

    /** Helper function:
     * compute the index immediately after a given index. */
    private int nextIndex(int index) {
        if (index + 1 < items.length) {
            return index + 1;
        }
        return index + 1 - items.length;
    }

    /** Helper function:
     * return the correct array index given the deque index. */
    private int getArrayIndex(int dequeIndex) {
        if (start + dequeIndex < items.length) {
            return start + dequeIndex;
        }
        return start + dequeIndex - items.length;
    }

    /** Helper function:
     * Enlarge array size.
     * Resized array always starts from position zero */
    private void resize(int capacity) {
        T[] newItems = (T []) new Object[capacity];
        if (start + size < items.length) {
            System.arraycopy(items, start, newItems, 0, size);
        } else {
            System.arraycopy(items, start, newItems, 0, items.length - start);
            System.arraycopy(items, 0, newItems, items.length - start, end + 1);
        }
        items = newItems;
        start = 0;
        end = size - 1;
    }

    /** Helper function:
     * calculate the optimal array size based on deque length. */
    private void optimalCapacity() {
        if (size < items.length * 0.25) {
            int capacity = (int) Math.round(items.length * 0.5) + 1;
            resize(capacity);
        }
    }

    /** Add an item to the beginning of the deque. */
    public void addFirst(T item) {
        if (size > 0) {
            if (size == items.length) {
                resize(size * 2);
            }
            start = prevIndex(start);
        }
        items[start] = item;
        size += 1;
    }

    /** Add an item to the end of the deque. */
    public void addLast(T item) {
        if (size > 0) {
            if (size == items.length) {
                resize(size * 2);
            }
            end = nextIndex(end);
        }
        items[end] = item;
        size += 1;
    }

    /** Remove and return the item at the front of the deque. */
    public T removeFirst() {
        if (size == 0){
            return null;
        }
        T removedItem = items[start];
        items[start] = null;
        start = nextIndex(start);
        size -= 1;
        optimalCapacity();
        return removedItem;
    }

    /** Remove and return the item at the back of the deque. */
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T removedItem = items[end];
        items[end] = null;
        end = prevIndex(end);
        size -= 1;
        optimalCapacity();
        return removedItem;
    }

    /** Return true if deque is empty. */
    public  boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    /** Return the number of items in the deque. */
    public int size(){
        return size;
    }

    /** Print the items in the deque form the first to last. */
    public void printDeque() {
        int i = start;
        while (i != end) {
            System.out.print(items[i] + " ");
            i = nextIndex(i);
        }
        System.out.print(items[i] + "\n");
    }

    //* Get the item at the given index, where 0 is
    // the front, 1 is the next item, and so forth.
    // 1. Find the correct array index.
    // 2. return item value of that index. /
    public T get(int index) {
        int arrayIndex = getArrayIndex(index);
        return items[arrayIndex];
    }
}
