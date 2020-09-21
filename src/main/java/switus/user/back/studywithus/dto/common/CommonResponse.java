package switus.user.back.studywithus.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse<T> {
    private boolean success;
    private int status;
    private String message;
    private T data;

    public CommonResponse(boolean success){
        this.success = success;
    }

    public CommonResponse(boolean success, HttpStatus status, String message){
        this.success = success;
        this.status = status.value();
        this.message = message;
    }

    public CommonResponse(boolean success, HttpStatus status){
        this.success = success;
        this.status = status.value();
    }


    public static CommonResponse success(){
        return new CommonResponse(true, HttpStatus.OK);
    }

    public static <T> CommonResponse<T> success(T data){
        CommonResponse<T> response = new CommonResponse<T>(true, HttpStatus.OK);
        response.setData(data);
        return response;
    }

}
