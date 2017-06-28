package com.ibm.emotion.servlet;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.batik.transcoder.TranscoderException;

import com.ibm.emotion.util.StringUtils;
import com.ibm.emotion.util.SvgPngUtil;

import net.sf.json.JSONObject;

public class UploadSVGServlet extends HttpServlet{

	private static final long serialVersionUID = -8668362360523040722L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=utf-8");
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = req.getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) {
		}
		JSONObject params = JSONObject.fromObject(jb.toString());
		String wavName = params.getString("wavName");
		String svg = params.getString("svg");
		if (!StringUtils.isEmpty(wavName) && !StringUtils.isEmpty(svg)) {
			String path = req.getServletContext().getRealPath("") + "chartimgs/" + wavName + ".png";
			try {
				SvgPngUtil.convertToPng(svg, path);
			} catch (TranscoderException e) {
				e.printStackTrace();
			}
		}
		JSONObject object = new JSONObject();
		object.put("msg", "ok");
		resp.getWriter().write(object.toString());
	}
}
