package machineService;

public class InsufficientFundsException extends Exception {

    InsufficientFundsException(String message) {
        super(message);
    }

    InsufficientFundsException(String message, Exception e){
        super(message, e);
    }
}
