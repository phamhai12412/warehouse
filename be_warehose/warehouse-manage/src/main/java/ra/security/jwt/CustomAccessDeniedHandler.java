package ra.security.jwt;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // Tạo một đối tượng ResponseEntity chứa thông báo lỗi và mã trạng thái HTTP 403
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Bạn không có quyền truy cập", HttpStatus.FORBIDDEN);

        // Thiết lập loại nội dung và mã hóa cho phản hồi JSON
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        // Gửi phản hồi JSON về client
        response.getWriter().write(responseEntity.getBody());
        response.setStatus(responseEntity.getStatusCodeValue());
    }
}
