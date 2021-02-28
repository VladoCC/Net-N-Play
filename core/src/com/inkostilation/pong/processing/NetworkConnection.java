package com.inkostilation.pong.processing;

import com.inkostilation.pong.exceptions.EmptyParcelException;
import com.inkostilation.pong.exceptions.ParsingNotFinishedException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class NetworkConnection {

    private static final int bufferSize = 1024;

    private static final MessageParser parser = new MessageParser();
    private static final ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

    public static List<String> listen(SocketChannel channel) throws IOException {
        List<String> objects = new ArrayList<>();

        /** message parsing */
        parser.newMessage();
        while (true) {
            try {
                channel.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
                channel.close();
            }
            try {
                parser.addParcel(buffer);
            } catch (EmptyParcelException e) {
                e.printStackTrace();
                break;
            }
            buffer.clear();

            if (parser.isEmpty() || parser.hasObjects()) {
                try {
                    objects = parser.getAndClearObjects();
                } catch (ParsingNotFinishedException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

        return objects;
    }
}
