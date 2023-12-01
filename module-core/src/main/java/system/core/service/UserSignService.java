package system.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import system.api.payload.request.UserSignRequest;
import system.api.payload.response.UserSignResponse;
import system.core.entity.User;
import system.core.enums.UserGradeType;
import system.core.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSignService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 사용자 등록을 수행한다.
     */
    @Transactional
    public UserSignResponse sign(UserSignRequest userRequest, UserGradeType gradeType) {
        User entity = User.create(
                userRequest.getUserCode(),
                userRequest.getUserName(),
                userRequest.getUserEmail(),
                passwordEncoder.encode(userRequest.getUserPassword()),
                gradeType
        );

        return UserSignResponse.create(userRepository.save(entity));
    }
}