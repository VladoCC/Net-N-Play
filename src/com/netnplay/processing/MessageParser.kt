package com.netnplay.processing

import com.netnplay.exceptions.EmptyParcelException
import com.netnplay.exceptions.ParsingNotFinishedException
import java.nio.ByteBuffer
import java.util.*
import java.util.stream.Collectors

/**
 * Parser designed to recreate json objects from incoming
 * byte buffers, which contains consecutive parts of json objects.
 *
 * General purpose of this class is to recreate serialized representations
 * of command objects, received through the network.
 * This step in the process allows [NetworkListener] to not worry
 * about the byte buffer size. Otherwise we'd need to know the length
 * of the biggest possible message and have a buffer that is at least as big
 * as this message.
 *
 * See [NetworkListener] for more information about receiving data through network.
 *
 * This class only checks message to be structurally correct,
 * but not to be a valid json. This checks are performed by [com.google.gson.Gson]
 * in the process of converting [String] to [com.netnplay.commands.ICommand].
 */
class MessageParser {
    /**
     * List of json objects, retrieved from incoming byte buffers.
     */
    private val objects = ArrayList<Position>()

    /**
     * Counter that stores information about the brackets found in the
     * incoming data earlier.
     */
    private var counter = -1

    /**
     * Builder intended for concatenating received strings.
     */
    private var builder = StringBuilder()

    /**
     * Reset function, that clears all the parsed objects and sets bracket counter to -1.
     */
    fun newMessage() {
        clear()
        counter = -1
    }

    /**
     * Returns true if the last buffer finished json object and not started
     * a new one and false otherwise.
     *
     * This conditions is equal to the fact that [objects] list is filled
     * with json objects from incoming buffers and there is no
     * unfinished objects in the last buffer.
     */
    fun hasObjects(): Boolean {
        return counter == 0
    }

    /**
     * Returns true if there is no incoming buffers or they had no brackets
     * Returns false otherwise.
     *
     * This condition is equal to the fact that there is no json objects
     * to get from the parser.
     */
    val isEmpty: Boolean
        get() = counter == -1

    /**
     * This method returns the list of all json objects parsed from buffers,
     * clears list an resets parser.
     * This is the only way to get objects from the parser.
     * Clearing parser after it is protection measure that's intended
     * to make sure that there is no problems with duplicated data
     * and unnecessary objects stored in memory.
     */
    @Throws(ParsingNotFinishedException::class)
    fun getAndClear(): List<String> {
        if (!hasObjects() && !isEmpty) {
            throw ParsingNotFinishedException()
        }
        val parcel = builder.toString()

        val result = objects.stream()
                .map<String> { p: Position -> return@map parcel.substring(p.start, p.end) }
                .collect(Collectors.toList())
        clear()
        return result
    }

    /**
     * Method for clearing buffers and getting rid of parsers internal data.
     */
    fun clear() {
        objects.clear()
        builder.clear()
    }

    /**
     * This method is designed for inserting new data into the parser.
     * It processes [parcel] and adds it to resulting string.
     */
    @Throws(EmptyParcelException::class)
    fun addParcel(parcel: ByteBuffer) {
        val pos = parcel.position()
        val newParcel = String(parcel.array()).substring(0, pos)
        if (newParcel.length == 0) {
            throw EmptyParcelException()
        }
        builder.append(newParcel)
        processParcel()
    }

    /**
     * This method is searching for json objects inside of the received string
     * and adds positions of this jsons to [objects] list.
     *
     * The algorithm behind it based on searching for opening and closing brackets
     * to decide whether we are inside of the json or just got out of it.
     */
    private fun processParcel() {
        var counter = 0
        var parcelStart = -1
        var parcelEnd: Int
        val parcel = builder.toString()
        for (i in 0 until parcel.length) {
            val elem = parcel[i]
            if (elem == '{') {
                if (counter == 0) {
                    parcelStart = i
                }
                counter++
            }
            if (elem == '}' && counter > 0) { //check counter to see if we are inside json or not
                counter--
                if (counter == 0) {
                    parcelEnd = i
                    objects.add(Position(parcelStart, parcelEnd + 1))
                }
            }
        }
        this.counter = counter
    }

    private data class Position(val start: Int, val end: Int)
}