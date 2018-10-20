package com.test;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class ResponseHeaderFilter implements Filter{
	 
	 private String header;
	 private String value;
	  
	 public void init(FilterConfig fc) throws ServletException {
	  header = fc.getInitParameter("header");
	  value = fc.getInitParameter("value");
	 }
	  
	 public void doFilter(ServletRequest req, ServletResponse res,
	   FilterChain fc) throws IOException, ServletException {
	  HttpServletResponse response = (HttpServletResponse) res;
	  response.setHeader(header, value);
	  fc.doFilter(req, res);
	 }
	  
	 public void destroy() {
	   
	 }
	}