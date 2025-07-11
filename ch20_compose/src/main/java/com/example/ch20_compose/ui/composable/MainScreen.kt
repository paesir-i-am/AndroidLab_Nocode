package com.example.ch20_compose.ui.composable

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
import com.example.ch20_compose.MyApplication
import com.example.ch20_compose.model.ItemModel
import com.example.ch20_compose.model.PageListModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("UnrememberedMutableState") // 상태를 remember 없이 선언할 때 lint 경고 억제
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    // 상태 변수 선언: 초기값은 빈 리스트. 단순 데이터를 담기 위한 목적이므로 remember 없이 사용
    val datas = mutableStateOf(listOf<ItemModel>())

    // 재구성될 때마다 서버 연동이 필요 없어서 LaunchedEffect를 이용. key 가 변경되지 않는 한 {} 부분이 다시 실행되지 않음
    // 최초 1회만 실행: 재구성 시 서버 재호출 방지를 위해 key = true
    LaunchedEffect(true) {
        val call: Call<PageListModel> = MyApplication.networkService.getList(
            MyApplication.QUERY,     // 검색 키워드
            MyApplication.API_KEY,   // API 키
            1,                       // 페이지 번호
            10                       // 페이지당 항목 수
        )

        // 비동기 네트워크 요청 수행
        call.enqueue(object : Callback<PageListModel> {
            override fun onResponse(call: Call<PageListModel>, response: Response<PageListModel>) {
                if (response.isSuccessful) {
                    // 응답 데이터가 정상일 경우 articles 리스트로 상태 갱신
                    datas.value = response.body()?.articles ?: listOf()
                }
            }

            override fun onFailure(call: Call<PageListModel>, t: Throwable) {
                Log.d("kkang", "error.....") // 네트워크 오류 로그 출력
            }
        })
    }

    // 리스트 UI 구성
    LazyColumn(modifier = modifier) {
        itemsIndexed(datas.value) { index, item ->
            Item(item) // 각 항목 렌더링

            // 항목 사이에 구분선 추가 (마지막 항목 제외)
            if (index < datas.value.lastIndex)
                HorizontalDivider(thickness = 1.dp, color = Color.Black)
        }
    }
}