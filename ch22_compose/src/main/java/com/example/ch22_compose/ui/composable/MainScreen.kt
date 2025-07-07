package com.example.ch22_compose.ui.composable

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ch22_compose.MyApplication
import com.example.ch22_compose.model.ItemModel
import com.example.ch22_compose.model.PageListModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("UnrememberedMutableState")
@Composable
fun MainScreen(modifier: Modifier = Modifier){
    //이전 상태를 저장할 필요가 없어서 remember 를 사용하지 않고 상태 선언
    //MutableList로 처리하게 되면 항목이 변경될 때마다 재구성이 이루어 질 수 있어서 List 로 처리
    val datas = mutableStateOf(listOf<ItemModel>())

    //재구성될 때마다 서버 연동이 필요 없어서 LaunchedEffect를 이용. key 가 변경되지 않는 한 {} 부분이 다시 실행되지 않음
    LaunchedEffect(true) {
        val call: Call<PageListModel> = MyApplication.networkService.getList(
            MyApplication.QUERY,
            MyApplication.API_KEY,
            1,
            10
        )
        call?.enqueue(object : Callback<PageListModel>{
            override fun onResponse(call: Call<PageListModel>, response: Response<PageListModel>) {
                if(response.isSuccessful){
                    datas.value = response.body()?.articles ?: listOf()
                }
            }

            override fun onFailure(call: Call<PageListModel>, t: Throwable) {
                Log.d("kkang", "error.....")
            }
        })

    }

    LazyColumn(modifier = modifier) {
        itemsIndexed(datas.value){ index, item ->
            Item(item)
            if(index < datas.value.lastIndex)
                HorizontalDivider(thickness = 1.dp, color = Color.Black)
        }
    }
}