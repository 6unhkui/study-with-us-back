package switus.user.back.studywithus.common.error.exception;

public class BadRequestException extends CommonRuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(Class clazz, String... searchParamsMap) {
        super(generateMessage(
                clazz.getSimpleName(),
                " was bad parameters ",
                toMap(String.class, String.class, (Object) searchParamsMap)));
    }
}
