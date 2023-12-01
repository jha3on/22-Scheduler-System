package system.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import system.api.payload.request.UserSignRequest;
import system.core.repository.query.UserQueryRepository;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {
    private final UserQueryRepository userQueryRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserSignRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserSignRequest request = (UserSignRequest) target;

        validateUserCode(request.getUserCode(), errors);
        validateUserName(request.getUserName(), errors);
        validateUserEmail(request.getUserEmail(), errors);
        validateUserPassword(request.getUserPassword(), errors);
    }

    private void validateUserCode(String userCode, Errors errors) {
        if (StringUtils.isEmpty(userCode)) {
            errors.rejectValue("userCode", "", "사번을 입력하세요.");
        }

        if (!isMatchUserCode(userCode)) {
            errors.rejectValue("userCode", "", "사번 형식(숫자 6자)을 확인하세요.");
        }

        if (userQueryRepository.existsByUserCode(userCode)) {
            errors.rejectValue("userCode", "", "이미 등록된 사번입니다.");
        }
    }

    private void validateUserName(String userName, Errors errors) {
        if (StringUtils.isEmpty(userName)) {
            errors.rejectValue("userName", "", "이름을 입력하세요.");
        }

        if (!isMatchUserName(userName)) {
            errors.rejectValue("userName", "", "이름 형식(국문 5자 이내)을 확인하세요.");
        }
    }

    private void validateUserEmail(String userEmail, Errors errors) {
        if (StringUtils.isEmpty(userEmail)) {
            errors.rejectValue("userEmail", "", "이메일을 입력하세요.");
        }

        if (!isMatchUserEmail(userEmail)) {
            errors.rejectValue("userEmail", "", "이메일 형식을 확인하세요.");
        }

        if (userQueryRepository.existsByUserEmail(userEmail)) {
            errors.rejectValue("userEmail", "", "이미 등록된 이메일입니다.");
        }
    }

    private void validateUserPassword(String userPassword, Errors errors) {
        if (StringUtils.isEmpty(userPassword)) {
            errors.rejectValue("userPassword", "", "비밀번호를 입력하세요.");
        }

        if (!isMatchUserPassword(userPassword)) {
            errors.rejectValue("userPassword", "", "비밀번호 형식(영문, 숫자 10자 이내)을 확인하세요.");
        }
    }

    private Boolean isMatchUserCode(String userCode) {
        return Pattern.matches("^\\d{6}$", userCode);
    }

    private Boolean isMatchUserName(String userName) {
        return Pattern.matches("^[가-힣]{1,5}$", userName);
    }

    private Boolean isMatchUserEmail(String userEmail) {
        return Pattern.matches("^\\S+@\\S+\\.\\S+$", userEmail);
    }

    private Boolean isMatchUserPassword(String userPassword) {
        return Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{1,10}$", userPassword);
    }
}