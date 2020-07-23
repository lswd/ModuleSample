package com.fanyafeng.modules.app

import android.app.Application
import android.content.Context
import com.facebook.drawee.backends.pipeline.Fresco
import com.fanyafeng.modules.mediapick.config.RippleImageLoadFrameImpl
import com.ripple.log.tpyeextend.toLogD
import com.ripple.media.picker.RippleMediaPick
import com.ripple.permission.RipplePermissionImpl
import com.ripple.tool.date.long2Date

/**
 * Author: fanyafeng
 * Data: 2020/4/26 17:30
 * Email: fanyafeng@live.cn
 * Description:
 */
open class AppConfig : Application() {
    override fun onCreate() {
        super.onCreate()
        toLogD("APP开始启动时间 onCreate：" + System.currentTimeMillis().long2Date())
        RipplePermissionImpl.init(this)
        Fresco.initialize(this)
        RippleMediaPick
            .getInstance()
            .setImageLoadFrame(RippleImageLoadFrameImpl())
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        toLogD("APP开始启动时间 attachBaseContext：" + System.currentTimeMillis().long2Date())
    }

}