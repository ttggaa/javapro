package com.kariqu.zwsrv.app.controller;

import com.kariqu.zwsrv.thelib.Constants;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Created by simon on 24/11/17.
 */
@RestController
@RequestMapping("file/v1")
public class FileController extends BaseController {

    //subDir=>type referto relaytypes
    @RequestMapping(value = "dl/{appId}/{subDir}/**", method = RequestMethod.GET)
    public void download(
            @PathVariable("appId") String appId,
            @PathVariable("subDir") String subDir,
            @PathVariable("fileName") String fileName,
            HttpServletRequest request,
            HttpServletResponse response) {
        String temp = appId+"/"+subDir+"/";
        String requestURI = request.getRequestURI();
        int index = requestURI.indexOf(temp);
        if (index>=0) {
            String relativePath = requestURI.substring(index + temp.length());
            String filePath = Constants.DATA_DIR_NAME;//+"/"+appId;//+"/"+Defs.DATA_SUB_DIR_NAME_AVATAR+"/"+relativePath;
            if (appId!=null&&appId.length()>0) {
                filePath+="/";
                filePath+=appId;
            }
            if (subDir!=null&&subDir.length()>0) {
                filePath+="/";
                filePath+=subDir;
            }
            if (relativePath!=null&&relativePath.length()>0) {
                filePath+="/";
                filePath+=relativePath;
            }
            Path path = Paths.get(filePath);
            if (Files.exists(path,new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
                try {
                    File file = new File(filePath);
                    if (file.isFile()) {
                        response.setContentType(Files.probeContentType(path));

                        // get your file as InputStream
                        InputStream is = new BufferedInputStream(new FileInputStream(file));
                        // copy it to response's OutputStream
                        org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
                        response.flushBuffer();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequestMapping(value="{appId}/{subDir}/**", method = RequestMethod.GET)
    public HttpEntity test(@PathVariable("appId") String appId,
                           @PathVariable("subDir") String subDir,
                           @RequestParam Map<String,String> allRequestParams,
                           HttpServletRequest request,
                           HttpServletResponse response) {

        String temp = appId+"/"+subDir+"/";
        String requestURI = request.getRequestURI();
        int index = requestURI.indexOf(temp);
        if (index>=0) {
            String relativePath = requestURI.substring(index + temp.length());
            String filePath = Constants.DATA_DIR_NAME;//+"/"+appId;//+"/"+Defs.DATA_SUB_DIR_NAME_AVATAR+"/"+relativePath;
            if (appId!=null&&appId.length()>0) {
                filePath+="/";
                filePath+=appId;
            }
            if (subDir!=null&&subDir.length()>0) {
                filePath+="/";
                filePath+=subDir;
            }
            if (relativePath!=null&&relativePath.length()>0) {
                filePath+="/";
                filePath+=relativePath;
            }
            Path path = Paths.get(filePath);
            if (Files.exists(path,new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
                try {
                    File file = new File(filePath);
                    if (file.isFile()) {
                        response.setContentType(Files.probeContentType(path));
                        return responseData(file);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    protected HttpEntity responseData(File file) {
        try {
            InputStream is = new BufferedInputStream(new FileInputStream(file));
            InputStreamResource inputStream = new InputStreamResource(is);
            return ResponseEntity.ok(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
