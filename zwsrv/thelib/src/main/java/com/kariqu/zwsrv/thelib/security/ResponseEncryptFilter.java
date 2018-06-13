package com.kariqu.zwsrv.thelib.security;

import com.kariqu.zwsrv.thelib.util.AESCoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 25/01/2018.
 */
public class ResponseEncryptFilter implements Filter {

    protected final transient Logger logger = LoggerFactory.getLogger("EncryptFilter");

    boolean isProdEnv = false;
    List<String> ignoreFilterList = new ArrayList<>();

    public ResponseEncryptFilter() {

    }

    public ResponseEncryptFilter(List<String> ignoreFilterList, boolean isProdEnv) {
        this.isProdEnv=isProdEnv;
        if (ignoreFilterList!=null) {
            this.ignoreFilterList.addAll(ignoreFilterList);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest)) {
            throw new RuntimeException("Expecting an HTTP request");
        }

        HttpServletRequest httpServletRequest = (HttpServletRequest)request;

        boolean filtered = false;
        String requestURI = httpServletRequest.getRequestURI();
        for (String prefix : ignoreFilterList) {
            if (requestURI.startsWith(prefix)) {
                filtered=true;
            }
        }

        if (!filtered) {
            if (isProdEnv) {
                String secretKey = SecretKeyUtil.apiSecretKey(httpServletRequest);
                if (secretKey!=null&&secretKey.length()>0) {

                    HttpServletResponse httpServletResponse =(HttpServletResponse)response;
                    BufferedServletResponseWrapper bufferedResponse = new BufferedServletResponseWrapper(
                            httpServletResponse);

                    chain.doFilter(request, bufferedResponse);

                    String responseData = bufferedResponse.getResponseData("utf-8");
                    String encryptedResponseData = null;
                    try {

                        //logger.info("ResponseEncryptFilter requestURI:{} ",requestURI!=null?requestURI:"");

                        //logger.info("ResponseEncryptFilter responseData:{} ",responseData!=null?responseData:"");

                        encryptedResponseData = AESCoder.encrypt(secretKey.getBytes(), responseData);

                        //logger.info("ResponseEncryptFilter encryptedResponseData:{} ",encryptedResponseData!=null?encryptedResponseData:"");

                        // 重置响应输出的内容长度
                        response.setContentLength(-1);
                        // 输出最终的结果
                        PrintWriter out = response.getWriter();
                        out.write(encryptedResponseData);
                        out.flush();
                        out.close();

//                        OutputStream outputStream=httpServletResponse.getOutputStream();
//                        outputStream.write(encryptedResponseData != null ? encryptedResponseData.getBytes() : "".getBytes());
//                        outputStream.flush();
//                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
