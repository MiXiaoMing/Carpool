package com.spirited.support.event;

import java.io.Serializable;

public class PushEvent implements Serializable {

    private Event event;

    public PushEvent(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }


    public enum Event {
        CID_READY,
    }
}
