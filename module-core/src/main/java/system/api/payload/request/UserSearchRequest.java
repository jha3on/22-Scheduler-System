package system.api.payload.request;

import lombok.*;
import system.core.enums.search.UserSortBy;
import system.share.base.common.enums.SortType;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchRequest {

    @Builder.Default
    private SortType sort = SortType.DESC; // 정렬 방향

    @Builder.Default
    private UserSortBy sortBy = UserSortBy.SEQ; // 정렬 기준

    @Builder.Default
    private String keyword = ""; // 검색 단어
}