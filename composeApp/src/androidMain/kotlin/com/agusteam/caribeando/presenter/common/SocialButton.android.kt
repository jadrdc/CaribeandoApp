package com.agusteam.caribeando.presenter.common

import android.app.Activity.RESULT_OK
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.google
import caribeando.composeapp.generated.resources.google_button
import com.agusteam.caribeando.domain.models.TokenMode
import com.agusteam.caribeando.presenter.social.SocialSignInEvent
import com.agusteam.caribeando.presenter.social.SocialSignViewModel
import com.agusteam.caribeando.presenter.theme.secondary
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel

@Composable
actual fun SocialButton(
    onLogin: (TokenMode) -> Unit
) {
    val socialViewModel: SocialSignViewModel = koinViewModel()
    val context = LocalContext.current

    val event = socialViewModel.events

    ObserveAsEvents(event) { event ->
        if (event is SocialSignInEvent.Success) {
            onLogin(event.data)
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                result.data?.let { intentData ->
                    socialViewModel.handleEvent(SocialSignInEvent.SignInSuccessful(intentData))
                } ?: showToast(context, "Sign-in successful but no data found")
            }

            else -> showToast(context, "Sign-in failed")
        }
    }

    GoogleSignInButton(
        onClick = { socialViewModel.handleEvent(SocialSignInEvent.Login(launcher)) }
    )
}

@Composable
private fun GoogleSignInButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, bottom = 16.dp)
            .border(1.dp, secondary, RoundedCornerShape(8.dp))
            .height(52.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(Res.drawable.google),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = stringResource(Res.string.google_button),
            style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
            color = secondary,
            modifier = Modifier.padding(start = 20.dp)
        )
    }
}

private fun showToast(context: android.content.Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}