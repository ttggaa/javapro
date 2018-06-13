package com.kariqu.zwsrv.thelib.security;

/**
 * Created by simon on 23/07/17.
 */
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;


public class BufferedServletResponseWrapper extends HttpServletResponseWrapper {

    private ByteArrayOutputStream outputStream;
    private PrintWriter writer;

    public BufferedServletResponseWrapper(HttpServletResponse response)
            throws IOException {
        super(response);
        outputStream = new ByteArrayOutputStream();
        writer = new PrintWriter(outputStream);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {

        return new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
            }

            @Override
            public void write(byte[] b) throws IOException {
                outputStream.write(b);
            }
            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener arg0) {

            }
        };
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return writer;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (writer != null) {
            writer.flush();
        } else if (outputStream != null) {
            outputStream.flush();
        }
    }

    public String getResponseData(String charsetName) throws UnsupportedEncodingException {

        String str = "";
        try {
            //刷新该流的缓冲，详看java.io.Writer.flush()
            writer.flush();
            str = outputStream.toString(charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return str;
    }
}





//package com.kariqu.zwsrv.thelib.security;
//
///**
// * Created by simon on 23/07/17.
// */
//        import javax.servlet.http.HttpServletResponse;
//        import javax.servlet.http.HttpServletResponseWrapper;
//        import java.io.ByteArrayOutputStream;
//        import java.io.IOException;
//        import java.io.PrintWriter;
//        import java.io.UnsupportedEncodingException;
//
//
//public class BufferedServletResponseWrapper extends HttpServletResponseWrapper {
//
//    private ByteArrayOutputStream outputStream;
//    private MyPrintWriter writer;
//
//
//    public BufferedServletResponseWrapper(HttpServletResponse response)
//            throws IOException {
//        super(response);
//        outputStream = new ByteArrayOutputStream();
//        writer = new MyPrintWriter(outputStream);
//    }
//
//    public void finalize() throws Throwable {
//        super.finalize();
//        outputStream.close();
//        writer.close();
//    }
//
//    public String getContent() {
//        try {
//            writer.flush();   //刷新该流的缓冲，详看java.io.Writer.flush()
//            String s = writer.getByteArrayOutputStream().toString("UTF-8");
//            //此处可根据需要进行对输出流以及Writer的重置操作
//            //比如tmpWriter.getByteArrayOutputStream().reset()
//            return s;
//        } catch (UnsupportedEncodingException e) {
//            return "UnsupportedEncoding";
//        }
//    }
//
////    //覆盖getWriter()方法，使用我们自己定义的Writer
////    public PrintWriter getWriter() throws IOException {
////        return writer;
////    }
//
////    public void close() throws IOException {
////        writer.close();
////    }
//
////    @Override
////    public ServletOutputStream getOutputStream() throws IOException {
////
////        return new ServletOutputStream() {
////            @Override
////            public void write(int b) throws IOException {
////                outputStream.write(b);
////            }
////
////            @Override
////            public void write(byte[] b) throws IOException {
////                outputStream.write(b);
////            }
////            @Override
////            public boolean isReady() {
////                return true;
////            }
////
////            @Override
////            public void setWriteListener(WriteListener arg0) {
////
////            }
////        };
////    }
//
//    @Override
//    public PrintWriter getWriter() throws IOException {
//        return writer;
//    }
//
//    @Override
//    public void flushBuffer() throws IOException {
//        if (writer != null) {
//            writer.flush();
//        } else if (outputStream != null) {
//            outputStream.flush();
//        }
//    }
//
////    public String getResponseData() {
////        return outputStream.toString();
////    }
//
//
//    //自定义PrintWriter，为的是把response流写到自己指定的输入流当中
//    //而非默认的ServletOutputStream
//    private static class MyPrintWriter extends PrintWriter {
//        ByteArrayOutputStream myOutput;   //此即为存放response输入流的对象
//
//        public MyPrintWriter(ByteArrayOutputStream output) {
//            super(output);
//            myOutput = output;
//        }
//        public ByteArrayOutputStream getByteArrayOutputStream() {
//            return myOutput;
//        }
//    }
//}

