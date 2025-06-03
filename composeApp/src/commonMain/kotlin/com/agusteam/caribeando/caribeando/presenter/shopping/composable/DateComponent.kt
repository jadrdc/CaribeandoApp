package com.agusteam.caribeando.presenter.shopping.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.presenter.theme.CustomFontFamily
import com.agusteam.caribeando.presenter.theme.grey500

@Composable
fun DateComponent(title: String, subTitle: String) {
    Column {
        Text(
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = CustomFontFamily()
            ), text = title
        )
        Text(
            maxLines = 2,
            lineHeight = 24.sp,
            modifier = Modifier.padding(top = 8.dp).widthIn(max = 120.dp),
            style = TextStyle(fontWeight = FontWeight.Normal, color = grey500, fontSize = 14.sp),
            text = subTitle
        )
    }
}