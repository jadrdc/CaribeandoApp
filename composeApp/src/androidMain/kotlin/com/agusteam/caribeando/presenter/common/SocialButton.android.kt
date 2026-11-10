package com.agusteam.caribeando.presenter.common

import android.app.Activity.RESULT_OK
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.google
import caribeando.composeapp.generated.resources.google_button
import com.agusteam.caribeando.domain.models.TokenMode
import com.agusteam.caribeando.presenter.social.SocialSignInEvent
import com.agusteam.caribeando.presenter.social.SocialSignViewModel
import com.agusteam.caribeando.presenter.theme.Radii
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import com.agusteam.caribeando.presenter.theme.caribeandoTextStyles
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

/**
 * The handoff's "SecondaryButton" chrome applied to the Google sign-in row: "Full width,
 * transparent fill, 1dp #E2E2E2 border, radius 14, 14dp padding, label 600/14 in #003049," with
 * "google.png at 18." (was a flat 8dp-radius border with a plain 16sp label and no design-token
 * color). Vertical spacing around the button now lives in the caller (LoginScreen) instead of
 * being baked into this component, matching how `ActionButton`/`SecondaryButton` don't self-pad
 * either.
 */
@Composable
private fun GoogleSignInButton(onClick: () -> Unit) {
    val colors = caribeandoColors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, colors.panelFieldBorder, Radii.buttonShape)
            .bounceClick(scaleDown = 0.97f) { onClick() }
            .padding(14.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(Res.drawable.google),
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )

        Text(
            text = stringResource(Res.string.google_button),
            style = caribeandoTextStyles().primaryButtonLabel.copy(fontSize = 14.sp),
            color = colors.textPrimary,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

private fun showToast(context: android.content.Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}
