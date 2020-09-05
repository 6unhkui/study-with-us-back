package switus.user.back.studywithus.common.error.exception;

public class UserNotFoundException extends CommonRuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(){
        super("User does not exist.");
    };
}
