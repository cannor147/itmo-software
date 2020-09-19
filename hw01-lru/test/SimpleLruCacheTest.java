import org.junit.Assert;
import org.junit.Test;

public class SimpleLruCacheTest {
    @Test
    public void testNull() {
        LruCache<Integer, String> lruCache = new SimpleLruCache<>(4);
        Assert.assertEquals(0, lruCache.size());
        Assert.assertNull(lruCache.get(10));
    }

    @Test
    public void testOnly() {
        LruCache<Integer, String> lruCache = new SimpleLruCache<>(1);
        lruCache.put(1, "one");
        lruCache.put(2, "one else");
        Assert.assertEquals(1, lruCache.size());
        Assert.assertNull(lruCache.get(1));
    }

    @Test
    public void testLimit() {
        LruCache<Integer, String> lruCache = new SimpleLruCache<>(4);
        lruCache.put(1, "a");
        lruCache.put(2, "b");
        lruCache.put(3, "c");
        lruCache.put(4, "d");
        lruCache.put(5, "e");
        Assert.assertEquals(4, lruCache.capacity());
        Assert.assertEquals(4, lruCache.size());
        Assert.assertNull(lruCache.get(1));
    }

    @Test
    public void testUpdate() {
        LruCache<Integer, String> lruCache = new SimpleLruCache<>(2);
        lruCache.put(1, "a");
        lruCache.put(2, "b");
        lruCache.get(1);
        lruCache.put(3, "c");
        Assert.assertNotNull(lruCache.get(1));
    }

    @Test
    public void testChangeCapacity() {
        LruCache<Integer, String> lruCache = new SimpleLruCache<>(2);
        lruCache.put(1, "a");
        lruCache.put(2, "b");
        lruCache.put(3, "c");
        Assert.assertEquals(2, lruCache.size());

        lruCache.changeCapacity(3);
        lruCache.put(4, "d");
        Assert.assertEquals(3, lruCache.size());
    }

    @Test
    public void testChange() {
        LruCache<Integer, String> lruCache = new SimpleLruCache<>(2);
        lruCache.put(1, "a");
        lruCache.put(2, "b");
        lruCache.put(1, "русский текст");
        Assert.assertEquals("русский текст", lruCache.get(1));
    }
}
