package ra.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component

public class JwtEntryPoint implements AuthenticationEntryPoint {
    //    JwtEntryPoint: Đây là một lớp triển khai của giao diện AuthenticationEntryPoint trong Spring Security.
//    Nó xử lý các lỗi xác thực bằng cách gửi phản hồi HTTP 401 (Unauthorized) đến các máy khách khi xác thực thất bại.
    public final Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        logger.error("Error->>> Authentication : ", authException.getMessage());
        // Tạo một đối tượng ResponseEntity chứa thông báo lỗi và mã trạng thái HTTP 401
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Bạn không có quyền truy cập", HttpStatus.UNAUTHORIZED);

        // Thiết lập loại nội dung và mã hóa cho phản hồi JSON
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        // Gửi phản hồi JSON về client
        response.getWriter().write(responseEntity.getBody());
        response.setStatus(responseEntity.getStatusCodeValue());

    }
}

