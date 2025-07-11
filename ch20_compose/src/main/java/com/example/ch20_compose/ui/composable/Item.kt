package com.example.ch20_compose.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ch20_compose.model.ItemModel

@Composable
fun Item(item: ItemModel) {
    // 수직으로 정렬된 Column 레이아웃, 패딩 10dp 적용
    Column(modifier = Modifier.padding(10.dp)) {

        // 뉴스 제목 표시 (null-safe 처리), 굵은 글씨, 큰 글꼴 크기
        Text(
            text = item.title ?: "",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        // 작성자와 발행일 표시, 이탤릭체
        Text(
            text = "${item.author} - ${item.publishedAt}",
            fontStyle = FontStyle.Italic
        )

        // 뉴스 설명 표시 (null-safe 처리)
        Text(
            text = item.description ?: ""
        )

        // 비동기 이미지 로드 (뉴스 이미지), Coil 라이브러리 사용
        AsyncImage(
            model = item.urlToImage,
            contentDescription = "news image"
        )
    }
}