package com.netnplay.processing;

import com.google.gson.*;
import com.netnplay.commands.ICommand;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Custom serialization class that works with commands.
 * This realization is capable of recreating ICommand or
 * any subclass of it.
 *
 * This is the only Java class in this library.
 * Gson can't correctly deserialize Kotlin classes without specifying
 * concrete class, because of the way deserialization realized
 * for generics inside of Gson, which is strongly tied to
 * class structure of Java.
 */
public class Serializer {

    private static Gson gson;

    public Serializer() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Parcel.class, new ParcelDeserializer());
        gson = builder.create();
    }

    /**
     * Method for deserializing json objects to commands, using Parcel structure.
     * @param json stringified json object corresponding to command.
     * @return command with the class specified in Parcel object.
     */
    public ICommand deserialize(String json) {
        try {
            Parcel parcel = gson.fromJson(json, Parcel.class);
            Class<? extends ICommand> commandClass = null;
            commandClass = (Class<? extends ICommand>) Class.forName(parcel.commandClass);
            return commandClass.cast(parcel.command);
        } catch (ClassNotFoundException | JsonParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ICommand> deserialize(List<String> jsons) {
        return jsons.stream().map(j -> deserialize(j)).collect(Collectors.toList());
    }

    /**
     * Method that turns command into Parcel that makes a command
     * fully reproducible without losing subclass details.
     * @param command that needs to be serialized
     * @return json object that can be deserialized into command of the same
     * type as argument
     */
    public String serialize(ICommand command) {
        return gson.toJson(new Parcel(command), Parcel.class);
    }

    public String serialize(List<ICommand> commands) {
        StringBuilder message = new StringBuilder();
        commands.forEach(c -> message.append(serialize(c)));
        return message.toString();
    }

    /**
     * Simple data class that contains qll the information about command
     * that is important for serializing/deserializing it.
     */
    private final class Parcel {
        String commandClass;
        ICommand command;

        public Parcel(ICommand command) {
            this.command = command;
            commandClass = command.getClass().getCanonicalName();
        }
    }

    /**
     * Custom deserializer that extracts command class first and then tries to deserialize
     * supplied command as an object of the extracted class.
     */
    private final class ParcelDeserializer implements JsonDeserializer<Parcel> {

        @Override
        public Parcel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            final JsonObject jsonObject = json.getAsJsonObject();
            final JsonPrimitive commandClass = (JsonPrimitive) jsonObject.get("commandClass");
            if (commandClass == null) {
                throw new JsonParseException("Incorrect Parcel object: \"commandClass\" field not found. ");
            }
            final String className = commandClass.getAsString();
            final JsonElement commandJson = jsonObject.get("command");
            ICommand command;
            try {
                command = context.deserialize(commandJson, Class.forName(className));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                command = null;
            }
            return new Parcel(command);
        }
    }
}
