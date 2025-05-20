package com.agusteam.caribeando.presenter.common


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.presenter.formatting.NumberDefaults.MASK
import com.agusteam.caribeando.presenter.formatting.PhoneMaskTransformation
import com.agusteam.caribeando.presenter.theme.primary
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.hide_password
import caribeando.composeapp.generated.resources.show_password

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

    Column {
        OutlinedTextField(
            visualTransformation = if (keyboardType == KeyboardType.Password && !passwordVisible) {
                PasswordVisualTransformation()
            } else if (usePhoneMask) {
                PhoneMaskTransformation(MASK)
            } else {
                VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = TextFieldDefaults.colors(
                focusedLabelColor = primary,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White, // Background color when focused
                focusedIndicatorColor = primary,
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
            modifier = modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(25.dp)), placeholder = {
                Text(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    text = labelText
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            trailingIcon = {
                if (keyboardType == KeyboardType.Password) {
                    Box(Modifier.padding(8.dp).clickable { passwordVisible = !passwordVisible }) {
                        Text(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            text = stringResource(if (!passwordVisible) Res.string.show_password else Res.string.hide_password),
                            textDecoration = TextDecoration.Underline
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
                    color = Color.Red
                )
            }
        }
    }
}