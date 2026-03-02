package com.razielo.boutscoring.data.models.enums

import androidx.annotation.StringRes
import com.razielo.boutscoring.R

enum class WinMethod(
    @get:StringRes override val displayName: Int,
    @get:StringRes override val abbreviation: Int
) : ResultMethod {
    UNANIMOUS_DECISION(R.string.unanimous_decision, R.string.unanimous_decision_abbr),
    SPLIT_DECISION(R.string.split_decision, R.string.split_decision_abbr),
    MAJORITY_DECISION(R.string.majority_decision, R.string.majority_decision_abbr),
    POINTS(R.string.points, R.string.points_abbr),
    KNOCKOUT(R.string.knockout, R.string.knockout_abbr),
    TECHNICAL_KNOCKOUT(R.string.technical_knockout, R.string.technical_knockout_abbr),
    RETIRED(R.string.retired, R.string.retired_abbr),
    TECHNICAL_DECISION(R.string.technical_decision, R.string.technical_decision_abbr),
    DISQUALIFICATION(R.string.disqualification, R.string.disqualification_abbr);
}