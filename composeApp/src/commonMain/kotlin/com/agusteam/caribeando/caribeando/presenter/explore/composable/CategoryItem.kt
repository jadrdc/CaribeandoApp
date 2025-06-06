package com.agusteam.caribeando.presenter.explore.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.agusteam.caribeando.domain.models.CategoryModel
import com.agusteam.caribeando.presenter.theme.CustomFontFamily
import com.agusteam.caribeando.presenter.theme.grey500
import com.agusteam.caribeando.presenter.theme.primary

@Composable
fun CategoryItem(
    item: CategoryModel,
    onSelected: (CategoryModel) -> Unit
) {
    val color = if (item.isSelected) primary else grey500

    Column(Modifier.clickable(interactionSource = null, indication = null) {
        onSelected(item)
    }, horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            model = item.image,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(32.dp),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color)
        )
        Text(
            text = item.description,
            color = color,
            modifier = Modifier.padding(top = 4.dp),
            fontWeight = FontWeight.SemiBold,
            fontFamily = CustomFontFamily(),
            fontSize = 12.sp
        )
    }
}
