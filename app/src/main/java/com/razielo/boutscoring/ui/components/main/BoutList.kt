package com.razielo.boutscoring.ui.components.main

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.razielo.boutscoring.ui.models.ParsedBout

/**
 * Display a list of saved bouts
 */
@Composable
fun BoutList(
    bouts: List<ParsedBout>,
    goToBout: (Int) -> Unit,
    deleteBout: (ParsedBout) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 6.dp)
    ) {
        items(bouts.size) { index ->
            BoutListCard(index, bouts[index], goToBout, deleteBout)
        }
    }
}

/**
 * Preview for the bout list
 */
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BoutListPreview() {
    BoutList(
        bouts = listOf(ParsedBout.example(), ParsedBout.example(), ParsedBout.example()),
        goToBout = {},
        deleteBout = {},
    )
}