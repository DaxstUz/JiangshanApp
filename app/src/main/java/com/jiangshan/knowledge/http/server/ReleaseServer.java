package com.jiangshan.knowledge.http.server;

import com.hjq.http.config.IRequestServer;
import com.hjq.http.model.BodyType;

public class ReleaseServer implements IRequestServer {

    @Override
    public String getHost() {
        return "https://api.51kpm.com/app";
    }

    @Override
    public String getPath() {
        return "";
    }

    @Override
    public BodyType getType() {
        return BodyType.JSON;
    }
}