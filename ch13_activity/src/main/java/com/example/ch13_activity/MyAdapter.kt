package com.example.ch13_activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ch13_activity.databinding.ItemRecyclerviewBinding

// RecyclerView의 각 아이템을 담을 ViewHolder 클래스 정의
class MyViewHolder(val binding: ItemRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)

// RecyclerView.Adapter 구현
class MyAdapter(val datas: MutableList<String>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 아이템 개수 반환
    override fun getItemCount(): Int {
        // datas가 null이면 0, 아니면 크기 반환
        return datas?.size ?: 0
    }

    /**
     * ViewHolder 생성 메서드
     * 새로운 ViewHolder 객체를 생성하여 반환한다.
     * ViewHolder는 아이템 하나를 표시하는 뷰를 담고 있음
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // XML 레이아웃을 ViewBinding으로 inflate
        val binding = ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    /**
     * ViewHolder에 데이터를 연결하는 메서드
     * position 위치에 해당하는 데이터를 View에 설정함
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        binding.itemData.text = datas!![position] // 해당 위치의 텍스트를 텍스트뷰에 설정
    }
}