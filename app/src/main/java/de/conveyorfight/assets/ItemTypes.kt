package de.conveyorfight.assets

enum class ItemTypes(val range: Int = 0) {
    Helmet, Gloves, Armor, Pants, Shoes,
    Special,
    RangeNull, RangeTwo(2), RangeFour(4),
    RangeSix(6),
}