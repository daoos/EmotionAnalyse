package com.ibm.emotion.util;

import javax.servlet.http.HttpServletRequest;

public class ServerUtil {

	/**
	 * 获取tomcat webapps目录
	 * 
	 * @param request
	 * @return
	 */
	public static String getTomcatWebappsPath(HttpServletRequest request) {
		String tomcatRoot = request.getSession().getServletContext().getRealPath("/");
	    String os = System.getProperty("os.name"); 
	    String[] foo = null;
	    if(os.toLowerCase().startsWith("win")){  
	    	foo = tomcatRoot.split("\\\\");
	    }else {
	    	foo = tomcatRoot.split("/");
		} 
		StringBuilder tomcatWebAppsBuilder = new StringBuilder();
		int i = 0;
		for (String paths : foo) {
			++i;
			if (i != foo.length) {
				tomcatWebAppsBuilder.append(paths);
				tomcatWebAppsBuilder.append("/");
			}
		}
		String tomcatWebApps = tomcatWebAppsBuilder.toString();
		return tomcatWebApps;
	}
}
