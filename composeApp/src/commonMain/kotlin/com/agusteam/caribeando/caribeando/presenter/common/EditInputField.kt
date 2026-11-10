package com.agusteam.caribeando.presenter.common


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.presenter.formatting.NumberDefaults.MASK
import com.agusteam.caribeando.presenter.formatting.PhoneMaskTransformation
import com.agusteam.caribeando.presenter.theme.Radii
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import com.agusteam.caribeando.presenter.theme.caribeandoTextStyles
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.hide_password
import caribeando.composeapp.generated.resources.show_password

/**
 * The handoff's "Field": "Fill #F4F5F6, radius 14, padding 11 x 15. Caps label 500/10 #5F6A70
 * above the value; value 400/14 #003049, 3dp below the label. No border, no focus ring; focus is
 * the caret."
 *
 * 2026-08-20 (design handoff pass): switched from `placeholder` (text that disappears once you
 * type) to `label` (M3 floats it above the value once focused/filled) -- closer to the spec's
 * "caps label always above the value," though M3's floating-label animation isn't pixel-identical
 * to the spec's static two-line layout. Fill moved from plain white to `fieldFill` (theme-aware);
 * both border colors are now transparent so focus is only the caret, per spec. Radius moved
 * 16dp -> Radii.button (14dp), correcting the earlier "modernize pass" guess to the exact spec.
 */
@Composable
fun EditInputField(
    modifier: Modifier = Modifier,
    query: String,
    labelText: String,
    onQueryChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Unspecified,
    error: Boolean = false,
    errorText: String = "",
    usePhoneMask: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val colors = caribeandoColors
    val textStyles = caribeandoTextStyles()

    Column(modifier = modifier) {
        OutlinedTextField(
            visualTransformation = if (keyboardType == KeyboardType.Password && !passwordVisible) {
                PasswordVisualTransformation()
            } else if (usePhoneMask) {
                PhoneMaskTransformation(MASK)
            } else {
                VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colors.fieldFill,
                unfocusedContainerColor = colors.fieldFill,
                disabledContainerColor = colors.fieldFill,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                focusedLabelColor = colors.textSecondary,
                unfocusedLabelColor = colors.textSecondary,
                focusedTextColor = colors.textPrimary,
                unfocusedTextColor = colors.textPrimary,
                cursorColor = colors.redFill,
            ),
            value = query,
            onValueChange = {
                if (usePhoneMask) {
                    if (it.length <= 10)
                        onQueryChange(it)
                } else {
                    onQueryChange(it)
                }
            },
            modifier = Modifier
                .fillMaxWidth(),
            label = {
                Text(
                    text = labelText.uppercase(),
                    style = textStyles.fieldLabelCaps
                )
            },
            textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal),
            singleLine = true,
            shape = Radii.buttonShape,
            trailingIcon = {
                if (keyboardType == KeyboardType.Password) {
                    // 2026-08-20 (design handoff pass): "trailing Mostrar label, 500/11.5 #003049" --
                    // was a generic 14sp/SemiBold underlined text.
                    Box(Modifier.padding(8.dp).bounceClick(scaleDown = 0.95f, indication = null) { passwordVisible = !passwordVisible }) {
                        Text(
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = colors.textPrimary,
                            text = stringResource(if (!passwordVisible) Res.string.show_password else Res.string.hide_password),
                        )
                    }

                }
            }
        )
        if (error && errorText.isNotBlank()) {
            Box(Modifier.padding(top = 8.dp)) {
                Text(
                    text = errorText, fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.redText // was errorColor -- "red as text" is its own role per the handoff
                )
            }
        }
    }
}
