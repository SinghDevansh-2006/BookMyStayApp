import java.util.*;

class RoomInventory {

    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        roomAvailability.put("Single", 5);
        roomAvailability.put("Double", 3);
        roomAvailability.put("Suite", 2);
    }

    public int getAvailableRooms(String roomType) {
        return roomAvailability.getOrDefault(roomType, 0);
    }

    public void incrementRoom(String roomType) {
        int count = roomAvailability.getOrDefault(roomType, 0);
        roomAvailability.put(roomType, count + 1);
    }
}

class CancellationService {

    private Stack<String> releasedRoomIds;
    private Map<String, String> reservationRoomTypeMap;

    public CancellationService() {
        releasedRoomIds = new Stack<>();
        reservationRoomTypeMap = new HashMap<>();
    }

    public void registerBooking(String reservationId, String roomType) {
        reservationRoomTypeMap.put(reservationId, roomType);
    }

    public void cancelBooking(String reservationId, RoomInventory inventory) {

        if (!reservationRoomTypeMap.containsKey(reservationId)) {
            System.out.println("Cancellation failed: Reservation not found.");
            return;
        }

        String roomType = reservationRoomTypeMap.get(reservationId);

        inventory.incrementRoom(roomType);

        releasedRoomIds.push(reservationId);

        reservationRoomTypeMap.remove(reservationId);

        System.out.println("Booking cancelled successfully. Inventory restored for room type: " + roomType);
    }

    public void displayRollbackHistory() {

        System.out.println("\nRollback History (Most Recent First):");

        for (String id : releasedRoomIds) {
            System.out.println("Released Reservation ID: " + id);
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Booking Cancellation");

        RoomInventory inventory = new RoomInventory();

        CancellationService cancellationService = new CancellationService();

        String reservationId = "Single-1";
        String roomType = "Single";

        cancellationService.registerBooking(reservationId, roomType);

        cancellationService.cancelBooking(reservationId, inventory);

        cancellationService.displayRollbackHistory();

        System.out.println("\nUpdated Single Room Availability: " +
                inventory.getAvailableRooms("Single"));
    }
}