package switus.user.back.studywithus.common.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

@Data
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.CUSTOM, property = "error", visible = true)
@JsonTypeIdResolver(CustomClassNameResolver.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestRequestError {

    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;
    private List<RequestErrorDetail> requestErrorDetails;

    private RestRequestError() {
        timestamp = LocalDateTime.now();
    }

    public RestRequestError(HttpStatus status) {
        this();
        this.status = status;
    }

    public RestRequestError(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    public RestRequestError(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    private void addRequestErrorDetail(RequestErrorDetail requestErrorDetail) {
        if (requestErrorDetails == null) {
            requestErrorDetails = new ArrayList<>();
        }
        requestErrorDetails.add(requestErrorDetail);
    }

    private void addValidationError(String object, String field, Object rejectedValue, String message) {
        addRequestErrorDetail(new RequestValidationError(object, field, rejectedValue, message));
    }

    private void addValidationError(String object, String message) {
        addRequestErrorDetail(new RequestValidationError(object, message));
    }

    private void addValidationError(FieldError fieldError) {
        this.addValidationError(
                fieldError.getObjectName(),
                fieldError.getField(),
                fieldError.getRejectedValue(),
                fieldError.getDefaultMessage());
    }

    void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(this::addValidationError);
    }

    private void addValidationError(ObjectError objectError) {
        this.addValidationError(
                objectError.getObjectName(),
                objectError.getDefaultMessage());
    }

    void addValidationError(List<ObjectError> globalErrors) {
        globalErrors.forEach(this::addValidationError);
    }

    private void addValidationError(ConstraintViolation<?> cv) {
        this.addValidationError(
                cv.getRootBeanClass().getSimpleName(),
                ((PathImpl) cv.getPropertyPath()).getLeafNode().asString(),
                cv.getInvalidValue(),
                cv.getMessage());
    }

    void addValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
        constraintViolations.forEach(this::addValidationError);
    }

    abstract class RequestErrorDetail { }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @AllArgsConstructor
    class RequestValidationError extends RequestErrorDetail {
        private String object;
        private String field;
        private Object rejectedValue;
        private String message;

        RequestValidationError(String object, String message) {
            this.object = object;
            this.message = message;
        }
    }

}

class CustomClassNameResolver extends TypeIdResolverBase {

    @Override
    public String idFromValue(Object value) {
        String className = value.getClass().getSimpleName();
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        return idFromValue(value);
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }

}

