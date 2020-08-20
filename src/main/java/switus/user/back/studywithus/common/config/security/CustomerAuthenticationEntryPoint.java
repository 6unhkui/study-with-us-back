package switus.user.back.studywithus.common.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomerAuthenticationEntryPoint implements AuthenticationEntryPoint {
//    private static ApiResult E401 = new ApiResult("Access denied", HttpStatus.UNAUTHORIZED);

    @Autowired
    private ObjectMapper om;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("content-type", "html/text");
        response.getWriter().write("Access denied");
        response.getWriter().flush();
        response.getWriter().close();
    }
}
