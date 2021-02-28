package com.inkostilation.pong.desktop.network;

import com.inkostilation.pong.commands.*;
import com.inkostilation.pong.commands.request.RequestScoreCommand;
import com.inkostilation.pong.desktop.notification.ClientNotifier;
import com.inkostilation.pong.engine.IEngine;
import com.inkostilation.pong.exceptions.NoEngineException;
import com.inkostilation.pong.processing.NetworkConnection;
import com.inkostilation.pong.processing.Serializer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ServerEngine implements IEngine<Void> {

    private static final String host = "localhost";
    private static final int port = 8080;

    private SocketChannel channel;
    private Serializer serializer;

    private boolean connected = false;

    private List<AbstractRequestCommand> commandQueue = Collections.synchronizedList(new ArrayList<>());

    ServerEngine() {
        serializer = new Serializer();
    }

    private void addCommandToQueue(AbstractRequestCommand command) {
        commandQueue.add(command);
    }

    @Override
    public void act(float delta) {
        try {
            if (!connected) {
                connect(host, port);
            } else {
                communicate();
            }
        } catch (IOException | NoEngineException e) {
            e.printStackTrace();
        }
    }

    private void connect(String host, int port) throws IOException {
        channel = SocketChannel.open(new InetSocketAddress(host, port));

        connected = true;
    }

    private void communicate() throws IOException, NoEngineException {
        if (commandQueue.size() > 0) {
            sendQueuedCommmands();
            commandQueue.clear();

            List<AbstractResponseCommand> commands = listen();
            for (AbstractResponseCommand command : commands) {
                receiveCommand(command, null);
            }
        }
    }

    private List<AbstractResponseCommand> listen() throws IOException {
        List<String> objects = NetworkConnection.listen(channel);

        return objects.stream()
                .map(o -> {
                    AbstractResponseCommand command = (AbstractResponseCommand) serializer.deserialize(o);
                    if (command != null) {
                        command.setNotifier(ClientNotifier.getInstance());
                    }
                    return command;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void receiveCommand(AbstractResponseCommand command, Void mark) throws IOException, NoEngineException {
        if (command != null) {
            command.execute();
        }
    }

    @Override
    public void quit(Void marker) throws IOException {
        addCommandToQueue(new QuitCommand());
        sendQueuedCommmands();
    }

    @Override
    public void sendCommand(AbstractRequestCommand... commands) {
        commandQueue.addAll(Arrays.asList(commands));
    }

    private void sendQueuedCommmands() throws IOException {
        StringBuilder msg = new StringBuilder();
        AbstractRequestCommand[] commands = new AbstractRequestCommand[commandQueue.size()];
        commands = commandQueue.toArray(commands);
        for (AbstractRequestCommand command: commands) {
            msg.append(serializer.serialize(command));
        }
        channel.write(ByteBuffer.wrap(msg.toString().getBytes()));
    }
}
