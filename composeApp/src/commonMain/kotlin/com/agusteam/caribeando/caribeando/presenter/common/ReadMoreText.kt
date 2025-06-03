package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.presenter.theme.grey500
import com.agusteam.caribeando.presenter.theme.secondary
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.read_less
import caribeando.composeapp.generated.resources.read_more

@Composable
fun ReadMoreText(
    text: String,
    collapsedMaxLines: Int = 5
) {
    var isExpanded by remember { mutableStateOf(false) }
    val toggleText =
        if (isExpanded) stringResource(Res.string.read_less) else stringResource(Res.string.read_more)

    Column {
        Text(
            modifier = Modifier.padding(top = 16.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = grey500,
            text = text,
            maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLines,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = toggleText,
            color = secondary,
            fontSize = 16.sp,
            textDecoration = TextDecoration.Underline,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .clickable { isExpanded = !isExpanded }
                .padding(top = 8.dp)
        )
    }
}