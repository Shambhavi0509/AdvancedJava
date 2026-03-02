import java.util.*;

public class Problem9 {

    static class Transaction {
        int id;
        int amount;
        String merchant;

        Transaction(int id, int amount, String merchant) {
            this.id = id;
            this.amount = amount;
            this.merchant = merchant;
        }

        public String toString() {
            return "(id:" + id + ", amount:" + amount + ")";
        }
    }

    // Classic Two-Sum
    public static List<String> findTwoSum(List<Transaction> transactions, int target) {
        HashMap<Integer, Transaction> map = new HashMap<>();
        List<String> result = new ArrayList<>();

        for (Transaction t : transactions) {
            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                result.add(map.get(complement) + " + " + t);
            }

            map.put(t.amount, t);
        }

        return result;
    }

    // Duplicate detection (same amount + same merchant)
    public static void detectDuplicates(List<Transaction> transactions) {
        HashMap<String, List<Integer>> duplicateMap = new HashMap<>();

        for (Transaction t : transactions) {
            String key = t.amount + "_" + t.merchant;
            duplicateMap.putIfAbsent(key, new ArrayList<>());
            duplicateMap.get(key).add(t.id);
        }

        System.out.println("\nDuplicate Transactions:");
        for (Map.Entry<String, List<Integer>> entry : duplicateMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                System.out.println("Duplicate: " + entry.getKey() +
                        " -> IDs " + entry.getValue());
            }
        }
    }

    // Simple 3-Sum (K-Sum demo)
    public static void findThreeSum(List<Transaction> transactions, int target) {
        System.out.println("\nThree-Sum Results:");

        int n = transactions.size();

        for (int i = 0; i < n; i++) {
            HashSet<Integer> seen = new HashSet<>();

            for (int j = i + 1; j < n; j++) {
                int complement = target -
                        transactions.get(i).amount -
                        transactions.get(j).amount;

                if (seen.contains(complement)) {
                    System.out.println(transactions.get(i) + ", " +
                            transactions.get(j) +
                            " with complement " + complement);
                }

                seen.add(transactions.get(j).amount);
            }
        }
    }

    public static void main(String[] args) {

        List<Transaction> transactions = Arrays.asList(
                new Transaction(1, 500, "StoreA"),
                new Transaction(2, 300, "StoreB"),
                new Transaction(3, 200, "StoreC"),
                new Transaction(4, 500, "StoreA"),
                new Transaction(5, 100, "StoreD")
        );

        System.out.println("Two-Sum (Target 500):");
        List<String> pairs = findTwoSum(transactions, 500);
        for (String pair : pairs) {
            System.out.println(pair);
        }

        detectDuplicates(transactions);

        findThreeSum(transactions, 1000);
    }
}