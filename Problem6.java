import java.util.*;

public class TokenBucketRateLimiter {

    // TokenBucket class
    static class TokenBucket {
        private int tokens;
        private final int maxTokens;
        private final long refillIntervalMillis;
        private long lastRefillTime;

        public TokenBucket(int maxTokens, long refillIntervalMillis) {
            this.maxTokens = maxTokens;
            this.refillIntervalMillis = refillIntervalMillis;
            this.tokens = maxTokens;
            this.lastRefillTime = System.currentTimeMillis();
        }

        private void refill() {
            long now = System.currentTimeMillis();
            long timePassed = now - lastRefillTime;

            if (timePassed > refillIntervalMillis) {
                tokens = maxTokens;
                lastRefillTime = now;
            }
        }

        public synchronized boolean allowRequest() {
            refill();

            if (tokens > 0) {
                tokens--;
                return true;
            }
            return false;
        }

        public int getRemainingTokens() {
            refill();
            return tokens;
        }
    }

    // clientId -> TokenBucket
    private static HashMap<String, TokenBucket> clientBuckets = new HashMap<>();

    // Rate limit check
    public static String checkRateLimit(String clientId) {

        clientBuckets.putIfAbsent(clientId,
                new TokenBucket(5, 10000)); // 5 requests per 10 seconds (demo)

        TokenBucket bucket = clientBuckets.get(clientId);

        if (bucket.allowRequest()) {
            return "Allowed (" + bucket.getRemainingTokens() + " remaining)";
        } else {
            return "Denied (Rate limit exceeded)";
        }
    }

    public static void main(String[] args) throws InterruptedException {

        String client = "abc123";

        for (int i = 1; i <= 7; i++) {
            System.out.println("Request " + i + ": " +
                    checkRateLimit(client));
        }

        System.out.println("\nWaiting for refill...");
        Thread.sleep(11000);

        System.out.println("After refill: " +
                checkRateLimit(client));
    }
}
