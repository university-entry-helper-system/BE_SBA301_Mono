package com.example.SBA_M.utils;

public enum FaqType {
    TONG_QUAN_TUYEN_SINH("Nhóm 1: Tổng quan về tuyển sinh"),
    XEM_DIEM_CHUAN("Nhóm 2: Xem điểm chuẩn"),
    TRA_CUU_TRUONG_HOC("Nhóm 3: Tra cứu trường học"),
    TRA_CUU_NGANH_HOC("Nhóm 4: Tra cứu ngành học"),
    HOI_DAP_HO_SO_QUY_TRINH("Nhóm 5: Hỏi đáp về hồ sơ & quy trình"),
    TIN_TUC_TUYEN_SINH("Nhóm 6: Tin tức tuyển sinh"),
    HOC_PHI_HOC_BONG("Nhóm 7: Học phí & học bổng"),
    HO_TRO_TU_VAN_CA_NHAN("Nhóm 8: Hỗ trợ tư vấn cá nhân"),
    THONG_TIN_BO_SUNG("Nhóm 9: Thông tin bổ sung"),
    VAN_DE_KY_THUAT_TAI_KHOAN("Nhóm 10: Vấn đề kỹ thuật & tài khoản");

    private final String displayName;

    FaqType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}