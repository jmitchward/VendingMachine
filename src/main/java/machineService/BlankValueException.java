package machineService;

public class BlankValueException extends Exception {
    public BlankValueException(String message) {
        super(message);
    }

    public BlankValueException(String message, Exception e){
        super(message, e);
    }
}
