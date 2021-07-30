package de.conveyorfight.assets

data class PropertyDetail(
    val isDamage: Boolean,
    val blockedBy: Properties?,
    val rarities: List<RarityValue>
)
