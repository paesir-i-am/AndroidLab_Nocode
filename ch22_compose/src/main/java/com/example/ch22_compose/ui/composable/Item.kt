package com.example.ch22_compose.ui.composable

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
import com.example.ch22_compose.model.ItemModel

@Composable
fun Item(item: ItemModel){
   Column(modifier = Modifier.padding(10.dp)) {
       Text(item.title ?: "", fontSize = 20.sp, fontWeight = FontWeight.Bold)
       Text("${item.author} - ${item.publishedAt}", fontStyle = FontStyle.Italic)
       Text(item.description ?: "")
       AsyncImage(model = item.urlToImage, contentDescription = "news image")
   }
}