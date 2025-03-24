package kr.co.fitzcode.community.service;

import kr.co.fitzcode.community.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl {
    private final CommentMapper commentMapper;
}
