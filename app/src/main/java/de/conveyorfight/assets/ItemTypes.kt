package de.conveyorfight.assets

import java.util.*


enum class ItemTypes(val range: Int = 0) {
    Helmet, Gloves, Armor, Pants, Shoes,
    Special,
    RangeNull, RangeTwo(2), RangeFour(4),
    RangeSix(6);

    companion object {

        fun valueOfCaseInsensitive(string: String): ItemTypes? {
            for (i in values()) {
                if (i.toString().uppercase() == string.uppercase(Locale.getDefault())) return i
            }
            return null
        }
    }

}