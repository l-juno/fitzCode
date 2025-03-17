package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.mapper.AdminCouponMapper;
import kr.co.fitzcode.common.dto.CouponDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final AdminCouponMapper couponMapper;

    @Override
    @Transactional
    public void addCoupon(CouponDTO couponDTO) {
        couponMapper.insertCoupon(couponDTO);
    }

    @Override
    public List<CouponDTO> getAllCoupons(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return couponMapper.getAllCoupons(offset, pageSize);
    }

    @Override
    public CouponDTO getCouponById(Integer couponId) {
        return couponMapper.getCouponById(couponId);
    }

    @Override
    @Transactional
    public void updateCoupon(CouponDTO couponDTO) {
        couponMapper.updateCoupon(couponDTO);
    }

    @Override
    @Transactional
    public void deleteCoupon(Integer couponId) {
        couponMapper.deleteCoupon(couponId);
    }

    @Override
    public int countAllCoupons() {
        return couponMapper.countAllCoupons();
    }

    @Override
    @Transactional
    public void issueCouponToUser(Integer couponId, Integer userId, Timestamp validUntil) {
        couponMapper.issueCouponToUser(couponId, userId, validUntil);
    }

    @Override
    @Transactional
    public void issueCouponToAllUsers(Integer couponId, Timestamp validUntil) {
        List<Integer> userIds = couponMapper.getAllUserIds();
        for (Integer userId : userIds) {
            couponMapper.issueCouponToUser(couponId, userId, validUntil);
        }
    }
}