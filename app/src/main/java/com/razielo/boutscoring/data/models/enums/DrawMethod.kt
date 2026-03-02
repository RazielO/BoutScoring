package com.razielo.boutscoring.data.models.enums

import androidx.annotation.StringRes
import com.razielo.boutscoring.R

enum class DrawMethod(
    @get:StringRes override val displayName: Int,
    @get:StringRes override val abbreviation: Int
) : ResultMethod {
    TECHNICAL_DRAW(R.string.technical_draw, R.string.technical_draw_abbr),
    DRAW(R.string.draw, R.string.draw_abbr),
    SPLIT_DRAW(R.string.split_draw, R.string.split_draw_abbr),
    MAJORITY_DRAW(R.string.majority_draw, R.string.majority_draw_abbr);
}