package system.share.base.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JpaUtils {

    /**
     * Pageable 객체에 페이징 조건을 적용한다.
     */
    public static Pageable set(Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        int size = pageable.getPageSize();

        return PageRequest.of(page, size);
    }

    /**
     * Pageable 객체에 페이징, 정렬 조건을 적용한다.
     */
    public static Pageable set(Pageable pageable, String sort, String sortBy) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        int size = pageable.getPageSize();
        Sort option = Sort.by(Sort.Direction.fromString(sort), sortBy);

        return PageRequest.of(page, size, option);
    }

    /**
     * Page 컬렉션을 생성한다.
     */
    public static <T> Page<T> toPage(final List<T> contents, final Long total, final Pageable pageable) {
        return new PageImpl<>(contents, pageable, total);
    }
}