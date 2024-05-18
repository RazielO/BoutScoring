package com.razielo.boutscoring.data.models.enums

enum class WeightClasses(val displayName: String) {
    MINIMUM("Minimumweight (105 lbs)"),
    JR_FLY("Jr Flyweight (108 lbs)"),
    FLY("Flyweight (112 lbs)"),
    SUPER_FLY("Super Flyweight (115 lbs)"),
    BANTAM("Bantamweight (118 lbs)"),
    SUPER_BANTAM("Super Bantamweight (122 lbs)"),
    FEATHER("Featherweight (126 lbs)"),
    SUPER_FEATHER("Super Featherweight (130 lbs)"),
    LIGHT("Lightweight (135 lbs)"),
    SUPER_LIGHT("Super Lightweight (140 lbs)"),
    WELTER("Welterweight (147 lbs)"),
    SUPER_WELTER("Super Welterweight (154 lbs)"),
    MIDDLE("Middleweight (160 lbs)"),
    SUPER_MIDDLE("Super Middleweight (168 lbs)"),
    LIGHT_HEAVY("Light Heavyweight (175 lbs)"),
    CRUISER("Cruiserweight (200 lbs)"),
    BRIDGER("Bridgerweight (224 lbs)"),
    HEAVY("Heavyweight (224+ lbs)"),
    CATCH("Catchweight");

    companion object {
        private val map = WeightClasses.entries.associateBy { it.displayName }

        infix fun fromDisplayName(displayName: String): WeightClasses? = map[displayName]
    }
}