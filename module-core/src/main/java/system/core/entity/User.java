package system.core.entity;

import lombok.*;
import lombok.experimental.Accessors;
import system.core.enums.UserGradeType;
import system.core.enums.UserStoreType;
import system.share.base.common.entity.AbstractEntity;
import javax.persistence.*;

@Getter
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true, of = {"userSeq"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_user", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_code"}),
    @UniqueConstraint(columnNames = {"user_email"}),
})
public class User extends AbstractEntity {

    @Id
    @Column(name = "user_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq; // 사용자 번호 (PK_1)

    @Column(name = "user_code", nullable = false, unique = true)
    private String userCode; // 사용자 사번 (UK_1)

    @Column(name = "user_name", nullable = false)
    private String userName; // 사용자 이름

    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail; // 사용자 이메일 (UK_2)

    @Column(name = "user_password", nullable = false)
    private String userPassword; // 사용자 비밀번호

    @Enumerated(value = EnumType.STRING)
    @Column(name = "grade_type", nullable = false)
    private UserGradeType gradeType; // 사용자 등급 유형

    @Enumerated(value = EnumType.STRING)
    @Column(name = "store_type", nullable = false)
    private UserStoreType storeType; // 사용자 등록 유형

    @Builder(access = AccessLevel.PRIVATE)
    private User(
        String userCode,
        String userName,
        String userEmail,
        String userPassword,
        UserGradeType gradeType,
        UserStoreType storeType
    ) {
        this.userCode = userCode;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.gradeType = gradeType;
        this.storeType = storeType;
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static User create(
        String userCode,
        String userName,
        String userEmail,
        String userPassword,
        UserGradeType gradeType
    ) {
        return User.builder()
                .userCode(userCode)
                .userName(userName)
                .userEmail(userEmail)
                .userPassword(userPassword)
                .gradeType(gradeType)
                .storeType(UserStoreType.STORED)
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------
}