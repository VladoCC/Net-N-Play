package com.inkostilation.pong.processing;

import com.inkostilation.pong.exceptions.EmptyParcelException;
import com.inkostilation.pong.exceptions.ParsingNotFinishedException;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MessageParser {

    private ArrayList<Position> objects = new ArrayList<>();
    private int counter = -1;
    private StringBuilder builder = new StringBuilder();

    public void newMessage() {
        objects.clear();
        builder = new StringBuilder();
        counter = -1;
    }

    public boolean hasObjects() {
        return counter == 0;
    }

    public boolean isEmpty() {
        return  counter == - 1;
    }

    public List<String> getAndClearObjects() throws ParsingNotFinishedException {
        if (!hasObjects() && !isEmpty()) {
            throw new ParsingNotFinishedException();
        }

        String parcel = builder.toString();
        List<String> messages = objects.stream()
                .map(p -> new String(parcel.substring(p.start, p.end)))
                .collect(Collectors.toList());

        return messages;
    }

    public void clearObjects() {
        objects.clear();
    }

    public void addParcel(ByteBuffer parcel) throws EmptyParcelException {
        int pos = parcel.position();
        String newParcel = new String(parcel.array()).substring(0, pos);
        if (newParcel.length() == 0) {
            throw new EmptyParcelException();
        }
        builder.append(newParcel);
        processParcel();
    }

    private void processParcel() {
        int counter = 0;
        int parcelStart = -1;
        int parcelEnd;
        String parcel = builder.toString();
        for (int i = 0; i < parcel.length(); i++) {
            char elem = parcel.charAt(i);
            if (elem == '{') {
                if (counter == 0) {
                    parcelStart = i;
                }
                counter++;
            }
            if (elem == '}' && counter > 0)  { //check counter to see if we are in json or not
                counter--;
                if (counter == 0) {
                    parcelEnd = i;
                    objects.add(new Position(parcelStart, parcelEnd + 1));
                }
            }
        }

        this.counter = counter;
    }

    private final class Position {
        private int start;
        private int end;

        public Position(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }
}
