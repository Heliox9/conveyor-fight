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

    /**
     * push message to outgoing queue
     */
    fun addOutgoing(out: String) {
        outgoing.push(out)
    }

    /**
     * read the oldest message from incoming queue,
     * if none exist waits until the next message is pushed
     */
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

    /**
     * send message over socket
     */
    private fun sendToServer(s: String) {
        println("sending: $s")
        output.println(s)
    }

    /**
     * receive message from socket
     */
    private fun readFromServer(): String {
        val read = input.readLine()
        println("receiving: $read")
        return read
    }

    /**
     * executes handshake and loops to server the queues
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

    /**
     * push incoming message to queue if its not empty
     */
    private fun appendIncoming(inc: String) {
        println("attempting to add message to queue: $inc")
        if (inc.isNotEmpty()) incoming.add(inc)
        println("message queue size: ${incoming.size}")
        println("message queue: $incoming")
    }
}