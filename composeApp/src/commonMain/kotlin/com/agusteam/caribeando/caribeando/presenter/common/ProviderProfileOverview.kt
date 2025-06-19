package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.domain.models.ProfileDataModel
import com.agusteam.caribeando.domain.models.TripProviderModel
import com.agusteam.caribeando.presenter.theme.primary
import com.agusteam.caribeando.presenter.theme.secondary
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.address
import caribeando.composeapp.generated.resources.email
import caribeando.composeapp.generated.resources.phone

@Composable
fun ProviderProfileOverview(tripProviderModel: TripProviderModel) {
    val profileList = listOf(
        ProfileDataModel(
            title = stringResource(Res.string.address),
            description = tripProviderModel.address,
            icon = Icons.Filled.Home
        ),
      /*  ProfileDataModel(
            title = stringResource(Res.string.phone),
            description = tripProviderModel.phone,
            icon = Icons.Filled.Phone
        ),
        ProfileDataModel(
            title = stringResource(Res.string.email),
            description = tripProviderModel.email,
            icon = Icons.Filled.Email
        ),*/
    )
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        profileList.forEach { profile ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top,
            ) {

                Icon(
                    tint = primary,
                    imageVector = profile.icon,
                    modifier = Modifier.size(24.dp),
                    contentDescription = null
                )

                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = profile.description,
                    color = secondary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }


        if (tripProviderModel.description.isNotEmpty()) {
            ReadMoreText(
                text = tripProviderModel.description,
            )
        }
        HorizontalDivider(Modifier.padding(top = 16.dp))
    }
}