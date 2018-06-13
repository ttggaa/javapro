package com.kariqu.zwsrv.thelib.http;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;

/**
 * Created by simon on 5/5/16.
 */
public class HttpSessionContext {
    private HttpSession httpSession;

    public HttpSessionContext(HttpSession httpSession)
    {
        this.httpSession = httpSession;
    }

    public void setAttribute(String name, Object value)
    {
        httpSession.setAttribute(name, value);
    }

    public void removeAttribute(String name)
    {
        httpSession.removeAttribute(name);
    }

    public Object getAttribute(String name)
    {
        return httpSession.getAttribute(name);
    }

    public String getId()
    {
        return httpSession.getId();
    }

    public Enumeration getAttributeNames()
    {
        return httpSession.getAttributeNames();
    }

    public void invalidate()
    {
        httpSession.invalidate();
    }
}
