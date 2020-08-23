package switus.user.back.studywithus.common.error.exception;

public class ResourceNotFoundException extends CommonRuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(Class clazz, String... searchParamsMap) {
        super(ResourceNotFoundException.generateMessage(
                clazz.getSimpleName(),
                " was not found for parameters ",
                toMap(String.class, String.class, (Object) searchParamsMap)));
    }

}
