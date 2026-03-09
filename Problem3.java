import java.util.*;


public class DNSCacheSystem {

    // DNS Entry class
    static class DNSEntry {
        String ipAddress;
        long expiryTime;

        DNSEntry(String ipAddress, long ttlSeconds) {
            this.ipAddress = ipAddress;
            this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000);
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    // LRU Cache with max size 5 (for demo)
    private static final int MAX_CACHE_SIZE = 5;

    private static LinkedHashMap<String, DNSEntry> cache =
            new LinkedHashMap<>(16, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                    return size() > MAX_CACHE_SIZE;
                }
            };

    private static int hits = 0;
    private static int misses = 0;

    // Simulated upstream DNS lookup
    private static String queryUpstreamDNS(String domain) {
        return "192.168.1." + new Random().nextInt(255);
    }

    public static String resolve(String domain, long ttlSeconds) {

        long start = System.nanoTime();

        if (cache.containsKey(domain)) {
            DNSEntry entry = cache.get(domain);

            if (!entry.isExpired()) {
                hits++;
                long time = System.nanoTime() - start;
                System.out.println("Cache HIT (" + time + " ns)");
                return entry.ipAddress;
            } else {
                cache.remove(domain);
            }
        }

        misses++;
        String ip = queryUpstreamDNS(domain);
        cache.put(domain, new DNSEntry(ip, ttlSeconds));

        long time = System.nanoTime() - start;
        System.out.println("Cache MISS (" + time + " ns)");

        return ip;
    }

    public static void getStats() {
        int total = hits + misses;
        double hitRate = total == 0 ? 0 : (hits * 100.0 / total);

        System.out.println("\nCache Statistics:");
        System.out.println("Hits: " + hits);
        System.out.println("Misses: " + misses);
        System.out.println("Hit Rate: " + String.format("%.2f", hitRate) + "%");
        System.out.println("Cache Size: " + cache.size());
    }

    public static void main(String[] args) throws InterruptedException {

        System.out.println(resolve("google.com", 5));
        System.out.println(resolve("google.com", 5));

        Thread.sleep(6000); // expire TTL

        System.out.println(resolve("google.com", 5));

        System.out.println(resolve("openai.com", 5));
        System.out.println(resolve("github.com", 5));
        System.out.println(resolve("stackoverflow.com", 5));
        System.out.println(resolve("example.com", 5));
        System.out.println(resolve("extra.com", 5)); // triggers LRU eviction

        getStats();
    }
}
