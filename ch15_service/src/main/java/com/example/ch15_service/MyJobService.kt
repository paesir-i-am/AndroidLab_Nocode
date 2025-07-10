package com.example.ch15_service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build
import androidx.core.app.NotificationCompat

// JobService: 예약된 작업을 백그라운드에서 실행할 수 있도록 도와주는 서비스
class MyJobService : JobService() {

    // Job이 시작될 때 호출되는 콜백 메서드
    override fun onStartJob(params: JobParameters?): Boolean {
        // NotificationManager 시스템 서비스 가져오기
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Android 8.0 (Oreo, API 26) 이상은 NotificationChannel 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "oneId",               // 채널 ID
                "oneName",             // 사용자에게 표시될 채널 이름
                NotificationManager.IMPORTANCE_DEFAULT // 알림 우선순위
            )
            channel.description = "oneDesc" // 채널 설명 설정
            manager.createNotificationChannel(channel) // 채널 등록
            NotificationCompat.Builder(this, "oneId")  // 채널 ID 기반 빌더 생성
        } else {
            // API 26 미만에서는 채널 없이 빌더 생성
            NotificationCompat.Builder(this)
        }.run {
            // 공통 알림 설정
            setSmallIcon(android.R.drawable.ic_notification_overlay) // 작은 아이콘 설정
            setContentTitle("JobScheduler Title") // 알림 제목
            setContentText("Content Message")     // 알림 메시지
            setAutoCancel(true)                   // 클릭 시 알림 자동 제거
            manager.notify(1, build())            // 알림 발행 (ID: 1)
        }

        // return 값이 false면 Job이 끝났음을 의미
        return false
    }

    // Job이 취소될 때 호출되는 메서드
    override fun onStopJob(params: JobParameters?): Boolean {
        // true를 반환하면 시스템이 Job을 재시도함
        return true
    }
}