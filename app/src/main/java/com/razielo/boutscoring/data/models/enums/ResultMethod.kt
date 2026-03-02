package com.razielo.boutscoring.data.models.enums

import androidx.annotation.StringRes

sealed interface ResultMethod {
    @get:StringRes
    val displayName: Int
    @get:StringRes
    val abbreviation: Int
}
