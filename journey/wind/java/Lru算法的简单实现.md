public class LruLinkedHashMap extends LinkedHashMap {

    private final int maxCapacity;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private final Lock lock = new ReentrantLock();

    public LruLinkedHashMap(int maxCapacity) {
        super(maxCapacity,DEFAULT_LOAD_FACTOR,true);
        this.maxCapacity = maxCapacity;
    }

    @Override
    protected boolean removeEldestEntry(java.util.Map.Entry entry) {
        System.out.println("***");
        return size()>maxCapacity;
    }

    @Override
    public void clear() {
        try {
            lock.lock();
            super.clear();
        }finally{
            lock.unlock();
        }
    }

    @Override
    public V get(Object key) {
        try {
            lock.lock();
            return super.get(key);
        }finally{
            lock.unlock();
        }
    }

    @Override
    public V put(K key, V value) {
        try {
            lock.lock();
            return super.put(key, value);
        }finally{
            lock.unlock();
        }
    }