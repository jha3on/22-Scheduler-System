package system.core.enums.schedule;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ScheduleStoreType {

    /**
     * 스케줄 저장
     */
    STORED,

    /**
     * 스케줄 삭제
     */
    DELETED,
}