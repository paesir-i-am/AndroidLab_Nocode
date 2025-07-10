package com.example.ch13_activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ch13_activity.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // ViewBinding 객체 선언
    lateinit var binding: ActivityMainBinding

    // RecyclerView에 표시할 데이터 리스트
    var datas: MutableList<String>? = null

    // 커스텀 어댑터 선언
    lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 객체 초기화 및 레이아웃 설정
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * [ActivityResultLauncher]
         * - registerForActivityResult()를 사용해 새 Activity에서 결과를 받아올 수 있는 런처를 생성
         * - AddActivity에서 받아온 문자열을 datas에 추가하고 adapter를 갱신
         */
        val requestLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            it.data!!.getStringExtra("result")?.let { result ->
                datas?.add(result)
                adapter.notifyDataSetChanged()
            }
        }

        // FloatingActionButton 클릭 시 AddActivity 실행
        binding.mainFab.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            requestLauncher.launch(intent)
        }

        /**
         * savedInstanceState가 존재하면 이전에 저장된 데이터를 복원
         * 그렇지 않으면 새로운 빈 리스트를 생성
         */
        datas = savedInstanceState?.let {
            it.getStringArrayList("datas")?.toMutableList()
        } ?: mutableListOf()

        // RecyclerView 설정
        val layoutManager = LinearLayoutManager(this)  // 세로 리스트
        binding.mainRecyclerView.layoutManager = layoutManager

        adapter = MyAdapter(datas)  // 어댑터에 데이터 연결
        binding.mainRecyclerView.adapter = adapter

        // 항목 사이 구분선 추가
        binding.mainRecyclerView.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )
    }

    /**
     * [onSaveInstanceState]
     * - 화면 회전 등의 상황에서 현재 데이터를 임시 저장
     * - key "datas"로 리스트 저장
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList("datas", ArrayList(datas))
    }
}