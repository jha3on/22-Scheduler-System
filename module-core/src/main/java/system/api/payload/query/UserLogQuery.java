package system.api.payload.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import system.core.enums.UserLogType;
import system.share.base.utility.DateTimeUtils;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserLogQuery {
    private Long logSeq; // 로그 번호
    private Long jobSeq; // 작업 번호
    private Long triggerSeq; // 트리거 번호
    private String logResult; // 로그 내용
    private String logDetail; // 로그 상세 내용
    private String logRegister; // 로그 등록자
    private String logType; // 로그 유형
    private String logTime; // 로그 시간

    @QueryProjection
    public UserLogQuery(
        Long logSeq,
        Long jobSeq,
        Long triggerSeq,
        String logResult,
        String logRegister,
        UserLogType logType,
        LocalDateTime logTime
    ) {
        this.logSeq = logSeq;
        this.jobSeq = jobSeq;
        this.triggerSeq = triggerSeq;
        this.logResult = logResult;
        this.logRegister = logRegister;
        this.logType = logType.name();
        this.logTime = DateTimeUtils.toString(logTime);
    }

    @QueryProjection
    public UserLogQuery(
        Long logSeq,
        Long jobSeq,
        Long triggerSeq,
        String logResult,
        String logDetail,
        String logRegister,
        UserLogType logType,
        LocalDateTime logTime
    ) {
        this.logSeq = logSeq;
        this.jobSeq = jobSeq;
        this.triggerSeq = triggerSeq;
        this.logResult = logResult;
        this.logDetail = logDetail;
        this.logRegister = logRegister;
        this.logType = logType.name();
        this.logTime = DateTimeUtils.toString(logTime);
    }
}