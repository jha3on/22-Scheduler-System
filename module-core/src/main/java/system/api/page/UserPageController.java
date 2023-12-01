package system.api.page;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import system.api.payload.query.UserLogQuery;
import system.api.payload.query.UserQuery;
import system.api.payload.request.UserSearchRequest;
import system.api.payload.response.UserProfileResponse;
import system.api.shape.SystemPageController;
import system.core.entity.User;
import system.core.service.UserQueryService;
import system.share.security.SecurityUtils;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserPageController implements SystemPageController {
    private final UserQueryService userQueryService;
    private final String PUBLIC_USER_PAGE = "secure/page";
    private final String PRIVATE_USER_PAGE = "schedule/page/user";

    /**
     * 사용자 등록 화면
     */
    @PreAuthorize(value = "isAnonymous()")
    @GetMapping(value = {"/user/sign"})
    public String userSign() {
        return String.format("%s/%s", PUBLIC_USER_PAGE, "userSign");
    }

    /**
     * 사용자 로그인 화면
     */
    @PreAuthorize(value = "isAnonymous()")
    @GetMapping(value = {"/login", "/user/login"})
    public String userLogin() {
        return String.format("%s/%s", PUBLIC_USER_PAGE, "userLogin");
    }

    /**
     * 사용자 프로필 화면
     */
    @GetMapping(value = {"/user/profile"})
    @PreAuthorize(value = "@userUtils.hasRoles({'R'})")
    public String userProfile(Model model) {
        User user = userQueryService.getUser(SecurityUtils.getUserCode());
        model.addAttribute("user", UserProfileResponse.create(user));

        return String.format("%s/%s", PRIVATE_USER_PAGE, "userProfile");
    }

    /**
     * 사용자 프로필 화면
     */
    @GetMapping(value = {"/user/profile/{userSeq}"})
    @PreAuthorize(value = "@userUtils.hasRoles({'R'})")
    public String userProfile(@PathVariable Long userSeq, Model model) {
        User user = userQueryService.getUser(userSeq);
        model.addAttribute("user", UserProfileResponse.create(user));

        return String.format("%s/%s", PRIVATE_USER_PAGE, "userProfile");
    }

    /**
     * 사용자 목록 화면
     */
    @GetMapping(value = {"/user/list"})
    @PreAuthorize(value = "@userUtils.hasRoles({'R'})")
    public String userList(@ModelAttribute UserSearchRequest request,
                           @PageableDefault Pageable pageable, Model model) {
        Page<UserQuery> results = userQueryService.getUserList(request, pageable);

        model.addAttribute("url", "/user/list");
        model.addAttribute("sort", request.getSort()); // 정렬 방향
        model.addAttribute("sortBy", request.getSortBy()); // 정렬 조건
        model.addAttribute("keyword", request.getKeyword()); // 검색 단어
        model.addAttribute("results", results);

        return String.format("%s/%s", PRIVATE_USER_PAGE, "userList");
    }

    /**
     * 사용자 로그 상세 화면
     */
    @GetMapping(value = {"/user/log/{logSeq}"})
    @PreAuthorize(value = "@userUtils.hasRoles({'R'})")
    public String userLogDetail(@PathVariable Long logSeq, Model model) {
        UserLogQuery log = userQueryService.getUserLog(logSeq);

        model.addAttribute("log", log);

        return String.format("%s/%s", PRIVATE_USER_PAGE, "userLogDetail");
    }

    /**
     * 사용자 로그 목록 화면
     */
    @GetMapping(value = {"/user/log"})
    @PreAuthorize(value = "@userUtils.hasRoles({'R'})")
    public String userLogList(@PageableDefault Pageable pageable, Model model) {
        Page<UserLogQuery> results = userQueryService.getUserLogList(pageable);

        model.addAttribute("url", "/user/log");
        model.addAttribute("results", results);

        return String.format("%s/%s", PRIVATE_USER_PAGE, "userLogList");
    }

    /**
     * 사용자 매뉴얼 화면
     */
    @GetMapping(value = {"/user/manual"})
    @PreAuthorize(value = "@userUtils.hasRoles({'R'})")
    public String userManual() {
        return String.format("%s/%s", PRIVATE_USER_PAGE, "userManual");
    }
}