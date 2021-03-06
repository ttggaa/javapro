package com.kariqu.zwsrv.thelib.http;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by simon on 5/5/16.
 */
public class HttpResponseContext {

    private HttpServletResponse response = null;

    public HttpResponseContext(HttpServletResponse response)
    {
        this.response = response;
    }

    public void setContentLength(int len)
    {
        response.setContentLength(len);
    }

    public boolean containsHeader(String name)
    {
        return response.containsHeader(name);
    }

    public void setHeader(String name, String value)
    {
        response.setHeader(name, value);
    }

    public void addCookie(Cookie cookie)
    {
        response.addCookie(cookie);
    }

    public String encodeRedirectURL(String url)
    {
        return response.encodeRedirectURL(url);
    }

    public void sendRedirect(String location) throws IOException {
        response.sendRedirect(location);
    }

    public String getCharacterEncoding()
    {
        return response.getCharacterEncoding();
    }

    public void setContentType(String type)
    {
        response.setContentType(type);
    }

    public ServletOutputStream getOutputStream() throws IOException {
        return response.getOutputStream();
    }

    public PrintWriter getWriter() throws IOException {
        return response.getWriter();
    }

    public String encodeURL(String url)
    {
        return response.encodeURL(url);
    }

    public void sendError(int sc) throws IOException {
        response.sendError(sc);
    }

    public void addHeader(String name, String value)
    {
        response.addHeader(name, value);
    }
}
