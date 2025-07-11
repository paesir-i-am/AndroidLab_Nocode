package com.example.ch20_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.ch20_compose.ui.composable.MainScreen
import com.example.ch20_compose.ui.theme.AndroidLabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 상태바와 내비게이션 바를 앱 콘텐츠 영역까지 확장
        enableEdgeToEdge()

        // Compose UI 설정
        setContent {
            // 커스텀 테마
            AndroidLabTheme {
                // 기본 레이아웃 구조 제공 (TopBar, BottomBar 등 포함 가능)
                Scaffold(
                    modifier = Modifier.fillMaxSize() // 전체 화면 채우기
                ) { innerPadding ->
                    // 시스템 UI 영역 (예: 상태바, 내비게이션바) 만큼 padding 적용
                    MainScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}