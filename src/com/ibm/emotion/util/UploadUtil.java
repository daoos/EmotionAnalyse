package com.ibm.emotion.util;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import net.sf.json.JSONObject;

public class UploadUtil {

	// 上传文件存储目录
	private static final String UPLOAD_FILE = "/home/ibm/project/EM_demo/input.jpg";

	// 上传配置
	private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3; // 3MB
	private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
	private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB

	/**
	 * 保存成功：{"status":true, "message":"save success", "filePath":"path", "fileName":"name", "absolutePath":"path"}
	 * 保存失败：{"status":false, "message":"reason"}
	 * @return
	 */
	public static JSONObject saveUploadFile(HttpServletRequest request) {
		JSONObject object = new JSONObject();
		// 检测是否为多媒体上传
		if (!ServletFileUpload.isMultipartContent(request)) {
			System.out.println("not multipart" + request.getContentType());
			object.put("status", false);
			object.put("message", "not multipart");
		} else {
			// 配置上传参数
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 设置内存临界值 - 超过后将产生临时文件并存储于临时目录中
			factory.setSizeThreshold(MEMORY_THRESHOLD);
			// 设置临时存储目录
			factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 设置最大文件上传值
			upload.setFileSizeMax(MAX_FILE_SIZE);

			// 设置最大请求值 (包含文件和表单数据)
			upload.setSizeMax(MAX_REQUEST_SIZE);

			try {
				// 解析请求的内容提取文件数据
				List<FileItem> formItems = upload.parseRequest(request);
				if (formItems != null && formItems.size() > 0) {
					// 迭代表单数据
					for (FileItem item : formItems) {
						// 处理不在表单中的字段
						if (!item.isFormField()) {
							File storeFile = new File(UPLOAD_FILE);
							// 保存文件到硬盘
							item.write(storeFile);
							object.put("status", true);
							object.put("message", "save success");
							object.put("absolutePath", storeFile.getAbsolutePath());
						}
					}
				}
			} catch (Exception ex) {
				object.put("status", false);
				object.put("message", "server exception");
			}
		}
		return object;
	}
	
}
