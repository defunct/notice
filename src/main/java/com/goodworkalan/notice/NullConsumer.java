package com.goodworkalan.notice;

import java.util.Map;


class NullConsumer implements Consumer {
    public void consume(Map<String, Object> message) {
    }

    public void shutdown() {
    }
}
