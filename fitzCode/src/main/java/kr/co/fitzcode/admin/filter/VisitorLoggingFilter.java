package kr.co.fitzcode.admin.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.fitzcode.admin.dto.VisitorDTO;
import kr.co.fitzcode.admin.mapper.DashboardMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Component
public class VisitorLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(VisitorLoggingFilter.class);
    private static final String VISITOR_ID_COOKIE = "visitorId";
    private static final int COOKIE_MAX_AGE = 7 * 24 * 60 * 60; // 7일 유지
    private static final long DUPLICATE_CHECK_INTERVAL = 30 * 60 * 1000; // 30분 (밀리초)
    private final DashboardMapper dashboardMapper;

    // 검색엔진 도메인 패턴
    private static final Pattern[] SEARCH_ENGINE_PATTERNS = {
            Pattern.compile("https?://(www\\.)?google\\.(com|co\\.kr|.*)"),
            Pattern.compile("https?://(www\\.)?naver\\.com"),
            Pattern.compile("https?://(www\\.)?yahoo\\.com"),
            Pattern.compile("https?://(www\\.)?bing\\.com")
    };

    @Autowired
    public VisitorLoggingFilter(DashboardMapper dashboardMapper) {
        this.dashboardMapper = dashboardMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String pageUrl = request.getRequestURI();

            // 정적 리소스 제외
            if (isStaticResource(pageUrl)) {
                filterChain.doFilter(request, response);
                return;
            }

            // 쿠키에서 visitorId 가져오기
            String visitorId = getVisitorIdFromCookie(request);

            // 쿠키에 visitorId가 없으면 새로 생성
            if (visitorId == null) {
                visitorId = UUID.randomUUID().toString();
                setVisitorIdCookie(response, visitorId);
//                log.debug("새로운 visitorId 생성 및 쿠키에 설정: {}", visitorId);
            }

            // 방문자 데이터 로깅
            Long userId = null; // 로그인한 경우 실제 사용자 ID 설정 (SecurityContext에서 가져오기)
            Timestamp visitTime = new Timestamp(System.currentTimeMillis());
            String referrerUrl = request.getHeader("Referer");
            String userAgent = request.getHeader("User-Agent");
            int deviceType = request.getHeader("User-Agent").contains("Mobile") ? 1 : 2; // 간단한 모바일 감지
            String ipAddress = request.getRemoteAddr();

            // 검색엔진 또는 외부 링크 여부 확인
            String processedReferrerUrl = processReferrerUrl(referrerUrl);

            // 중복 체크: 30분 이내 동일 visitorId와 pageUrl로 요청이 있었는지 확인
            VisitorDTO recentLog = dashboardMapper.findRecentVisitLog(visitorId, pageUrl);
            if (recentLog == null || (System.currentTimeMillis() - recentLog.getVisitTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()) > DUPLICATE_CHECK_INTERVAL) {
//                log.debug("요청 처리 중 - 페이지 URL: {}, visitorId: {}, 방문 시간: {}, 유입 경로: {}",
//                        pageUrl, visitorId, visitTime, processedReferrerUrl);

                if (dashboardMapper != null) {
                    dashboardMapper.insertVisitLog(userId, visitorId, visitTime, pageUrl, processedReferrerUrl, userAgent, deviceType, ipAddress);
//                    log.debug("방문자 로그 삽입: userId={}, visitorId={}, 페이지 URL={}, 유입 경로={}",
//                            userId, visitorId, pageUrl, processedReferrerUrl);
                } else {
//                    log.error("DashboardMapper가 null입니다, 방문자 로그를 삽입할 수 없습니다");
                }
            } else {
//                log.debug("중복 방문자 로그 건너뜀 - visitorId={}, 페이지 URL={} (30분 이내), 유입 경로: {}",
//                        visitorId, pageUrl, processedReferrerUrl);
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
//            log.error("VisitorLoggingFilter에서 오류 발생: ", e);
            throw e;
        }
    }

    private boolean isStaticResource(String path) {
        return path.startsWith("/css/") ||
                path.startsWith("/img/") ||
                path.startsWith("/js/") ||
                path.equals("/favicon.ico");
    }

    private String getVisitorIdFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            Optional<Cookie> visitorCookie = Arrays.stream(request.getCookies())
                    .filter(cookie -> VISITOR_ID_COOKIE.equals(cookie.getName()))
                    .findFirst();
            return visitorCookie.map(Cookie::getValue).orElse(null);
        }
        return null;
    }

    private void setVisitorIdCookie(HttpServletResponse response, String visitorId) {
        Cookie cookie = new Cookie(VISITOR_ID_COOKIE, visitorId);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(COOKIE_MAX_AGE);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private String processReferrerUrl(String referrerUrl) {
        if (referrerUrl == null || referrerUrl.isEmpty()) {
            return null;
        }

        // 검색엔진 패턴 확인
        for (Pattern pattern : SEARCH_ENGINE_PATTERNS) {
            if (pattern.matcher(referrerUrl).matches()) {
                return "SEARCH_ENGINE:" + referrerUrl;
            }
        }

        // 로컬호스트인 경우 null
        if (referrerUrl.startsWith("http://localhost:8080") || referrerUrl.startsWith("https://localhost:8080")) {
            return null;
        }

        // 외부 링크로 간주함
        return "EXTERNAL_LINK:" + referrerUrl;
    }
}