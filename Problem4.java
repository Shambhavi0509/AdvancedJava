import java.util.*;

public class DocumentSimilarityAnalyzer {

    // n-gram -> set of document IDs
    private static HashMap<String, Set<String>> ngramIndex = new HashMap<>();

    private static final int N = 5; // 5-gram

    // Generate n-grams
    private static List<String> generateNGrams(String text) {
        List<String> ngrams = new ArrayList<>();
        String[] words = text.toLowerCase().split("\\s+");

        for (int i = 0; i <= words.length - N; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < N; j++) {
                sb.append(words[i + j]).append(" ");
            }
            ngrams.add(sb.toString().trim());
        }

        return ngrams;
    }

    // Add document to index
    public static void indexDocument(String docId, String content) {
        List<String> ngrams = generateNGrams(content);

        for (String gram : ngrams) {
            ngramIndex.putIfAbsent(gram, new HashSet<>());
            ngramIndex.get(gram).add(docId);
        }

        System.out.println(docId + " indexed with " + ngrams.size() + " n-grams.");
    }

    // Analyze document similarity
    public static void analyzeDocument(String docId, String content) {
        List<String> ngrams = generateNGrams(content);

        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String gram : ngrams) {
            if (ngramIndex.containsKey(gram)) {
                for (String existingDoc : ngramIndex.get(gram)) {
                    if (!existingDoc.equals(docId)) {
                        matchCount.put(existingDoc,
                                matchCount.getOrDefault(existingDoc, 0) + 1);
                    }
                }
            }
        }

        System.out.println("\nAnalysis for " + docId + ":");

        for (Map.Entry<String, Integer> entry : matchCount.entrySet()) {
            double similarity = (entry.getValue() * 100.0) / ngrams.size();
            System.out.println("Matched with " + entry.getKey()
                    + " → " + entry.getValue()
                    + " matching n-grams"
                    + " | Similarity: "
                    + String.format("%.2f", similarity) + "%");
        }
    }

    public static void main(String[] args) {

        String doc1 = "data structures and algorithms are important for coding interviews and software development";
        String doc2 = "algorithms are important for coding interviews and competitive programming practice";
        String doc3 = "machine learning and artificial intelligence are trending fields in software development";

        indexDocument("doc1", doc1);
        indexDocument("doc2", doc2);
        indexDocument("doc3", doc3);

        analyzeDocument("new_doc",
                "algorithms are important for coding interviews and real world applications");
    }
}
