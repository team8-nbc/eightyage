package com.example.eightyage.domain.product.entity;

public enum Category {
    SKINCARE("스킨케어"),
    MAKEUP("메이크업"),
    HAIRCARE("헤어케어"),
    BODYCARE("바디케어"),
    FRAGRANCE("향수"),
    SUNCARE("선케어"),
    CLEANSING("클렌징"),
    MASK_PACK("마스크팩"),
    MEN_CARE("남성용"),
    TOOL("뷰티 도구"),

    // 피부 타입
    DRY_SKIN("건성"),
    OILY_SKIN("지성"),
    NORMAL_SKIN("중성"),
    COMBINATION_SKIN("복합성"),
    SENSITIVE_SKIN("민감성"),
    ACNE_PRONE_SKIN("여드름성"),
    ATOPIC_SKIN("아토피");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
