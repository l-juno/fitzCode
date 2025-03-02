package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.dto.InquiryDTO;
import kr.co.fitzcode.admin.mapper.InquiryMapper;
import kr.co.fitzcode.common.enums.InquiryStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {
    private final InquiryMapper inquiryMapper;

    // 답변되지 않은 문의 개수 조회
    @Override
    public int getUnansweredInquiryCount() {
        return inquiryMapper.getUnansweredInquiryCount();
    }

    // 필터링된 총 개수 조회 (카테고리 및 상태 필터링)
    @Override
    public int getTotalInquiryCount(List<Integer> categoryIds, List<Integer> statusCodes) {
        return inquiryMapper.getTotalFilteredInquiryCount(categoryIds, statusCodes);
    }

    // 필터링된 1:1 문의 목록 조회 (페이지네이션)
    @Override
    public List<InquiryDTO> getInquiryList(int page, int size, List<Integer> categoryIds, List<Integer> statusCodes) {
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        int offset = (page - 1) * size;
        return inquiryMapper.getFilteredInquiryList(size, offset, categoryIds, statusCodes);
    }

    // 전체 페이지 수 계산
    @Override
    public int calculateTotalPages(int totalCount, int size) {
        if (size < 1) size = 10;
        return Math.max(1, (int) Math.ceil((double) totalCount / size));
    }

    // 문의 상세 조회
    @Override
    public InquiryDTO getInquiryDetail(int inquiryId) {
        return inquiryMapper.getInquiryDetail(inquiryId);
    }

    // 문의의 상태 업데이트
    @Override
    public void updateInquiryStatus(int inquiryId, int status) {
        inquiryMapper.updateInquiryStatus(inquiryId, status);
    }

    // 문의 카테고리 업데이트
    @Override
    public void updateInquiryCategory(int inquiryId, int category) {
        inquiryMapper.updateInquiryCategory(inquiryId, category);
    }

    // 문의에 답변 추가
    @Override
    public void updateInquiryReply(int inquiryId, String reply) {
        inquiryMapper.updateInquiryReplyAndStatus(inquiryId, reply, InquiryStatus.ANSWERED.getCode());
    }

    // 문의 등록 (이미지 포함)
    @Override
    public void saveInquiry(InquiryDTO inquiryDTO, List<MultipartFile> images) {
        // 문의 저장
        inquiryMapper.insertInquiry(inquiryDTO);

        if (images != null && !images.isEmpty()) {
            // 이미지 개수 제한 (최대 5개)
            if (images.size() > 5) {
                throw new IllegalArgumentException("최대 5개의 이미지만 첨부 가능합니다.");
            }

            // 총 용량 계산
            long totalSize = images.stream().mapToLong(MultipartFile::getSize).sum();
            if (totalSize > 25 * 1024 * 1024) { // 25MB 제한
                throw new IllegalArgumentException("총 이미지 용량은 25MB를 초과할 수 없습니다.");
            }

            // 개별 이미지 크기 검사 및 업로드
            List<String> imageUrls = new ArrayList<>();
            int order = 0;
            for (MultipartFile image : images) {
                if (image.getSize() > 5 * 1024 * 1024) { // 5MB 제한
                    throw new IllegalArgumentException("각 이미지는 5MB 미만이어야 합니다.");
                }
                String imageUrl = uploadImage(image);
                imageUrls.add(imageUrl);
                inquiryMapper.insertInquiryImage(inquiryDTO.getInquiryId(), imageUrl, order++);
            }
            inquiryDTO.setImageUrls(imageUrls); // DTO에 반영
        }
    }

    // 이미지 업로드 메서드
    private String uploadImage(MultipartFile image) {
        try {
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            String uploadDir = "src/main/resources/static/uploads/inquiry/"; // 실제 경로 조정 필요
            File dest = new File(uploadDir + fileName);
            dest.getParentFile().mkdirs(); // 디렉토리 생성
            image.transferTo(dest);
            return "/uploads/inquiry/" + fileName; // URL 반환
        } catch (Exception e) {
            throw new RuntimeException("이미지 업로드 실패: " + e.getMessage());
        }
    }
}