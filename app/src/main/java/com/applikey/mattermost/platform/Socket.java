package com.applikey.mattermost.platform;


import com.applikey.mattermost.models.socket.WebSocketEvent;

import rx.Observable;

public interface Socket {

    Observable<WebSocketEvent> listen();

    boolean isOpen();

    void close();

}
