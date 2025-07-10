package com.example.ch13_activity

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.ch13_activity.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {

    // ViewBinding 객체 선언
    lateinit var binding: ActivityAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 초기화 및 레이아웃 설정
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 커스텀 툴바를 액션바로 설정
        setSupportActionBar(binding.toolbar)
    }

    /**
     * 툴바에 메뉴 리소스를 연결하는 메서드
     * res/menu/menu_add.xml의 메뉴 항목을 툴바에 표시
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * 메뉴 항목 선택 시 실행되는 콜백 메서드
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        // "저장" 버튼이 클릭되었을 때
        R.id.menu_add_save -> {
            // EditText의 입력값을 Intent에 저장
            val intent = intent
            intent.putExtra("result", binding.addEditView.text.toString())

            // 이전 Activity로 결과 전달 후 종료
            setResult(Activity.RESULT_OK, intent)
            finish()
            true
        }

        // 그 외 항목은 처리하지 않음
        else -> true
    }
}