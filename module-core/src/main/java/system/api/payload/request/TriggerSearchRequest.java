package system.api.payload.request;

import lombok.*;
import system.core.enums.search.TriggerSortBy;
import system.share.base.common.enums.SortType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TriggerSearchRequest {

    @Builder.Default
    private SortType sort = SortType.DESC; // 정렬 방향

    @Builder.Default
    private TriggerSortBy sortBy = TriggerSortBy.SEQ; // 정렬 기준

    @Builder.Default
    private String keyword = ""; // 검색 단어
}