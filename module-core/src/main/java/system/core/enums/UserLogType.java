package system.core.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserLogType {

    /**
     * 사용자 스케줄 저장
     */
    STORED,

    /**
     * 사용자 스케줄 수정
     */
    UPDATED,

    /**
     * 사용자 스케줄 삭제
     */
    DELETED,
}