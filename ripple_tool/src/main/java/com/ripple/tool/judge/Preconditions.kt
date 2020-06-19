package com.ripple.tool.judge

/**
 * Author: fanyafeng
 * Data: 2020/3/6 18:10
 * Email: fanyafeng@live.cn
 * Description: 用来做判空，有返回值
 */

/**
 * 判断对象是否为空
 */
fun <T> checkNotNull(reference: T?, errorMessage: Any = "引用对象为空"): T {
    if (reference == null) {
        throw NullPointerException(errorMessage.toString())
    }
    return reference
}


fun <T> checkArgument(expression: Boolean, errorMessage: Any) {
    if (!expression) {
        throw IllegalArgumentException(errorMessage.toString())
    }
}

