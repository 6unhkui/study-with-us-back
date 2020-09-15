package switus.user.back.studywithus.common.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import switus.user.back.studywithus.dto.common.CommonResponse;

import javax.validation.ConstraintViolation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Setter @Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse extends CommonResponse<Object> {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();
    private String debugMessage;
    private List<ErrorDetail> requestErrorDetails;

    public ErrorResponse() {
        super(false);
    }

    public ErrorResponse(HttpStatus status){
        super(false, status);
    }

    public ErrorResponse(HttpStatus status, String message) {
        super(false, status, message);
    }

    public ErrorResponse(HttpStatus status, Throwable ex) {
        super(false, status, "Unexpected error");
        this.debugMessage = ex.getLocalizedMessage();
    }

    public ErrorResponse(HttpStatus status, String message, Throwable ex) {
        super(false, status, message);
        this.debugMessage = ex.getLocalizedMessage();
    }


    private void addRequestErrorDetail(ErrorDetail errorDetail) {
        if (requestErrorDetails == null) {
            requestErrorDetails = new ArrayList<>();
        }
        requestErrorDetails.add(errorDetail);
    }

    private void addValidationError(String object, String field, Object rejectedValue, String message) {
        addRequestErrorDetail(new ValidationError(object, field, rejectedValue, message));
    }

    private void addValidationError(String object, String message) {
        addRequestErrorDetail(new ValidationError(object, message));
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


    abstract class ErrorDetail {}

    @Data
    @EqualsAndHashCode(callSuper = false)
    @AllArgsConstructor
    class ValidationError extends ErrorDetail {
        private String object;
        private String field;
        private Object rejectedValue;
        private String message;

        ValidationError(String object, String message) {
            this.object = object;
            this.message = message;
        }
    }
}


