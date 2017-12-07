package com.lms.ui.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@ControllerAdvice
public class GlobalExceptionHandler {
	static Logger log = Logger.getLogger(GlobalExceptionHandler.class);
    //https://jira.spring.io/browse/SPR-14651
    //4.3.5 supports RedirectAttributes redirectAttributes
    @ExceptionHandler(MultipartException.class)
    public String handleError1(MultipartException e, RedirectAttributes redirectAttributes) {
    	log.error("Exception = " + e);
        redirectAttributes.addFlashAttribute("message", e.getCause().getMessage());
        log.debug("Redirecting to upload");
        return "redirect:/upload";

    }

    /*@ExceptionHandler(MultipartException.class)
    public String handleError2(MultipartException e) {

        return "redirect:/errorPage";

    }*/

}
