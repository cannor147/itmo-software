import java.util.HashMap;

public class SimpleLruCache<K, V> extends AbstractLruCache<K, V> {
    private final HashMap<K, Node<K, V>> cacheStorage;
    private Node<K, V> head;
    private Node<K, V> tail;

    public SimpleLruCache(int capacity) {
        super(capacity);

        this.cacheStorage = new HashMap<>();
        this.head = null;
        this.tail = null;
    }

    @Override
    protected V internalGet(K key) {
        update(key);
        return cacheStorage.containsKey(key) ? head.getValue() : null;
    }

    @Override
    protected void internalPut(K key, V value) {
        if (size() == capacity()) {
            overwrite(key, value);
        }
        if (cacheStorage.containsKey(key)) {
            update(key);
            head.setValue(value);
            return;
        }
        insert(key, value);
    }

    @Override
    protected int internalSize() {
        return cacheStorage.size();
    }

    @Override
    protected void internalAssert() {
        int listSize = 0;
        Node<K, V> node = head;
        while (node != null) {
            node = node.getNext();
            listSize++;
        }

        assert listSize == internalSize();
        if (listSize == 0) {
            assert head == null;
            assert tail == null;
        } else {
            assert head != null;
            assert tail != null;
        }
    }

    private void update(K key) {
        Node<K, V> node = cacheStorage.get(key);
        if (node == null || node == head) {
            return;
        }

        if (node == tail) {
            tail = node.getPrev();
        } else {
            node.getNext().setPrev(null);
        }
        node.getPrev().setNext(null);

        node.setNext(head);
        head.setPrev(node);
        head = node;
    }

    private void insert(K key, V value) {
        Node<K, V> node = new Node<>(key, value, null, head);
        if (tail == null) {
            tail = node;
        } else {
            head.setPrev(node);
            node.setNext(head);
        }
        head = node;
        cacheStorage.put(key, head);
    }

    private void overwrite(K key, V value) {
        cacheStorage.remove(tail.getKey());
        tail.setKey(key);
        tail.setValue(value);
        cacheStorage.put(key, tail);
    }
}
