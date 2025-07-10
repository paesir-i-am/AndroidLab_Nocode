package com.example.ch17_database

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ch17_database.databinding.ItemRecyclerviewBinding

// RecyclerView의 각 항목을 보관하는 ViewHolder 정의
class MyViewHolder(val binding: ItemRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root)

// RecyclerView의 어댑터 클래스 정의
class MyAdapter(val datas: MutableList<String>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 항목 개수 반환
    override fun getItemCount(): Int {
        return datas?.size ?: 0  // 데이터가 null이면 0, 아니면 리스트 크기 반환
    }

    // 새로운 ViewHolder 객체를 생성하여 반환
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyViewHolder(  // 커스텀 ViewHolder 객체 생성
            ItemRecyclerviewBinding.inflate(  // XML 레이아웃을 바인딩 객체로 변환
                LayoutInflater.from(parent.context),  // 부모 컨텍스트로부터 LayoutInflater 생성
                parent,
                false  // 바로 attach하지 않음
            )
        )

    // ViewHolder에 데이터를 바인딩하는 메서드
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding  // ViewHolder를 MyViewHolder로 캐스팅하여 바인딩 객체 참조
        binding.itemData.text = datas!![position]  // 해당 위치의 데이터를 TextView에 설정
    }
}