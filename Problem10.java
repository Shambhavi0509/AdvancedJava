import java.util.*;

public class Problem10 {

    // Simulated Database (L3)
    private static HashMap<String, String> database = new HashMap<>();

    // L2 Cache (SSD simulated)
    private static HashMap<String, String> L2Cache = new HashMap<>();

    // L1 Cache (LRU)
    private static final int L1_CAPACITY = 3;

    private static LinkedHashMap<String, String> L1Cache =
            new LinkedHashMap<>(16, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                    return size() > L1_CAPACITY;
                }
            };

    private static int L1Hits = 0;
    private static int L2Hits = 0;
    private static int L3Hits = 0;

    // Fetch video
    public static String getVideo(String videoId) {

        long start = System.nanoTime();

        // L1 Check
        if (L1Cache.containsKey(videoId)) {
            L1Hits++;
            System.out.println("L1 HIT");
            return L1Cache.get(videoId);
        }

        // L2 Check
        if (L2Cache.containsKey(videoId)) {
            L2Hits++;
            System.out.println("L2 HIT → Promoting to L1");
            String data = L2Cache.get(videoId);
            L1Cache.put(videoId, data);
            return data;
        }

        // L3 Database
        if (database.containsKey(videoId)) {
            L3Hits++;
            System.out.println("L3 HIT → Adding to L2");
            String data = database.get(videoId);
            L2Cache.put(videoId, data);
            return data;
        }

        return "Video Not Found";
    }

    public static void getStats() {
        int total = L1Hits + L2Hits + L3Hits;

        System.out.println("\nCache Statistics:");
        System.out.println("L1 Hits: " + L1Hits);
        System.out.println("L2 Hits: " + L2Hits);
        System.out.println("L3 Hits: " + L3Hits);

        double hitRate = total == 0 ? 0 :
                ((L1Hits + L2Hits) * 100.0 / total);

        System.out.println("Overall Cache Hit Rate: " +
                String.format("%.2f", hitRate) + "%");
    }

    public static void main(String[] args) {

        // Populate database
        database.put("video1", "Video Data 1");
        database.put("video2", "Video Data 2");
        database.put("video3", "Video Data 3");
        database.put("video4", "Video Data 4");

        System.out.println(getVideo("video1"));
        System.out.println(getVideo("video1"));
        System.out.println(getVideo("video2"));
        System.out.println(getVideo("video2"));
        System.out.println(getVideo("video3"));
        System.out.println(getVideo("video4"));
        System.out.println(getVideo("video3"));

        getStats();
    }
}