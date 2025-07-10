package com.example.ch17_database

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

// SettingActivity 클래스는 AppCompatActivity를 상속받아 Android 액티비티 역할을 함
class SettingActivity : AppCompatActivity() {

    // 액티비티가 생성될 때 호출되는 생명주기 메서드
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 레이아웃 XML 파일을 현재 액티비티의 UI로 설정
        // res/layout/activity_setting.xml 파일을 기반으로 화면 구성
        setContentView(R.layout.activity_setting)
    }
}