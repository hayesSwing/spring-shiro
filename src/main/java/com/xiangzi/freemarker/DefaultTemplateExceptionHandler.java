package com.xiangzi.freemarker;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class DefaultTemplateExceptionHandler implements TemplateExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(DefaultTemplateExceptionHandler.class);

	public void handleTemplateException(TemplateException te, Environment env, java.io.Writer out) throws TemplateException {
		try {
			logger.error("TemplateException:", te);
			out.write("[ERROR: " + te.getMessage() + "]");
		} catch (IOException e) {
			logger.error("TemplateException:", e);
			throw new TemplateException("Failed to print error message. Cause: " + e, env);
		}
	}
	
}
