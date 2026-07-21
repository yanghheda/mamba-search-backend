package com.rc.mambasaerchbackend.utils;

import com.rc.mambasaerchbackend.common.BusinessException;
import com.rc.mambasaerchbackend.common.ErrorCode;

public final class ThrowUtils {

    // 条件成立时抛业务异常
    public static void throwIf(boolean condition, ErrorCode errorCode) {
        if (condition) {
            throw new BusinessException(errorCode);
        }
    }

    // 条件成立时抛指定异常
    public static void throwIf(boolean condition, RuntimeException e) {
        if (condition) {
            throw e;
        }
    }
}
