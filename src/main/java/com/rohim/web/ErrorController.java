package com.rohim.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

	@Autowired
	private ErrorAttributes errorAttributes;

	@RequestMapping(path = "/error")
	@ResponseBody
	public Map<String, Object> errorPage(WebRequest webRequest) {
		ErrorAttributeOptions options = ErrorAttributeOptions.of(
			ErrorAttributeOptions.Include.EXCEPTION,
			ErrorAttributeOptions.Include.MESSAGE,
			ErrorAttributeOptions.Include.STACK_TRACE,
			ErrorAttributeOptions.Include.BINDING_ERRORS
		);

		Map<String, Object> errAttributes = errorAttributes.getErrorAttributes(webRequest, options);
		return errAttributes;
	}
}
