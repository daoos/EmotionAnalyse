package com.ibm.emotion.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.emotion.util.FileUtil;
import com.ibm.emotion.util.ServerUtil;
import com.ibm.emotion.util.StringUtils;

/**
 * 返回数据
 */
public class EmotionSourceServlet extends HttpServlet{

	private static final long serialVersionUID = 6158488556944770491L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
		String folderName = req.getParameter("folderName");
		String wavName = req.getParameter("wavName");
		String callback = req.getParameter("callback"); //必须有，否则不执行回调
		String path = ServerUtil.getTomcatWebappsPath(req);
		StringBuilder sb = new StringBuilder();
		if (!StringUtils.isEmpty(wavName)) {
			sb.append(callback + "(");
			String filePath = path + "/emotionFiles/" + folderName + "/" + wavName + ".csv";
			if (StringUtils.isEquals("valence", folderName)) {
				sb.append(FileUtil.readCsv(filePath, 0.1, true));
			}else {
				sb.append(FileUtil.readCsv(filePath, 0.0, false));
			}
			sb.append(");");
		}
		// 获取输出流
		resp.getWriter().write(sb.toString());
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
}
