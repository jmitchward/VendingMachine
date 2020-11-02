package machineService;

public class NoItemInventoryException extends Exception {
    NoItemInventoryException(String message) {
        super(message);
    }

    NoItemInventoryException(String message, Exception e) {
        super(message, e);
    }
}
