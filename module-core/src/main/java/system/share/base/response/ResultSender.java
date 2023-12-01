package system.share.base.response;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import system.share.base.utility.MapperUtils;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResultSender {
    private static final String CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;
    private static final String CHARSET_TYPE = StandardCharsets.UTF_8.name();

    /**
     * 응답 값을 생성한다.
     */
    public static void set(ResultType type, HttpStatus status, HttpServletResponse response) throws IOException {
        Result<?> result = Result.create(type);

        response.setStatus(status.value());
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(CHARSET_TYPE);
        response.getWriter().write(MapperUtils.serialize(result));
    }

    /**
     * 응답 값을 생성한다.
     */
    public static void set(ResultType type, Object payload, HttpStatus status, HttpServletResponse response) throws IOException {
        Result<?> result = Result.create(type, payload);

        response.setStatus(status.value());
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(CHARSET_TYPE);
        response.getWriter().write(MapperUtils.serialize(result));
    }

    /**
     * 응답 값을 생성한다.
     */
    public static void set(
        ResultType type,
        HttpStatus status,
        String errorPage,
        String targetPage,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        response.setStatus(status.value());
        request.setAttribute("forward", targetPage);
        request.setAttribute("message", type.getMessage());
        request.getRequestDispatcher(errorPage).forward(request, response);
    }
}