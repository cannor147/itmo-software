public class Node<K, V> {
    private K key;
    private V value;
    private Node<K, V> next;
    private Node<K, V> prev;

    public Node(K key, V value, Node<K, V> prev, Node<K, V> next) {
        this.key = key;
        this.value = value;
        this.prev = prev;
        this.next = next;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public Node<K, V> getNext() {
        return next;
    }

    public Node<K, V> getPrev() {
        return prev;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public void setNext(Node<K, V> next) {
        this.next = next;
    }

    public void setPrev(Node<K, V> prev) {
        this.prev = prev;
    }
}
