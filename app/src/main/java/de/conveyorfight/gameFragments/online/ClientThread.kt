package de.conveyorfight.gameFragments.online

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class ClientThread : Thread() {
    lateinit var socket: Socket
    lateinit var output: PrintWriter
    lateinit var input: BufferedReader

    public fun sendToServer(s: String) {
        output.println(s)
    }

    public fun readFromServer(): String {
        return input.readLine()
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
        sendToServer("App")
        println("Game ID: " + readFromServer())
        println("Opponent: " + readFromServer())

        while (true) {
            // dummy
        }
    }
}