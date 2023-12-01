package system;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import system.core.entity.User;
import system.core.enums.UserGradeType;
import system.core.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DummyUsers {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Order(value = 1)
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createGuests();
        createSchedulers();
        createSysops();
    }

    /**
     * UserGradeType.GUEST 등급의 더미 사용자를 생성한다.
     */
    private void createGuests() {
        userRepository.saveAll(createUsers("1", "게스트", UserGradeType.GUEST));
    }

    /**
     * UserGradeType.SCHED 등급의 더미 사용자를 생성한다.
     */
    private void createSchedulers() {
        userRepository.saveAll(createUsers("2", "스케줄러", UserGradeType.SCHED));
    }

    /**
     * UserGradeType.SYSOP 등급의 더미 사용자를 생성한다.
     */
    private void createSysops() {
        userRepository.saveAll(createUsers("3", "관리자", UserGradeType.SYSOP));
    }

    private List<User> createUsers(String userCode, String userName, UserGradeType gradeType) {
        List<User> users = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            users.add(User.create(
                String.format("%s%05d", userCode, i),
                String.format("%s%d", userName, i),
                String.format("%s%d@test.com", gradeType.name().toLowerCase(), i),
                passwordEncoder.encode("1"),
                gradeType
            ));
        }

        return users;
    }
}