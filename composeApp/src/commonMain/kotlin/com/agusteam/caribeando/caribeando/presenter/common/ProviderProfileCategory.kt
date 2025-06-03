package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.agusteam.caribeando.domain.models.CategoryModel
import com.agusteam.caribeando.presenter.theme.primary
import com.agusteam.caribeando.presenter.theme.secondary
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.trip_categories

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProviderProfileCategory(
    modifier: Modifier,
    categoriesModel: List<CategoryModel>,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(Res.string.trip_categories),
            color = secondary, fontSize = 16.sp, fontWeight = FontWeight.SemiBold
        )
        FlowRow(
            modifier = Modifier.padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp), // Horizontal spacing between chips
        ) {
            categoriesModel.forEach { category ->
                AsyncImage(
                    model = category.image,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.size(34.dp),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(primary)
                )
            }
        }
        HorizontalDivider(Modifier.padding(top = 16.dp))
    }
}