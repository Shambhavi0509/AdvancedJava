import java.util.*;

public class InventoryManager {

    // ProductId -> Stock Count
    private static HashMap<String, Integer> inventory = new HashMap<>();

    // ProductId -> Waiting List (FIFO)
    private static HashMap<String, LinkedList<Integer>> waitingList = new HashMap<>();

    // Initialize product
    public static void addProduct(String productId, int stock) {
        inventory.put(productId, stock);
        waitingList.put(productId, new LinkedList<>());
    }

    // Check stock in O(1)
    public static int checkStock(String productId) {
        return inventory.getOrDefault(productId, 0);
    }

    // Synchronized purchase method (thread-safe simulation)
    public synchronized static String purchaseItem(String productId, int userId) {

        int stock = inventory.getOrDefault(productId, 0);

        if (stock > 0) {
            inventory.put(productId, stock - 1);
            return "Success, remaining stock: " + (stock - 1);
        } else {
            waitingList.get(productId).add(userId);
            return "Out of stock. Added to waiting list. Position: " +
                    waitingList.get(productId).size();
        }
    }

    public static void main(String[] args) {

        addProduct("IPHONE15_256GB", 3);

        System.out.println("Stock: " + checkStock("IPHONE15_256GB"));

        System.out.println(purchaseItem("IPHONE15_256GB", 101));
        System.out.println(purchaseItem("IPHONE15_256GB", 102));
        System.out.println(purchaseItem("IPHONE15_256GB", 103));
        System.out.println(purchaseItem("IPHONE15_256GB", 104));

        System.out.println("Final Stock: " + checkStock("IPHONE15_256GB"));
    }
}
