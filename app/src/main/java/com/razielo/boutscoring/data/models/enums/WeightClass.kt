package com.razielo.boutscoring.data.models.enums

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.razielo.boutscoring.R

enum class WeightClass(
    @param:StringRes val nameRes: Int,
    val maxPounds: Int?
) {
    HEAVY(R.string.weight_heavy, null),
    BRIDGER(R.string.weight_bridger, 224),
    CRUISER(R.string.weight_cruiser, 200),
    LIGHT_HEAVY(R.string.weight_light_heavy, 175),
    SUPER_MIDDLE(R.string.weight_super_middle, 168),
    MIDDLE(R.string.weight_middle, 160),
    SUPER_WELTER(R.string.weight_super_welter, 154),
    WELTER(R.string.weight_welter, 147),
    SUPER_LIGHT(R.string.weight_super_light, 140),
    LIGHT(R.string.weight_light, 135),
    SUPER_FEATHER(R.string.weight_super_feather, 130),
    FEATHER(R.string.weight_feather, 126),
    SUPER_BANTAM(R.string.weight_super_bantam, 122),
    BANTAM(R.string.weight_bantam, 118),
    SUPER_FLY(R.string.weight_super_fly, 115),
    FLY(R.string.weight_fly, 112),
    JR_FLY(R.string.weight_jr_fly, 108),
    MINIMUM(R.string.weight_minimum, 105),
    CATCH(R.string.weight_catch, null);

    companion object {
        @Composable
        fun WeightClass.displayName(): String {
            return when (this) {
                CATCH -> stringResource(nameRes)

                HEAVY ->
                    stringResource(
                        R.string.weight_heavy_format,
                        stringResource(nameRes), 224
                    )

                else ->
                    stringResource(
                        R.string.weight_format,
                        stringResource(nameRes), maxPounds!!
                    )
            }
        }
    }
}