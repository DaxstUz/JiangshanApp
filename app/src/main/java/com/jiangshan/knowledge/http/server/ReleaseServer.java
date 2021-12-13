package com.jiangshan.knowledge.http.server;

import com.hjq.http.config.IRequestServer;
import com.hjq.http.model.BodyType;

public class ReleaseServer implements IRequestServer {

    @Override
    public String getHost() {
        return "https://api.51kpm.com/app";
//        return "http://172.16.31.235:8181/app/";
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