package de.conveyorfight.gameFragments.online

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.util.*

class ClientThread : Thread() {
    private lateinit var socket: Socket
    private lateinit var output: PrintWriter
    private lateinit var input: BufferedReader
    private val incoming = LinkedList<String>()
    private val outgoing = LinkedList<String>()

    fun addOutgoing(out: String) {
        outgoing.push(out)
    }

    fun getNextIncoming(): String {

        while (incoming.size == 0) {
            sleep(100)
        }

        val polled: String = incoming.poll()!!
        println("polled: $polled")
        println("values left: ${incoming.size}")
        println("list: $incoming")
        return polled


    }

    private fun sendToServer(s: String) {
        println("sending: $s")
        output.println(s)
    }

    private fun readFromServer(): String {
        val read = input.readLine()
        println("receiving: $read")
        return read
    }

    /**
     * If this thread was constructed using a separate
     * `Runnable` run object, then that
     * `Runnable` object's `run` method is called;
     * otherwise, this method does nothing and returns.
     *
     *
     * Subclasses of `Thread` should override this method.
     *
     * @see .start
     * @see .stop
     * @see .Thread
     */
    override fun run() {
        // socket creation
        socket = Socket("192.168.178.233", 88)// TODO change/ make configurable
        output = PrintWriter(socket.getOutputStream(), true)
        input = BufferedReader(InputStreamReader(socket.getInputStream()))


        // server handshake
        sendToServer("App")// sending name
        readFromServer()// game id
        readFromServer()//opponent

        println("handshake complete starting iteration")
        println("|")
        println("|")
        println("V")

        // always up loop to send and receive messages
        while (true) {
            appendIncoming(readFromServer())
            if (outgoing.size > 0) sendToServer(outgoing.pop())
        }
    }

    private fun appendIncoming(inc: String) {
        println("attempting to add message to queue: $inc")
        if (inc.isNotEmpty()) incoming.add(inc)
        println("message queue size: ${incoming.size}")
        println("message queue: $incoming")
    }
}