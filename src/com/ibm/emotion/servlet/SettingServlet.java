package com.ibm.emotion.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.emotion.constant.Constants;
import com.ibm.emotion.util.StringUtils;

public class SettingServlet extends HttpServlet{

	private static final long serialVersionUID = 2658618437567320520L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=utf-8");
		String debug = req.getParameter("debug");
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isEquals("true", debug)) {
			Constants.DEBUG = true;
			sb.append("debug=true");
		}else if (StringUtils.isEquals("false", debug)) {
			Constants.DEBUG = false;
			sb.append("debug=false");
		}
		resp.getWriter().write(sb.toString());
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
