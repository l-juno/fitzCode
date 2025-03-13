package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.mapper.SearchLogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchLogServiceImpl implements SearchLogService {

    private final SearchLogMapper searchLogMapper;

    @Override
    public void saveSearchLog(int userId, String keyword) {
        log.debug("Attempting to save search log: userId={}, keyword={}", userId, keyword);
        Long count = searchLogMapper.countValidKeyword(keyword);
        log.debug("Keyword validity check: count={}", count);

        if (count != null && count.intValue() > 0) {
            searchLogMapper.insertSearchLog(userId, keyword);
            log.debug("Search log saved: userId={}, keyword={}", userId, keyword);
        } else {
            log.debug("Keyword not found in PRODUCT or CATEGORY, skipping save: keyword={}", keyword);
        }
    }
}