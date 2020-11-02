package machineService;

public class RepeatIdException extends Exception {
    RepeatIdException(String message) {
        super(message);
    }
    RepeatIdException(String message, Exception e){
        super(message, e);
    }
}
