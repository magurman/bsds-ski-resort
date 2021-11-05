package com.bsds.server.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public class ServletUtils {

    static void formatHttpResponse(HttpServletResponse res, String responseBody, int statusCode, String contentType, HashMap<String, String> headers) throws IOException {

        res.setStatus(statusCode);

        if (contentType != null) res.setContentType(contentType);
        if (responseBody != null) res.getWriter().append(responseBody);
        if (headers != null) {
            for (Map.Entry<String,String> entry : headers.entrySet()) {
                res.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }   
}
