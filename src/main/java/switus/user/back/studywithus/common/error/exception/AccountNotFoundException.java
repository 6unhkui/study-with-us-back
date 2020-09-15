package switus.user.back.studywithus.common.error.exception;

public class AccountNotFoundException extends CommonRuntimeException {
    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(){
        super("Account does not exist.");
    };
}
