import java.util.*;

public class UsernameSystem { {

    // Username -> userId mapping
    private static HashMap<String, Integer> usernameMap = new HashMap<>();

    // Username attempt frequency tracking
    private static HashMap<String, Integer> attemptFrequency = new HashMap<>();

    // Check availability in O(1)
    public static boolean checkAvailability(String username) {
        attemptFrequency.put(username,
                attemptFrequency.getOrDefault(username, 0) + 1);

        return !usernameMap.containsKey(username);
    }

    // Suggest alternatives if taken
    public static List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();

        if (!usernameMap.containsKey(username)) {
            suggestions.add(username);
            return suggestions;
        }

        for (int i = 1; i <= 3; i++) {
            String suggestion = username + i;
            if (!usernameMap.containsKey(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        suggestions.add(username.replace("_", "."));

        return suggestions;
    }

    // Get most attempted username
    public static String getMostAttempted() {
        String mostAttempted = null;
        int max = 0;

        for (Map.Entry<String, Integer> entry : attemptFrequency.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                mostAttempted = entry.getKey();
            }
        }

        return mostAttempted + " (" + max + " attempts)";
    }

    public static void main(String[] args) {

        // Preload some existing users
        usernameMap.put("john_doe", 101);
        usernameMap.put("admin", 1);
        usernameMap.put("user123", 102);

        System.out.println("Check john_doe: " + checkAvailability("john_doe"));
        System.out.println("Check jane_smith: " + checkAvailability("jane_smith"));

        System.out.println("Suggestions for john_doe: " +
                suggestAlternatives("john_doe"));

        // Simulate multiple attempts
        checkAvailability("admin");
        checkAvailability("admin");
        checkAvailability("admin");

        System.out.println("Most Attempted: " + getMostAttempted());
    }
}
