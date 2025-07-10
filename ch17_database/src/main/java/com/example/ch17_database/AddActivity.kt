package com.example.ch17_database

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.ch17_database.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {

    // ViewBinding 객체 선언
    lateinit var binding: ActivityAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 초기화 및 레이아웃 설정
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 툴바를 액션바로 설정
        setSupportActionBar(binding.toolbar)
    }

    // 옵션 메뉴 생성 (우측 상단 메뉴)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // menu_add.xml 파일의 메뉴를 현재 액티비티에 적용
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // 옵션 메뉴 항목 클릭 시 호출되는 함수
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {

        // '저장' 버튼(menu_add_save) 클릭 시 동작
        R.id.menu_add_save -> {
            // EditText에 입력된 텍스트 가져오기
            val inputData = binding.addEditView.text.toString()

            // SQLite DB에 입력값 저장
            val db = DBHelper(this).writableDatabase
            db.execSQL(
                "insert into TODO_TB (todo) values (?)", // 파라미터 바인딩을 통한 안전한 쿼리
                arrayOf<String>(inputData)
            )
            db.close() // DB 닫기

            // 결과값을 이전 액티비티로 전달
            val intent = intent
            intent.putExtra("result", inputData) // 결과 데이터 담기
            setResult(Activity.RESULT_OK, intent) // 성공 결과 설정
            finish() // 현재 액티비티 종료
            true
        }

        // 그 외의 메뉴 항목 처리 (기본값)
        else -> true
    }
}