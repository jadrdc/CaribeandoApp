package com.agusteam.caribeando.presenter.profile.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.agusteam.caribeando.domain.models.ProfileDataModel

@Composable
fun ProfileDetailItemSection(profileList: List<ProfileDataModel>, isLoading: Boolean) {
    if (isLoading)
        Column(
            modifier = Modifier.padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ProfileItemShimmer()
            HorizontalDivider(Modifier.padding(top = 16.dp))
            ProfileItemShimmer()
            HorizontalDivider(Modifier.padding(top = 16.dp))
            ProfileItemShimmer()
            HorizontalDivider(Modifier.padding(top = 16.dp))
            ProfileItemShimmer()
        } else
        LazyColumn(

            modifier = Modifier.padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(profileList) { item ->
                ProfileItem(item)
            }
        }
}