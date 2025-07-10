package com.example.ch15_outer

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

// AIDL을 통해 외부 앱에서 제어할 수 있는 음악 재생 서비스 정의
class MyAIDLService : Service() {

    // MediaPlayer 인스턴스 선언
    lateinit var player: MediaPlayer

    // 서비스가 처음 생성될 때 호출됨
    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer()  // 초기 MediaPlayer 생성 (빈 상태)
    }

    // 서비스가 종료될 때 호출됨
    override fun onDestroy() {
        super.onDestroy()
        player.release()  // MediaPlayer 리소스 해제
    }

    // 클라이언트가 서비스에 바인딩할 때 호출됨
    override fun onBind(intent: Intent): IBinder {
        // AIDL 인터페이스를 구현하여 Stub 객체 반환
        return object : MyAIDLInterface.Stub() {

            // 재생 중인 음악의 전체 길이를 반환 (단위: 밀리초)
            override fun getMaxDuiration(): Int {
                return if (player.isPlaying)
                    player.duration
                else 0
            }

            // 음악 재생 시작
            override fun start() {
                if (!player.isPlaying) {
                    // raw 폴더의 music 리소스를 사용하여 MediaPlayer 초기화
                    player = MediaPlayer.create(this@MyAIDLService, R.raw.music)
                    try {
                        player.start()  // 재생 시작
                    } catch (e: Exception) {
                        e.printStackTrace()  // 예외 처리
                    }
                }
            }

            // 음악 재생 중지
            override fun stop() {
                if (player.isPlaying)
                    player.stop()
            }
        }
    }
}