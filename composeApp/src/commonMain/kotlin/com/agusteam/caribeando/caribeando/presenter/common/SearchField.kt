package com.agusteam.caribeando.presenter.common


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.agusteam.caribeando.presenter.theme.primary
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.search_hint

// 2026-08-20 (dark-mode audit): all three container-fill colors here were hardcoded
// `Color.White`, so this input box stayed a bright white rectangle in dark mode regardless of
// the system theme -- matches `SearchBar.kt`'s existing pattern of using `colors.fieldFill`
// for the same role. `primary` for the focus indicator is left as-is since it's identical to
// `colors.redFill` in both themes.
@Composable
fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = caribeandoColors
    OutlinedTextField(
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = colors.fieldFill,
            focusedContainerColor = colors.fieldFill, // Background color when focused
            focusedIndicatorColor = primary,
        ),
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .background(color = colors.fieldFill, shape = RoundedCornerShape(25.dp)), placeholder = {
            Text(
                text = stringResource(Res.string.search_hint),
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(8.dp)
    )
}