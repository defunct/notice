package com.goodworkalan.cassandra;

import java.io.PrintWriter;

import com.goodworkalan.notice.Sink;

public class Exceptions {
    public static void send(Sink sink, Throwable throwable) { 
        send(sink, throwable, new Clue());
    }
    
    public static void send(Sink sink, Throwable throwable, Clue clue) {
        Clue copy = new Clue(clue, throwable);
        throwable.printStackTrace(new PrintWriter(copy.getStackTrace()));
        copy.send(sink);
    }
}
