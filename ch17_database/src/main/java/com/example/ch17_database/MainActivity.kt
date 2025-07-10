package com.example.ch17_database

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ch17_database.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // 뷰 바인딩 객체
    lateinit var binding: ActivityMainBinding

    // RecyclerView에 표시할 데이터 리스트
    var datas: MutableList<String>? = null

    // RecyclerView 어댑터
    lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰 바인딩 초기화 및 레이아웃 설정
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 툴바를 액션바로 설정
        setSupportActionBar(binding.toolbar)

        // ActivityResult API: AddActivity에서 결과를 받아옴
        val requestLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            // AddActivity로부터 전달된 결과 처리
            it.data!!.getStringExtra("result")?.let {
                datas?.add(it)                // 결과 문자열 리스트에 추가
                adapter.notifyDataSetChanged() // 어댑터 갱신
            }
        }

        // FAB 클릭 시 AddActivity 실행
        binding.mainFab.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            requestLauncher.launch(intent)
        }

        // 데이터 리스트 초기화
        datas = mutableListOf<String>()

        // SQLite DB 읽기
        val db = DBHelper(this).readableDatabase
        val cursor = db.rawQuery("select * from TODO_TB", null)
        cursor.run {
            while (moveToNext()) {
                // 두 번째 컬럼(todo 문자열)만 가져와서 리스트에 추가
                datas?.add(cursor.getString(1))
            }
        }
        db.close()

        // RecyclerView 설정
        val layoutManager = LinearLayoutManager(this)
        binding.mainRecyclerView.layoutManager = layoutManager

        adapter = MyAdapter(datas)
        binding.mainRecyclerView.adapter = adapter

        // 구분선 추가
        binding.mainRecyclerView.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )
    }

    // 툴바에 메뉴 아이템 연결
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // 메뉴 아이템 클릭 이벤트 처리
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 설정 메뉴가 클릭된 경우
        if (item.itemId === R.id.menu_main_setting) {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}