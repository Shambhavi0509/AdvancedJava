import java.util.*;

public class Problem5 {

    // pageUrl -> total visit count
    private static HashMap<String, Integer> pageViews = new HashMap<>();

    // pageUrl -> unique users
    private static HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();

    // traffic source -> count
    private static HashMap<String, Integer> trafficSources = new HashMap<>();

    // Process incoming event
    public static void processEvent(String url, String userId, String source) {

        // Update total page views
        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);

        // Update unique visitors
        uniqueVisitors.putIfAbsent(url, new HashSet<>());
        uniqueVisitors.get(url).add(userId);

        // Update traffic sources
        trafficSources.put(source, trafficSources.getOrDefault(source, 0) + 1);
    }

    // Get Top 10 Pages
    public static void displayTopPages() {
        System.out.println("\nTop Pages:");

        pageViews.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(10)
                .forEach(entry -> {
                    String url = entry.getKey();
                    int total = entry.getValue();
                    int unique = uniqueVisitors.get(url).size();

                    System.out.println(url + " - " + total +
                            " views (" + unique + " unique)");
                });
    }

    // Display traffic sources
    public static void displayTrafficSources() {
        System.out.println("\nTraffic Sources:");

        int total = trafficSources.values().stream().mapToInt(i -> i).sum();

        for (Map.Entry<String, Integer> entry : trafficSources.entrySet()) {
            double percentage = (entry.getValue() * 100.0) / total;

            System.out.println(entry.getKey() + ": "
                    + String.format("%.2f", percentage) + "%");
        }
    }

    public static void main(String[] args) {

        // Simulated traffic
        processEvent("/article/breaking-news", "user1", "google");
        processEvent("/article/breaking-news", "user2", "facebook");
        processEvent("/sports/championship", "user3", "google");
        processEvent("/sports/championship", "user4", "direct");
        processEvent("/sports/championship", "user3", "google");
        processEvent("/tech/ai-trends", "user5", "direct");
        processEvent("/article/breaking-news", "user1", "google");

        displayTopPages();
        displayTrafficSources();
    }
}