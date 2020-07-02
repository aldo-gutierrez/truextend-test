package com.truextend.automation.http;

import java.nio.charset.Charset;
import java.util.*;

public class HTTPResponse {
    int code;
    byte[] body;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public String getBodyAsString() {
        if (body == null) {
            return null;
        } else {
            return new String(body, Charset.forName("UTF8"));
        }
    }

    public Map<String, Object> getJsonBodyAsMap()  {
        return JSONUtils.getMap(getBodyAsString());
    }

    public List getJsonBodyAsList()  {
        return JSONUtils.getList(getBodyAsString());
    }

}