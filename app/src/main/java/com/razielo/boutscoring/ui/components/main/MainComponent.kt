package com.razielo.boutscoring.ui.components.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.razielo.boutscoring.data.models.Bout

@Composable
fun MainComponent(bouts: List<Bout>, goToBout: (Int) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(16.dp)
    ) {
        items(bouts.size) { index ->
            BoutListCard(index, bouts[index], goToBout)
        }
    }
}
