package com.example.practice.utils

import android.app.Application
import com.example.practice.R
import com.kakao.sdk.common.KakaoSdk

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 카카오 SDK 초기화
        KakaoSdk.init(this, getString(R.string.kakao_app_key))
    }
}
