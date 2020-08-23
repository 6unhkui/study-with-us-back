package switus.user.back.studywithus.common.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import switus.user.back.studywithus.common.error.exception.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.time.format.DateTimeParseException;


@Slf4j
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";
        return buildResponseEntity(new RestRequestError(HttpStatus.BAD_REQUEST, error, ex));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        return buildResponseEntity(new RestRequestError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        RestRequestError restRequestError = new RestRequestError(HttpStatus.BAD_REQUEST);
        restRequestError.setMessage("Validation error");
        restRequestError.addValidationErrors(ex.getBindingResult().getFieldErrors());
        restRequestError.addValidationError(ex.getBindingResult().getGlobalErrors());
        return buildResponseEntity(restRequestError);
    }

    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(
            javax.validation.ConstraintViolationException ex) {
        RestRequestError restRequestError = new RestRequestError(HttpStatus.BAD_REQUEST);
        restRequestError.setMessage("Validation error");
        restRequestError.addValidationErrors(ex.getConstraintViolations());
        return buildResponseEntity(restRequestError);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFound(HttpServletRequest request, Exception ex) {
        RestRequestError restRequestError = new RestRequestError(HttpStatus.NOT_FOUND);
        restRequestError.setMessage(ex.getMessage());
        return buildResponseEntity(restRequestError);
    }

//    @ExceptionHandler(ResourceConflictException.class)
//    protected ResponseEntity<Object> handleResourceConflict(
//            ResourceConflictException ex) {
//        RestRequestError restRequestError = new RestRequestError(HttpStatus.CONFLICT);
//        restRequestError.setMessage(ex.getMessage());
//        return buildResponseEntity(restRequestError);
//    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequest(
            BadRequestException ex) {
        RestRequestError restRequestError = new RestRequestError(HttpStatus.BAD_REQUEST);
        restRequestError.setMessage(ex.getMessage());
        return buildResponseEntity(restRequestError);
    }

    @ExceptionHandler(InternalServerException.class)
    protected ResponseEntity<Object> handleInternalServerError(
            InternalServerException ex) {
        RestRequestError restRequestError = new RestRequestError(HttpStatus.INTERNAL_SERVER_ERROR);
        restRequestError.setMessage(ex.getMessage());
        return buildResponseEntity(restRequestError);
    }

    @ExceptionHandler(RestClientException.class)
    protected ResponseEntity<Object> handleRestClientException(
            RestClientException ex) {
        RestRequestError restRequestError = new RestRequestError(HttpStatus.INTERNAL_SERVER_ERROR);
        restRequestError.setMessage(ex.getMessage());
        return buildResponseEntity(restRequestError);
    }

    @ExceptionHandler(InvalidTokenException.class)
    protected ResponseEntity<Object> handleInvalidToken(
            InvalidTokenException ex) {
        RestRequestError restRequestError = new RestRequestError(HttpStatus.BAD_REQUEST);
        restRequestError.setMessage(ex.getMessage());
        return buildResponseEntity(restRequestError);
    }

//    @ExceptionHandler(InvalidFileAccessException.class)
//    protected ResponseEntity<Object> handleInvalidFileAccess(
//            InvalidFileAccessException ex) {
//        RestRequestError restRequestError = new RestRequestError(HttpStatus.BAD_REQUEST);
//        restRequestError.setMessage(ex.getMessage());
//        return buildResponseEntity(restRequestError);
//    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(
            IllegalArgumentException ex) {
        RestRequestError restRequestError = new RestRequestError(HttpStatus.BAD_REQUEST);
        restRequestError.setMessage(ex.getMessage());
        return buildResponseEntity(restRequestError);
    }

    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<Object> handleUnauthorized(
            UnauthorizedException ex) {
        RestRequestError restRequestError = new RestRequestError(HttpStatus.UNAUTHORIZED);
        restRequestError.setMessage(ex.getMessage());
        return buildResponseEntity(restRequestError);
    }

    @ExceptionHandler(ForbiddenException.class)
    protected ResponseEntity<Object> handleForbidden(
            ForbiddenException ex) {
        RestRequestError restRequestError = new RestRequestError(HttpStatus.FORBIDDEN);
        restRequestError.setMessage(ex.getMessage());
        return buildResponseEntity(restRequestError);
    }

    @ExceptionHandler(NoContentException.class)
    protected ResponseEntity<Object> handleNoContent(
            NoContentException ex) {
        RestRequestError restRequestError = new RestRequestError(HttpStatus.NO_CONTENT);
        restRequestError.setMessage(ex.getMessage());
        return buildResponseEntity(restRequestError);
    }

    @ExceptionHandler(DateTimeParseException.class)
    protected ResponseEntity<Object> handleDateTimeParseException(
            DateTimeParseException ex) {
        RestRequestError restRequestError = new RestRequestError(HttpStatus.BAD_REQUEST);
        restRequestError.setMessage(ex.getMessage());
        return buildResponseEntity(restRequestError);
    }

//    @ExceptionHandler(InvalidFileTypeException.class)
//    protected ResponseEntity<Object> handleInvalidFileTypeException(
//            InvalidFileTypeException ex) {
//        RestRequestError restRequestError = new RestRequestError(HttpStatus.BAD_REQUEST);
//        restRequestError.setMessage(ex.getMessage());
//        return buildResponseEntity(restRequestError);
//    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        log.error("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
        String error = "Malformed JSON request";
        return buildResponseEntity(new RestRequestError(HttpStatus.BAD_REQUEST, error, ex));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Error writing JSON output";
        return buildResponseEntity(new RestRequestError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        RestRequestError restRequestError = new RestRequestError(HttpStatus.BAD_REQUEST);
        restRequestError.setMessage(String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
        restRequestError.setDebugMessage(ex.getMessage());
        return buildResponseEntity(restRequestError);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        return buildResponseEntity(new RestRequestError(HttpStatus.NOT_FOUND, ex));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex,
                                                                  WebRequest request) {
        if (ex.getCause() instanceof ConstraintViolationException) {
            return buildResponseEntity(new RestRequestError(HttpStatus.CONFLICT, "Database error", ex.getCause()));
        }
        return buildResponseEntity(new RestRequestError(HttpStatus.INTERNAL_SERVER_ERROR, ex));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                      WebRequest request) {
        RestRequestError restRequestError = new RestRequestError(HttpStatus.BAD_REQUEST);
        restRequestError.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'", ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
        restRequestError.setDebugMessage(ex.getMessage());
        return buildResponseEntity(restRequestError);
    }

    private ResponseEntity<Object> buildResponseEntity(RestRequestError restRequestError) {
        return new ResponseEntity<>(restRequestError, restRequestError.getStatus());
    }

}
