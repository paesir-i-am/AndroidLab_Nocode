package com.example.ch15_service

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.ch15_outer.MyAIDLInterface
import com.example.ch15_service.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var connectionMode = "none" // AIDL 서비스 연결 상태 관리용

    // AIDL 관련 변수 선언
    var aidlService: MyAIDLInterface? = null // 외부 서비스에서 넘겨받은 AIDL 객체
    var aidlJob: Job? = null // 코루틴 작업 객체

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // AIDL 서비스와 연결할 UI 리스너 등록
        onCreateAIDLService()

        // JobScheduler 초기화 전에 권한 요청 처리
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            if (it.all { permission -> permission.value == true }) {
                onCreateJobScheduler()
            } else {
                Toast.makeText(this, "permission denied...", Toast.LENGTH_SHORT).show()
            }
        }

        // Android 13 이상에서는 POST_NOTIFICATIONS 권한이 런타임 권한이므로 확인 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    "android.permission.POST_NOTIFICATIONS"
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                onCreateJobScheduler()
            } else {
                permissionLauncher.launch(
                    arrayOf("android.permission.POST_NOTIFICATIONS")
                )
            }
        } else {
            onCreateJobScheduler()
        }
    }

    override fun onStop() {
        super.onStop()
        // AIDL 서비스가 연결되어 있다면 해제
        if (connectionMode === "aidl") {
            onStopAIDLService()
        }
        connectionMode = "none"
        changeViewEnable()
    }

    // UI 상태를 현재 연결 상태에 따라 변경
    fun changeViewEnable() = when (connectionMode) {
        "aidl" -> {
            binding.aidlPlay.isEnabled = false
            binding.aidlStop.isEnabled = true
        }
        else -> {
            binding.aidlPlay.isEnabled = true
            binding.aidlStop.isEnabled = false
            binding.aidlProgress.progress = 0
        }
    }

    // AIDL 서비스 연결 콜백 정의
    val aidlConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            aidlService = MyAIDLInterface.Stub.asInterface(service)
            aidlService!!.start() // 음악 재생 시작
            binding.aidlProgress.max = aidlService!!.maxDuiration

            // 코루틴으로 ProgressBar 업데이트
            val backgroundScope = CoroutineScope(Dispatchers.Default + Job())
            aidlJob = backgroundScope.launch {
                while (binding.aidlProgress.progress < binding.aidlProgress.max) {
                    delay(1000)
                    binding.aidlProgress.incrementProgressBy(1000)
                }
            }

            connectionMode = "aidl"
            changeViewEnable()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            aidlService = null
        }
    }

    // AIDL 서비스 연결 및 버튼 클릭 리스너 등록
    private fun onCreateAIDLService() {
        binding.aidlPlay.setOnClickListener {
            val intent = Intent("ACTION_SERVICE_AIDL") // AIDL 서비스 인텐트
            intent.setPackage("com.example.ch15_outer") // 외부 앱 패키지명 지정
            bindService(intent, aidlConnection, Context.BIND_AUTO_CREATE)
        }

        binding.aidlStop.setOnClickListener {
            aidlService!!.stop() // 음악 정지
            unbindService(aidlConnection) // 서비스 연결 해제
            aidlJob?.cancel() // 코루틴 중단
            connectionMode = "none"
            changeViewEnable()
        }
    }

    private fun onStopAIDLService() {
        unbindService(aidlConnection)
    }

    // JobScheduler를 통한 백그라운드 작업 예약
    private fun onCreateJobScheduler() {
        val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        val builder = JobInfo.Builder(1, ComponentName(this, MyJobService::class.java))
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED) // Wi-Fi 연결 필요
        val jobInfo = builder.build()
        jobScheduler.schedule(jobInfo) // 작업 예약
    }
}