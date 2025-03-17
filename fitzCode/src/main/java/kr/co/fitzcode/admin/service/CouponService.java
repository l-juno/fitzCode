package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.common.dto.CouponDTO;

import java.sql.Timestamp;
import java.util.List;

public interface CouponService {
    void addCoupon(CouponDTO couponDTO);
    List<CouponDTO> getAllCoupons(int page, int pageSize);
    CouponDTO getCouponById(Integer couponId);
    void updateCoupon(CouponDTO couponDTO);
    void deleteCoupon(Integer couponId);
    int countAllCoupons();
    void issueCouponToUser(Integer couponId, Integer userId, Timestamp validUntil);
    void issueCouponToAllUsers(Integer couponId, Timestamp validUntil);
}