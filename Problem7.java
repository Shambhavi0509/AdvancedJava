import java.util.*;

public class Problem7 {

    // Global frequency storage
    private static HashMap<String, Integer> queryFrequency = new HashMap<>();

    // Trie Node
    static class TrieNode {
        HashMap<Character, TrieNode> children = new HashMap<>();
        boolean isEndOfWord = false;
    }

    private static TrieNode root = new TrieNode();

    // Insert query into Trie
    public static void insert(String query) {
        TrieNode current = root;

        for (char ch : query.toCharArray()) {
            current.children.putIfAbsent(ch, new TrieNode());
            current = current.children.get(ch);
        }

        current.isEndOfWord = true;

        queryFrequency.put(query,
                queryFrequency.getOrDefault(query, 0) + 1);
    }

    // Collect all words from prefix node
    private static void collectSuggestions(TrieNode node,
                                           String prefix,
                                           List<String> results) {

        if (node.isEndOfWord) {
            results.add(prefix);
        }

        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            collectSuggestions(entry.getValue(),
                    prefix + entry.getKey(),
                    results);
        }
    }

    // Search suggestions for prefix
    public static List<String> search(String prefix) {
        TrieNode current = root;

        for (char ch : prefix.toCharArray()) {
            if (!current.children.containsKey(ch)) {
                return new ArrayList<>();
            }
            current = current.children.get(ch);
        }

        List<String> results = new ArrayList<>();
        collectSuggestions(current, prefix, results);

        // Sort by frequency descending
        results.sort((a, b) ->
                queryFrequency.get(b) - queryFrequency.get(a));

        return results.size() > 10
                ? results.subList(0, 10)
                : results;
    }

    public static void main(String[] args) {

        insert("java tutorial");
        insert("javascript");
        insert("java download");
        insert("java tutorial");
        insert("java 21 features");
        insert("java tutorial");
        insert("java interview questions");
        insert("java download");

        System.out.println("Search results for 'jav':");
        List<String> suggestions = search("jav");

        for (String suggestion : suggestions) {
            System.out.println(suggestion +
                    " (" + queryFrequency.get(suggestion) + " searches)");
        }
    }
}