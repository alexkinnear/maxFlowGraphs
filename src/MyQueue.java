// Implement a Linked List as a Queue (First In First Out)
public class MyQueue<E> {
    private Node<E> first, last;

    private static class Node<E> {
        private E data;

        private Node<E> back;

        public Node(E key) {
            data = key;
        }
    }

    // add to the Queue
    public void enqueue(E key) {
        Node<E> newKey = new Node<E>(key);
        if (first == null) {
            first = newKey;
        }
        else {
            if (first.back == null) {
                first.back = newKey;
            }
            else {
                last.back = newKey;
            }
            last = newKey;
        }
    }

    // Retrieve value from the queue
    public E dequeue() {
        E output = first.data;
        first = first.back;
        return output;
    }

    public boolean isEmpty() {
        return first == null;
    }
}
