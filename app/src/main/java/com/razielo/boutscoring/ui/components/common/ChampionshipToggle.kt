package com.razielo.boutscoring.ui.components.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.razielo.boutscoring.R
import com.razielo.boutscoring.ui.theme.BoutScoringTheme
import com.razielo.boutscoring.ui.theme.goldContainerDark


@Composable
fun ChampionshipToggle(championship: Boolean, toggle: (Boolean) -> Unit) {
    OutlinedCard(
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Image(
                    painterResource(R.drawable.belt),
                    contentDescription = null,
                    modifier = Modifier.height(16.dp)
                )
                Text(
                    stringResource(R.string.championship_in_play),
                    Modifier.padding(end = 16.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Switch(
                championship,
                onCheckedChange = toggle,
                colors = SwitchDefaults.colors().copy(
                    checkedTrackColor = goldContainerDark
                ),
            )
        }
    }
}

@Preview
@Composable
private fun ChampionshipTogglePreview() {
    BoutScoringTheme {
        ChampionshipToggle(championship = true, toggle = {})
    }
}
