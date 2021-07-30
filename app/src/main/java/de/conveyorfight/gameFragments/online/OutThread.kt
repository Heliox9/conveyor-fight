package de.conveyorfight.gameFragments.online

import java.io.PrintWriter
import java.util.*

class OutThread(val writer: PrintWriter) : Thread() {

    private val outgoing = LinkedList<String>()

    /**
     * push message to outgoing queue
     */
    fun addOutgoing(out: String) {

        println("adding to outgoing: $out")
        outgoing.add(out)
        println("values: ${outgoing.size}")
        println("list: $outgoing")

    }


    override fun run() {
        while (true) {
            if (outgoing.size > 0) sendToServer(outgoing.poll())
        }
    }

    /**
     * send message over socket
     */
    private fun sendToServer(s: String) {
        println("sending: $s")
        writer.println(s)
    }
}