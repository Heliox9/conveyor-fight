package de.conveyorfight.gameFragments.online

import de.conveyorfight.assets.Item

/**
 * data transfer object modeled after server implementation
 */
class ItemSelection(
    val selection: ArrayList<Item>,
    var bought: ArrayList<Item>,
    var saved: ArrayList<Item>
) {
    override fun toString(): String {
        return "ItemSelection(selection=$selection, bought=$bought, saved=$saved)"
    }
}