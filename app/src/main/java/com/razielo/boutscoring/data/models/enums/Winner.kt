package com.razielo.boutscoring.data.models.enums

import androidx.annotation.StringRes
import com.razielo.boutscoring.R

enum class Winner(
    @get:StringRes val displayName: Int,
    @get:StringRes val abbreviation: Int
) {
    RED_CORNER(R.string.red_corner, R.string.win_abbr),
    BLUE_CORNER(R.string.blue_corner, R.string.loss_abbr),
    DRAW(R.string.draw, R.string.draw_abbr),
    NO_RESULT(R.string.no_contest, R.string.no_contest_abbr);
}