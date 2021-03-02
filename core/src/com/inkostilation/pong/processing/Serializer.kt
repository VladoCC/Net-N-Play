package com.inkostilation.pong.processing

import com.google.gson.*
import com.inkostilation.pong.commands.ICommand
import java.lang.reflect.Type
import kotlin.jvm.Throws

class Serializer {
    fun deserialize(json: String): ICommand<*, *> {
        val parcel = gson.fromJson(json, Parcel::class.java)
        val commandClass = Class.forName(parcel.commandClass) as Class<out ICommand<*, *>>
        return commandClass.cast(parcel.command)!!
    }

    fun deserialize(jsons: List<String>): List<ICommand<*, *>> {
        return jsons.map { deserialize(it) }
    }

    fun serialize(command: ICommand<*, *>): String {
        return gson.toJson(Parcel(command), Parcel::class.java)
    }

    fun serialize(commands: List<ICommand<*, *>>): List<String> {
        return commands.map { serialize(it) }
    }

    private class Parcel(var command: ICommand<*, *>) {
        var commandClass: String = command.javaClass.canonicalName
    }

    private class ParcelDeserializer : JsonDeserializer<Parcel> {
        @Throws(JsonParseException::class)
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Parcel {
            val jsonObject = json.asJsonObject
            val commandClass = jsonObject["commandClass"] as JsonPrimitive
            val className = commandClass.asString
            val commandJson = jsonObject["command"]
            val command: ICommand<*, *>
            command = context.deserialize<ICommand<*, *>>(commandJson, Class.forName(className))
            return Parcel(command)
        }
    }

    companion object {
        private val gson by lazy {
            val builder = GsonBuilder()
            builder.registerTypeAdapter(Parcel::class.java, ParcelDeserializer())
            builder.create()
        }
    }
}