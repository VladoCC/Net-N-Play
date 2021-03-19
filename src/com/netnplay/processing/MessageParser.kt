package com.netnplay.processing

import com.netnplay.exceptions.EmptyParcelException
import com.netnplay.exceptions.ParsingNotFinishedException
import java.nio.ByteBuffer
import java.util.*
import java.util.stream.Collectors

class MessageParser {
    private val objects = ArrayList<Position>()
    private var counter = -1
    private var builder = StringBuilder()

    fun newMessage() {
        clear()
        counter = -1
    }

    fun hasObjects(): Boolean {
        return counter == 0
    }

    val isEmpty: Boolean
        get() = counter == -1

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

    fun clear() {
        objects.clear()
        builder.clear()
    }

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
            if (elem == '}' && counter > 0) { //check counter to see if we are in json or not
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