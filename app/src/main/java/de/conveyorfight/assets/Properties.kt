package de.conveyorfight.assets

enum class Properties (val detail: PropertyDetail){
    Physical_Armor(PropertyDetail(false,false,
        listOf(RarityValue(1, 1, 8), RarityValue(2, 9, 15)))),
    Physical_Damage(PropertyDetail(false,true,
        listOf(RarityValue(1, 5, 10), RarityValue(2, 11, 20)))),
    Earth_Armor(PropertyDetail(true ,false,
        listOf(RarityValue(2, 9, 15), RarityValue(3, 16, 21)))),
    Earth_Damage(PropertyDetail(true ,false,
        listOf(RarityValue(2, 11, 20), RarityValue(3, 21, 35)))),
    Air_Armor(PropertyDetail(true ,false,
        listOf(RarityValue(2, 9, 15), RarityValue(3, 16, 21)))),
    Air_Damage(PropertyDetail(true ,false,
        listOf(RarityValue(2, 11, 20), RarityValue(3, 21, 35)))),
    Water_Armor(PropertyDetail(true ,false,
        listOf(RarityValue(2, 9, 15), RarityValue(3, 16, 21)))),
    Water_Damage(PropertyDetail(true ,false,
        listOf(RarityValue(2, 11, 20), RarityValue(3, 21, 35)))),
    Fire_Armor(PropertyDetail(true ,false,
        listOf(RarityValue(2, 9, 15), RarityValue(3, 16, 21)))),
    Fire_Damage(PropertyDetail(true ,false,
        listOf(RarityValue(2, 11, 20), RarityValue(3, 21, 35)))),
    Flash_Damage(PropertyDetail(false ,false,
        listOf(RarityValue(3, 15, 20)))),
}