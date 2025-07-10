package com.example.ch14_receiver

import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.ch14_receiver.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding을 사용하여 XML의 뷰와 연결
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ▶ 알림 권한 요청 런처 정의 (POST_NOTIFICATIONS)
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            // 모든 권한이 승인된 경우 → 브로드캐스트 전송
            if (it.all { permission -> permission.value == true }) {
                val intent = Intent(this, MyReceiver::class.java)
                sendBroadcast(intent)
            } else {
                Toast.makeText(this, "permission denied...", Toast.LENGTH_SHORT).show()
            }
        }

        // ▶ 배터리 상태 확인: 시스템 브로드캐스트를 수신하여 즉시 정보 확인
        registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))!!.apply {
            // 배터리 충전 상태 확인
            when (getIntExtra(BatteryManager.EXTRA_STATUS, -1)) {
                BatteryManager.BATTERY_STATUS_CHARGING -> {
                    // 충전 방식에 따른 이미지 및 텍스트 설정
                    when (getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)) {
                        BatteryManager.BATTERY_PLUGGED_USB -> {
                            binding.chargingResultView.text = "USB Plugged"
                            binding.chargingImageView.setImageBitmap(
                                BitmapFactory.decodeResource(resources, R.drawable.usb)
                            )
                        }

                        BatteryManager.BATTERY_PLUGGED_AC -> {
                            binding.chargingResultView.text = "AC Plugged"
                            binding.chargingImageView.setImageBitmap(
                                BitmapFactory.decodeResource(resources, R.drawable.ac)
                            )
                        }
                    }
                }

                else -> {
                    binding.chargingResultView.text = "No Plugged"
                }
            }

            // 배터리 퍼센트 계산 및 표시
            val level = getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val batteryPct = level / scale.toFloat() * 100
            binding.percentResultView.text = "$batteryPct %"
        }

        // ▶ 버튼 클릭 시 사용자 정의 브로드캐스트 전송 (MyReceiver로)
        binding.button.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Android 13(API 33)+ : POST_NOTIFICATIONS 권한 체크
                if (ContextCompat.checkSelfPermission(
                        this,
                        "android.permission.POST_NOTIFICATIONS"
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    // 권한 있음 → 브로드캐스트 전송
                    val intent = Intent(this, MyReceiver::class.java)
                    sendBroadcast(intent)
                } else {
                    // 권한 없음 → 요청
                    permissionLauncher.launch(
                        arrayOf("android.permission.POST_NOTIFICATIONS")
                    )
                }
            } else {
                // Android 12 이하: 바로 브로드캐스트 전송
                val intent = Intent(this, MyReceiver::class.java)
                sendBroadcast(intent)
            }
        }
    }
}