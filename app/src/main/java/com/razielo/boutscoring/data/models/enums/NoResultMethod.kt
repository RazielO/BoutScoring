package com.razielo.boutscoring.data.models.enums

import androidx.annotation.StringRes
import com.razielo.boutscoring.R

enum class NoResultMethod(
    @get:StringRes override val displayName: Int,
    @get:StringRes override val abbreviation: Int
) : ResultMethod {
    NO_CONTEST(R.string.no_contest, R.string.no_contest_abbr);
}