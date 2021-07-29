package de.conveyorfight.gameFragments.online

import de.conveyorfight.assets.Character

/**
 * data transfer object modeled after server implementation
 */
class GameState(val roundNumber: Int, val player: Character, val opponent: Character) {
    override fun toString(): String {
        return "GameState(roundNumber=$roundNumber, player=$player, opponent=$opponent)"
    }
}