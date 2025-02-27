package kr.co.fitzcode.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ShoeSize {
    SIZE_220(220),
    SIZE_230(230),
    SIZE_240(240),
    SIZE_250(250),
    SIZE_260(260),
    SIZE_270(270),
    SIZE_280(280),
    SIZE_290(290),
    SIZE_300(300);

    private final int size;

    public static ShoeSize fromSize(int size) {
        for (ShoeSize s : values()) {
            if (s.getSize() == size) {
                return s;
            }
        }
        throw new IllegalArgumentException("적합하지 않은 신발 사이즈: " + size);
    }
}