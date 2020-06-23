package com.truextend.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class ExceptionHandler implements ExceptionMapper<Exception> {
    public static final String RESTEASY003870 = "RESTEASY003870";
    public static final String RESTEASY003210 = "RESTEASY003210";
    public static final String ILLEGAL_UNQUOTED_CHARACTER = "Illegal unquoted character";
    public static final String UNESPECTED_CHARACTER = "Unexpected character";

    @Override
    public Response toResponse(Exception e) {

        if (e instanceof BusinessException) {
            Map<String, Object> result = new HashMap<>();
            result.put("error", e.getMessage());
            return Response.status(((BusinessException) e).getStatus()).entity(result).type(MediaType.APPLICATION_JSON).build();

        } else {
            Map<String, Object> map = new HashMap<>();
            if (e.getMessage() != null) {
                if (e.getMessage().contains(RESTEASY003870)) {
                    map.put("error", "Unable to extract parameter from http request");
                } else if (e.getMessage().contains(RESTEASY003210)) {
                    map.put("error", "Could not find resource for full path");
                } else if (e.getMessage().contains(ILLEGAL_UNQUOTED_CHARACTER)) {
                    map.put("error", "Illegal unquoted character: has to be escaped using backslash to be included in string value");
                } else if (e.getMessage().contains(UNESPECTED_CHARACTER)) {
                    map.put("error", "Unexpected character: was expecting comma to separate OBJECT entries");
                } else {
                    map.put("error", e.getMessage());
                }
            }
            e.printStackTrace();
            return Response.serverError().entity(map).type(MediaType.APPLICATION_JSON).build();
        }
    }
}
