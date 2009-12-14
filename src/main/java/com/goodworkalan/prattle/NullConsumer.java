package com.goodworkalan.prattle;

import java.util.Map;

class NullConsumer implements Consumer {
    public void consume(Map<String, Object> message) {
    }

    public void shutdown() {
    }
}
