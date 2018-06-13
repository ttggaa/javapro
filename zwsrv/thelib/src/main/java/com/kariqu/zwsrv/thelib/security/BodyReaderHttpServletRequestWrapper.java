
package com.kariqu.zwsrv.thelib.security;

import com.kariqu.zwsrv.thelib.util.AESCoder;
import com.kariqu.zwsrv.thelib.util.JsonUtil;
import jodd.io.StreamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.net.URLDecoder;
import java.util.*;

public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {

    protected final transient Logger logger = LoggerFactory.getLogger("BodyReader");

    private Map<String,String[]> paramsMap = new HashMap<>();
    private PostRequestBodyParam requestBodyParam = null;
    private final byte[] body;
    private String secretKey;

    public BodyReaderHttpServletRequestWrapper(HttpServletRequest request, String secretKey) throws IOException {
        super(request);
        body = StreamUtil.readBytes(request.getInputStream());

        this.secretKey=secretKey;
        //body = StreamUtil.readBytes(request.getReader(), "UTF-8");


        Map<String, String[]> paramsMapTemp = super.getParameterMap();
        if (paramsMapTemp != null) {
            paramsMap.putAll(paramsMapTemp);
        }

        if ("POST".equals(request.getMethod().toUpperCase())) {
            String body = "";
            try {
                body = getRequestBody(this.getInputStream());
                //logger.info("BodyReaderHttpServletRequestWrapper: bodyTemp {} ",body!=null?body:"");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (body!=null&&body.length()>0) {
                boolean isJson = false;
                if (this.secretKey!=null&&this.secretKey.length()>0) {
                    try {
                        body = AESCoder.decrypt(this.secretKey.getBytes(),body);
                        //logger.info("BodyReaderHttpServletRequestWrapper:body.decrypt {}  ",body!=null?body:"");
                        body = URLDecoder.decode(body, "UTF-8");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    body = URLDecoder.decode(body, "UTF-8");
                    //logger.info("BodyReaderHttpServletRequestWrapper:body {} ",body!=null?body:"");
                }
                if (body.startsWith("{")&&body.endsWith("}")) {
                    requestBodyParam = JsonUtil.convertJson2Model(body,PostRequestBodyParam.class);
                    isJson=true;
                } else {
                    int beginIndex=body.indexOf("{");
                    if (beginIndex>=0) {
                        int endIndex=body.indexOf("}");
                        if (endIndex>beginIndex&&endIndex<body.length()) {
                            String beginString =body.substring(0, beginIndex);
                            String endString =body.substring(endIndex, body.length()-1);
                            if (beginString!=null&&endString!=null) {
                                beginString=beginString.replace(" ","");
                                endString=endString.replace(" ","");
                                if (beginString.equalsIgnoreCase("{")
                                        &&endString.equalsIgnoreCase("}")) {
                                    requestBodyParam = JsonUtil.convertJson2Model(body,PostRequestBodyParam.class);
                                    isJson=true;
                                }
                            }
                        }
                    }
                }
                if (!isJson) {
                    Map<String,String> temp = new HashMap<>();
                    StringTokenizer tokenizer = new StringTokenizer(body,"&");
                    while (tokenizer.hasMoreTokens()) {
                        String nextToken = tokenizer.nextToken();
                        StringTokenizer tokenizerTemp = new StringTokenizer(nextToken,"=");
                        String name = "";
                        String value = "";
                        if (tokenizerTemp.hasMoreTokens()) {
                            name=tokenizerTemp.nextToken();
                        }
                        if (tokenizerTemp.hasMoreTokens()) {
                            value=tokenizerTemp.nextToken();
                        }
                        if (name.length()>0) {
                            temp.put(name,value);
                        }
                    }
                    for (Map.Entry<String, String> entry : temp.entrySet()) {
                        String[] valueArray = new String[1];
                        valueArray[0] = String.valueOf(entry.getValue());
                        paramsMap.put(entry.getKey(),valueArray);
                    }
                }
            }
        }
        if (requestBodyParam!=null
                && requestBodyParam.getParams()!=null
                && requestBodyParam.getParams().size()>0) {
            for (Map.Entry<String, Object> entry : requestBodyParam.getParams().entrySet()) {
                String[] valueArray = new String[1];
                valueArray[0] = String.valueOf(entry.getValue());
                paramsMap.put(entry.getKey(),valueArray);
            }
        }
    }

    public boolean forward(ServletResponse response) throws ServletException, IOException {
        if (this.getRequest() instanceof BodyReaderHttpServletRequestWrapper) {
            ///...
        }
        else if (this.getRequest() instanceof ServletRequestWrapper && ((ServletRequestWrapper)this.getRequest()).getRequest()  instanceof BodyReaderHttpServletRequestWrapper) {
            ///...
        }
        else {
            if (requestBodyParam!=null
                    && requestBodyParam.getModule()!=null&&requestBodyParam.getModule().length()>0
                    && requestBodyParam.getPath()!=null&&requestBodyParam.getPath().length()>0) {
                String requestURI = super.getRequestURI();
                requestURI+=requestBodyParam.getModule();
                requestURI+="/";
                requestURI+=requestBodyParam.getPath();
                getRequestDispatcher(requestURI).forward(this, response);
                return true;
            }
        }
        return false;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener arg0) {

            }
        };
    }

    @Override
    public Map getParameterMap() {
        return paramsMap;
    }

    @Override
    public String getParameter(String name) {// 重写getParameter，代表参数从当前类中的map获取
        String[] values = paramsMap.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }

    @Override
    public String[] getParameterValues(String name) {// 同上
        return paramsMap.get(name);
    }

    @Override
    public Enumeration getParameterNames() {
        return Collections.enumeration(paramsMap.keySet());
    }

    private String getRequestBody(InputStream stream) {
        try {
//            BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) this.getInputStream(),"UTF-8"));
            BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) this.getInputStream()));
            StringBuffer sb = new StringBuffer("");
            String temp;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";

//        String line = "";
//        StringBuilder body = new StringBuilder();
//        int counter = 0;
//        // 读取POST提交的数据内容
//        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
//        try {
//            while ((line = reader.readLine()) != null) {
//                if (counter > 0) {
//                    body.append("rn");
//                }
//                body.append(line);
//                counter++;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return body.toString();
    }

}

//http://blog.csdn.net/Heng_Ji/article/details/54893352
