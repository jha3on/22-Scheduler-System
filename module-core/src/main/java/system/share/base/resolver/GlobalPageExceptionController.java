package system.share.base.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class GlobalPageExceptionController {
    private final String ERROR_PAGE = "share/page";

    @GetMapping(value = "/error")
    public String error() {
        return String.format("%s/%s", ERROR_PAGE, "errorDefault");
    }

    @GetMapping(value = "/error/security")
    public String security() {
        return String.format("%s/%s", ERROR_PAGE, "errorSecurity");
    }
}