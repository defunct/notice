package com.goodworkalan.prattle;

import com.goodworkalan.notice.Noticeable;

public interface Entry extends Noticeable<Entry> {
    public Entry start(String name);
    
    public Entry stop(String name);

    public void send();
}
