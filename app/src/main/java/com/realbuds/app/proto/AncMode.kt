package com.realbuds.app.proto

/**
 * ANC modes for Realme Buds Air 7.
 *
 * Values are the on-the-wire values for command 0x404 (NOT the Realme UI
 * values; the device remaps them internally before applying).
 *
 * Confirmed by hand on real hardware. Matches the stock behaviour of the
 * structure: three top-level modes, with noise cancellation split into
 * Max, Moderate and Adaptive levels (no Mild on this model).
 */
enum class AncMode(
    val wire: Int,
    val label: String,
    val description: String,
    val group: Group,
) {
    OFF(1, "Off", "No noise control", Group.OFF),
    TRANSPARENCY(2, "Transparency", "Let in outside noise", Group.TRANSPARENCY),
    ANC_SMART(32, "Adaptive", "Earbuds adjust strength to your surroundings", Group.ANC),
    ANC_MAX(8, "Max", "Very noisy places - planes, trains", Group.ANC),
    ANC_MODERATE(16, "Moderate", "Noisy places - streets, malls", Group.ANC),
    ;

    enum class Group { OFF, ANC, TRANSPARENCY }

    companion object {
        /**
         * Wire value 0 also selects Off on some models — the Air 7 uses
         * wire 1 as its canonical Off, but wire 0 may still arrive as an
         * alias in status replies or push notifications.
         */
        const val WIRE_OFF_ALIAS = 0

        fun fromWire(v: Int): AncMode? = when (v) {
            0, 1 -> OFF
            else -> entries.firstOrNull { it.wire == v }
        }

        /**
         * Noise cancellation levels for Buds Air 7:
         * Adaptive (wire 32), Max (wire 8), Moderate (wire 16).
         */
        val ancLevels: List<AncMode>
            get() = listOf(ANC_SMART, ANC_MAX, ANC_MODERATE)

        /**
         * Which modes the "noise control" gesture cycles through.
         *
         * Sent with [Cmd.SET_ANC] action 2 (not action 1, which sets the
         * current mode). The value is a bitmask in its own numbering, NOT the
         * mode wire values: bit0 = Off, bit1 = Transparency, bit3 = ANC.
         *
         * Only these four values are accepted; anything else is silently
         * dropped. A sensible minimum is two modes, since a
         * minimum of two modes, so a one-mode "cycle" is never offered — and
         * an unconfigured cycle is why a noise-control gesture can fire while
         * nothing changes.
         */
        enum class Cycle(val wire: Int, val label: String) {
            ANC_OFF(9, "Cancellation and Off"),
            ANC_TRANSPARENCY(10, "Cancellation and Transparency"),
            OFF_TRANSPARENCY(3, "Off and Transparency"),
            ALL_THREE(11, "Cancellation, Transparency and Off"),
            ;

            companion object {
                fun byWire(wire: Int) = entries.firstOrNull { it.wire == wire }
                val DEFAULT = ALL_THREE
            }
        }

        /** Look up by wire value; used when restoring a persisted mode. */
        fun byWire(wire: Int): AncMode? = fromWire(wire)
    }
}
