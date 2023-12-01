package system.core.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserStoreType {

    /**
     * 저장 상태
     */
    STORED,

    /**
     * 삭제 상태
     */
    DELETED,
}