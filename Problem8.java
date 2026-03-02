import java.util.*;

public class Problem8 {

    enum Status { EMPTY, OCCUPIED }

    static class ParkingSpot {
        String licensePlate;
        long entryTime;
        Status status;

        ParkingSpot() {
            this.status = Status.EMPTY;
        }
    }

    private static final int CAPACITY = 20; // demo size
    private static ParkingSpot[] table = new ParkingSpot[CAPACITY];
    private static int occupiedCount = 0;
    private static int totalProbes = 0;

    static {
        for (int i = 0; i < CAPACITY; i++) {
            table[i] = new ParkingSpot();
        }
    }

    // Custom hash function
    private static int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % CAPACITY;
    }

    // Park vehicle using linear probing
    public static void parkVehicle(String licensePlate) {
        int index = hash(licensePlate);
        int probes = 0;

        while (table[index].status == Status.OCCUPIED) {
            index = (index + 1) % CAPACITY;
            probes++;
        }

        table[index].licensePlate = licensePlate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].status = Status.OCCUPIED;

        occupiedCount++;
        totalProbes += probes;

        System.out.println("Parked " + licensePlate +
                " at spot #" + index +
                " (" + probes + " probes)");
    }

    // Exit vehicle
    public static void exitVehicle(String licensePlate) {
        int index = hash(licensePlate);

        while (table[index].status != Status.EMPTY) {
            if (table[index].licensePlate.equals(licensePlate)) {

                long durationMillis =
                        System.currentTimeMillis() - table[index].entryTime;

                double hours = durationMillis / (1000.0 * 60 * 60);
                double fee = hours * 10; // $10 per hour

                table[index] = new ParkingSpot();
                occupiedCount--;

                System.out.println("Vehicle " + licensePlate +
                        " exited. Fee: $" +
                        String.format("%.2f", fee));
                return;
            }
            index = (index + 1) % CAPACITY;
        }

        System.out.println("Vehicle not found.");
    }

    public static void getStatistics() {
        double occupancyRate =
                (occupiedCount * 100.0) / CAPACITY;

        double avgProbes =
                occupiedCount == 0 ? 0 :
                        (double) totalProbes / occupiedCount;

        System.out.println("\nStatistics:");
        System.out.println("Occupancy: " +
                String.format("%.2f", occupancyRate) + "%");
        System.out.println("Average Probes: " +
                String.format("%.2f", avgProbes));
    }

    public static void main(String[] args) throws InterruptedException {

        parkVehicle("ABC-1234");
        parkVehicle("ABC-1235");
        parkVehicle("XYZ-9999");

        Thread.sleep(2000);

        exitVehicle("ABC-1234");

        getStatistics();
    }
}