import java.util.*;

class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
    }

    public Reservation getNextRequest() {
        return requestQueue.poll();
    }

    public boolean hasPendingRequests() {
        return !requestQueue.isEmpty();
    }
}

class RoomInventory {

    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        roomAvailability.put("Single", 5);
        roomAvailability.put("Double", 3);
        roomAvailability.put("Suite", 2);
    }

    public int getAvailability(String roomType) {
        return roomAvailability.getOrDefault(roomType, 0);
    }

    public void decrementAvailability(String roomType) {
        int count = roomAvailability.get(roomType);
        roomAvailability.put(roomType, count - 1);
    }
}

class RoomAllocationService {

    private Set<String> allocatedRoomIds;
    private Map<String, Set<String>> assignedRoomsByType;

    public RoomAllocationService() {
        allocatedRoomIds = new HashSet<>();
        assignedRoomsByType = new HashMap<>();
    }

    public void allocateRoom(Reservation reservation, RoomInventory inventory) {

        String roomType = reservation.getRoomType();

        if (inventory.getAvailability(roomType) <= 0) {
            System.out.println("No rooms available for " + reservation.getGuestName());
            return;
        }

        String roomId = generateRoomId(roomType);

        allocatedRoomIds.add(roomId);

        assignedRoomsByType
                .computeIfAbsent(roomType, k -> new HashSet<>())
                .add(roomId);

        inventory.decrementAvailability(roomType);

        System.out.println("Booking confirmed for Guest: " +
                reservation.getGuestName() +
                ", Room ID: " +
                roomId);
    }

    private String generateRoomId(String roomType) {

        int counter = 1;
        String roomId;

        do {
            roomId = roomType + "-" + counter;
            counter++;
        } while (allocatedRoomIds.contains(roomId));

        return roomId;
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Room Allocation Processing");

        BookingRequestQueue queue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService allocationService = new RoomAllocationService();

        Reservation r1 = new Reservation("Abhi", "Single");
        Reservation r2 = new Reservation("Subha", "Single");
        Reservation r3 = new Reservation("Vanmathi", "Suite");

        queue.addRequest(r1);
        queue.addRequest(r2);
        queue.addRequest(r3);

        while (queue.hasPendingRequests()) {

            Reservation request = queue.getNextRequest();

            allocationService.allocateRoom(request, inventory);
        }
    }
}