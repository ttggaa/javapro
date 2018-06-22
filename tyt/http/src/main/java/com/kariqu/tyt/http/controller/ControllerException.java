package com.kariqu.tyt.http.controller;


import com.kariqu.tyt.http.pkg.ResponseData;
import com.kariqu.tyt.http.pkg.ResponseError;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class ControllerException {

    @InitBinder
    public void initBinder(WebDataBinder binder) {}

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("author", "Magical Sam");
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseData errorHandler(Exception ex) {
        return ResponseError.UnknownError;
        /*
        Map map = new HashMap();
        map.put("code", 100);
        map.put("msg", ex.getMessage());
        return map;
        */
    }
}
