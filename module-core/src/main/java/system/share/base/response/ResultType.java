package system.share.base.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ResultType {

    // -----------------------------------------------------------------------------------------------------------------

    /** 요청 성공 */
    SUCCESS("SRC-001", "요청을 수행하였습니다."),

    /** 요청 실패 */
    FAILURE("SRC-002", "요청을 수행할 수 없습니다."),

    /** 요청 실패 */
    INVALID_JSON("SRC-003", "잘못된 요청입니다."),

    /** 요청 실패 */
    INVALID_VALUE("SRC-004", "잘못된 요청입니다."),

    /** 요청 실패 */
    SORT_TYPE_NOT_FOUND("SRC-005", "정렬 조건을 찾을 수 없습니다."),

    // -----------------------------------------------------------------------------------------------------------------

    /** 사용자 조회 실패 */
    USER_NOT_FOUND("SRU-001", "사용자 정보를 찾을 수 없습니다."),

    /** 사용자 권한 조회 실패 */
    USER_ROLE_NOT_FOUND("SRU-002", "사용자 권한을 찾을 수 없습니다."),

    /** 사용자 등급 조회 실패 */
    USER_GRADE_NOT_FOUND("SRU-003", "사용자 등급을 찾을 수 없습니다."),

    /** 사용자 인증 조회 실패 */
    AUTHENTICATION_NOT_FOUND("SRU-004", "사용자 인증 정보를 찾을 수 없습니다."),

    /** 사용자 로그 조회 실패 */
    USER_LOG_NOT_FOUND("SRU-005", "사용자 로그를 찾을 수 없습니다."),

    /** 사용자 로그 유형 조회 실패 */
    USER_LOG_TYPE_NOT_FOUND("SRU-006", "사용자 로그 유형을 찾을 수 없습니다."),

    /** 사용자 정렬 조건 조회 실패 */
    USER_SORT_TYPE_NOT_FOUND("SRU-007", "사용자 정렬 조건을 찾을 수 없습니다."),

    /** 사용자 사번 중복 */
    DUPLICATE_USER_CODE("SRU-008", "이미 등록된 사번은 사용할 수 없습니다."),

    /** 사용자 이메일 중복 */
    DUPLICATE_USER_EMAIL("SRU-009", "이미 등록된 이메일은 사용할 수 없습니다."),

    /** 사용자 이메일, 비밀번호 불일치 */
    USER_LOGIN_MISMATCH("SRU-010", "이메일, 비밀번호를 확인하세요."),

    /** 인증되지 않은 요청 */
    UNAUTHENTICATED_REQUEST("SRU-011", "요청을 수행하기 위한 인증이 필요합니다."),

    /** 인가되지 않은 요청 */
    UNAUTHORIZED_REQUEST("SRU-012", "요청을 수행하기 위한 권한이 없습니다."),

    // -----------------------------------------------------------------------------------------------------------------

    /** 작업 목록 없음 */
    JOB_EMPTY("SRJ-001", "실행 중인 작업이 없습니다."),

    /** 작업 조회 실패 */
    JOB_NOT_FOUND("SRJ-002", "작업을 찾을 수 없습니다."),

    /** 작업 클래스 조회 실패 */
    JOB_CLASS_NOT_FOUND("SRJ-003", "작업 클래스를 찾을 수 없습니다."),

    /** 작업 클래스 유형 조회 실패 */
    JOB_CLASS_TYPE_NOT_FOUND("SRJ-004", "작업 클래스 유형을 찾을 수 없습니다."),

    /** 작업 정렬 조건 조회 실패 */
    JOB_SORT_TYPE_NOT_FOUND("SRJ-005", "작업 정렬 조건을 찾을 수 없습니다."),

    /** 작업 생성 실패 */
    JOB_CREATION("SRJ-006", "작업을 생성할 수 없습니다."),

    /** 작업 수정 실패 */
    JOB_UPDATION("SRJ-007", "작업을 수정할 수 없습니다."),

    /** 작업 삭제 실패 */
    JOB_DELETION("SRJ-008", "작업을 삭제할 수 없습니다."),

    /** 작업 전환 실패 */
    JOB_SWITCH("SRJ-009", "작업 상태를 변경할 수 없습니다."),

    /** 작업 중복 */
    JOB_DUPLICATION("SRJ-010", "이미 등록된 작업 이름, 그룹은 사용할 수 없습니다."),

    /** 작업 데이터 직렬화 오류 */
    JOB_SERIALIZATION("SRJ-011", "작업 데이터를 변환할 수 없습니다."),

    /** 작업 데이터 역직렬화 오류 */
    JOB_DESERIALIZATION("SRJ-012", "작업 데이터를 변환할 수 없습니다."),

    /** 작업 등록자 불일치 */
    JOB_REGISTER_MISMATCH("SRJ-013", "작업 관리 권한이 없습니다."),

    /** 작업 클래스 유형 불일치 */
    JOB_CLASS_TYPE_MISMATCH("SRJ-014", "작업 클래스 유형이 일치하지 않습니다."),

    // -----------------------------------------------------------------------------------------------------------------

    /** 작업 로그 조회 실패 */
    JOB_LOG_NOT_FOUND("SRJ-015", "작업 로그를 찾을 수 없습니다."),

    /** 작업 로그 중복 */
    JOB_LOG_DUPLICATION("SRJ-016", "작업 로그에 등록된 작업 이름, 그룹은 사용할 수 없습니다."),

    // -----------------------------------------------------------------------------------------------------------------

    /** 트리거 목록 없음 */
    TRIGGER_EMPTY("SRT-001", "실행 중인 트리거가 없습니다."),

    /** 트리거 조회 실패 */
    TRIGGER_NOT_FOUND("SRT-002", "트리거를 찾을 수 없습니다."),

    /** 트리거 정렬 조건 조회 실패 */
    TRIGGER_SORT_TYPE_NOT_FOUND("SRT-003", "트리거 정렬 조건을 찾을 수 없습니다."),

    /** 트리거 생성 실패 */
    TRIGGER_CREATION("SRT-004", "트리거를 생성할 수 없습니다."),

    /** 트리거 수정 실패 */
    TRIGGER_UPDATION("SRT-005", "트리거를 수정할 수 없습니다."),

    /** 트리거 삭제 실패 */
    TRIGGER_DELETION("SRT-006", "트리거를 삭제할 수 없습니다."),

    /** 트리거 전환 실패 */
    TRIGGER_SWITCH("SRT-007", "트리거 상태를 변경할 수 없습니다."),

    /** 트리거 중복 */
    TRIGGER_DUPLICATION("SRT-008", "이미 등록된 트리거 이름, 그룹은 사용할 수 없습니다."),

    /** 잘못된 트리거 시간 값 */
    INVALID_TRIGGER_TIME_VALUE("SRT-009", "트리거 시작, 종료 시간이 잘못되었습니다."),

    /** 잘못된 트리거 반복 값 */
    INVALID_TRIGGER_REPEAT_VALUE("SRT-010", "트리거 반복 값이 잘못되었습니다."),

    /** 트리거 등록자 불일치 */
    TRIGGER_REGISTER_MISMATCH("SRT-011", "트리거 관리 권한이 없습니다."),

    /** 트리거 실행 유형 불일치 */
    TRIGGER_CLASS_TYPE_MISMATCH("SRT-012", "트리거 실행 유형이 맞지 않습니다."),

    /** 지원하지 않는 트리거 실행 유형 */
    UNSUPPORTED_TRIGGER_CLASS_TYPE("SRT-013", "지원하지 않는 트리거 실행 유형입니다."),

    /** 지원하지 않는 트리거 정책 유형 */
    UNSUPPORTED_TRIGGER_POLICY_TYPE("SRT-014", "지원하지 않는 트리거 정책 유형입니다."),

    // -----------------------------------------------------------------------------------------------------------------

    /** 트리거 로그 조회 실패 */
    TRIGGER_LOG_NOT_FOUND("SRT-015", "트리거 로그를 찾을 수 없습니다."),

    /** 트리거 로그 중복 */
    TRIGGER_LOG_DUPLICATION("SRT-016", "트리거 로그에 등록된 트리거 이름, 그룹은 사용할 수 없습니다.");

    // -----------------------------------------------------------------------------------------------------------------

    private final String code;
    private final String message;
}