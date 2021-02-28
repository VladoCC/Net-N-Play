package com.inkostilation.pong.server.network;

import com.inkostilation.pong.commands.AbstractResponseCommand;
import com.inkostilation.pong.commands.AbstractRequestCommand;
import com.inkostilation.pong.engine.IEngine;
import com.inkostilation.pong.processing.NetworkConnection;
import com.inkostilation.pong.processing.Serializer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.stream.Collectors;

public class NetworkProcessor implements IProcessor {

    private static final String host = "localhost";
    private static final int port = 8080;

    private static NetworkProcessor instance = null;

    private Selector selector;
    private ServerSocketChannel serverSocket;
    private Serializer serializer = new Serializer();
    private ICommandRouter<SocketChannel> router = new ServerCommandRouter();
    private Map<SocketChannel, List<AbstractResponseCommand>> commandQueue = new HashMap<>();


    private boolean started = true;

    private NetworkProcessor() {
        try {
            selector = Selector.open();
            serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(host, port));
            serverSocket.configureBlocking(false);
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static NetworkProcessor getInstance() {
        if (instance == null) {
            instance = new NetworkProcessor();
        }
        return instance;
    }

    @Override
    public void start() {
        started = true;
    }

    @Override
    public void stop() {
        started = false;
    }

    @Override
    public void processConnections() {
        try {
            if (started) {
                selector.select(10);
                final Set<SelectionKey> selectedKeys = selector.selectedKeys();
                final Iterator<SelectionKey> iter = selectedKeys.iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    if (key.isAcceptable()) {
                        register();
                    }
                    if (key.isReadable()) {
                        receiveMessage(key);
                    }
                    iter.remove();
                }

                if (commandQueue.size() > 0) {
                    sendAll();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void register() throws IOException {
        SocketChannel client  = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }

    private void receiveMessage(SelectionKey key) throws IOException {
        List<String> objects = parseObjects((SocketChannel) key.channel());

        Set<Class> set = new HashSet<>();
        List<AbstractRequestCommand<IEngine<SocketChannel>, SocketChannel>> commands = objects.stream()
                .map(o -> {
                    AbstractRequestCommand<IEngine<SocketChannel>, SocketChannel> command = (AbstractRequestCommand<IEngine<SocketChannel>, SocketChannel>) serializer.deserialize(o);
                    if (command == null) {
                        return null;
                    }
                    command.setMarker((SocketChannel) key.channel());
                    return command;
                })
                .filter(c -> set.add(c.getClass()))
                .collect(Collectors.toList());

        for (AbstractRequestCommand<IEngine<SocketChannel>, SocketChannel> command: commands) {
            try {
                if (command != null) {
                    router.route(command);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private List<String> parseObjects(SocketChannel channel) throws IOException {
        return NetworkConnection.listen(channel);
    }

    @Override
    public void sendMessage(AbstractResponseCommand command, SocketChannel channel) {
        if (commandQueue.containsKey(channel)) {
            commandQueue.get(channel).add(command);
        } else {
            List<AbstractResponseCommand> commands = new ArrayList<>();
            commands.add(command);
            commandQueue.put(channel, commands);
        }
    }

    public boolean hasQueuedMessages(SocketChannel channel) {
        return commandQueue.containsKey(channel) && commandQueue.get(channel).size() > 0;
    }

    private void sendAll() {
        for (Map.Entry<SocketChannel, List<AbstractResponseCommand>> entry: commandQueue.entrySet()) {
            if (entry.getKey().isConnected()) {
                StringBuilder message = new StringBuilder();
                for (AbstractResponseCommand command : entry.getValue()) {
                    message.append(serializer.serialize(command));
                }
                try {
                    entry.getKey().write(ByteBuffer.wrap(message.toString().getBytes()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        commandQueue.clear();
    }

    public boolean isStarted() {
        return started;
    }
}
