public abstract class AbstractLruCache<K, V> implements LruCache<K, V> {
    private int capacity;

    public AbstractLruCache(int capacity) {
        assert capacity > 0;
        this.capacity = capacity;
    }

    @Override
    public V get(K key) {
        assert key != null;
        V result = internalGet(key);
        internalAssert();
        return result;
    }

    @Override
    public void put(K key, V value) {
        assert key != null;
        assert value != null;

        int startSize = size();
        internalPut(key, value);
        int endSize = size();

        assert get(key) == value;
        assert startSize + 1 == endSize || endSize == capacity;
        internalAssert();
    }

    @Override
    public int size() {
        int size = internalSize();

        assert size <= capacity;
        internalAssert();

        return size;
    }

    @Override
    public int capacity() {
        return capacity;
    }

    @Override
    public void changeCapacity(int capacity) {
        assert capacity >= Math.max(size(), 1);
        this.capacity = capacity;
        internalAssert();
    }

    protected abstract V internalGet(K key);

    protected abstract void internalPut(K key, V value);

    protected abstract int internalSize();

    protected abstract void internalAssert();
}
