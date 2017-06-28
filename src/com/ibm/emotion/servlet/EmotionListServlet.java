package com.ibm.emotion.servlet;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.csvreader.CsvReader;
import com.ibm.emotion.entity.FileEntity;
import com.ibm.emotion.util.FileUtil;
import com.ibm.emotion.util.ServerUtil;
import com.ibm.emotion.util.StringUtils;

import net.sf.json.JSONArray;

public class EmotionListServlet extends HttpServlet{

	private static final long serialVersionUID = 3049714420617908178L;

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String sortBy = req.getParameter("sortBy");//排序依据
		String desc = req.getParameter("desc");//升序降序
		List<FileEntity> entryList = new ArrayList<>();
		String webAppPath = ServerUtil.getTomcatWebappsPath(req);
		String analysisfile = webAppPath + "/emotionFiles/analysis/analysis.json";
		File file = new File(analysisfile);
		if (file.exists()) {
			JSONArray array = JSONArray.fromObject(FileUtil.readFile(file));
			entryList = (List<FileEntity>) JSONArray.toCollection(array, FileEntity.class);
		}else {
			File dir = new File(webAppPath + "/emotionFiles/valence");
			String wavBasePath = webAppPath + "/emotionFiles/callcenter/";
			File[] files = dir.listFiles();
			// 文件计数
			int fileCount = 1;
			for (File itemFile : files) {
				FileEntity entity = new FileEntity();
				CsvReader reader = new CsvReader(itemFile.getAbsolutePath(), ',', Charset.forName("SJIS"));
				Double totalValue = 0.0;
				int count = 1;
				while (reader.readRecord()) { // 逐行读入除表头的数据
					String cell = reader.getValues()[0]; // 取得第row行第0列的数据
					Double value = Double.valueOf(cell);
					totalValue += value;
					count ++;
				}
				Double avgValence = totalValue/count;
				//获取音频文件时长
				String os = System.getProperty("os.name");
				String ffmpegPath = null;
			    if(os.toLowerCase().startsWith("win")){  
			    	ffmpegPath = "D:/jnq/tool/ffmpeg-20170315-6c4665d-win64-static/bin/ffmpeg.exe";
				}else {
					ffmpegPath = "ffmpeg";
				}
			    Object[] audioTime = FileUtil.getAudioTime(wavBasePath + itemFile.getName().replace(".csv", ".wav"), ffmpegPath);
				entity.setId(fileCount);
				entity.setAvgValence(avgValence);
				entity.setFileName(itemFile.getName().replace(".csv", ""));
				if (audioTime != null) {
					entity.setAudioTimeStr(String.valueOf(audioTime[0]));
					entity.setAudioTime(Integer.valueOf(String.valueOf(audioTime[1])));
				}
				entryList.add(entity);
				reader.close();
				fileCount ++;
			}
			FileUtil.writeFile(webAppPath + "/emotionFiles/analysis/analysis.json", JSONArray.fromObject(entryList).toString());
		}
		//排序
		if (StringUtils.isEquals(sortBy, "avgValence") && 
				StringUtils.isEquals(desc, "desc")) {
			Collections.sort(entryList, new Comparator<FileEntity>() {

				@Override
				public int compare(FileEntity o1, FileEntity o2) {
					return new Double(o2.getAvgValence()).compareTo(new Double(o1.getAvgValence()));
				}
			});
		}else if (StringUtils.isEquals(sortBy, "avgValence") && 
				StringUtils.isEquals(desc, "asc")) {
			Collections.sort(entryList, new Comparator<FileEntity>() {

				@Override
				public int compare(FileEntity o1, FileEntity o2) {
					return new Double(o1.getAvgValence()).compareTo(new Double(o2.getAvgValence()));
				}
			});
		}else if (StringUtils.isEquals(sortBy, "wavLength") && 
				StringUtils.isEquals(desc, "desc")) {
			Collections.sort(entryList, new Comparator<FileEntity>() {

				@Override
				public int compare(FileEntity o1, FileEntity o2) {
					return new Integer(o2.getAudioTime()).compareTo(new Integer(o1.getAudioTime()));
				}
			});
		}else if (StringUtils.isEquals(sortBy, "wavLength") && 
				StringUtils.isEquals(desc, "asc")) {
			Collections.sort(entryList, new Comparator<FileEntity>() {

				@Override
				public int compare(FileEntity o1, FileEntity o2) {
					return new Integer(o1.getAudioTime()).compareTo(new Integer(o2.getAudioTime()));
				}
			});
		}else {
			sortBy = "avgValence";
			desc = "asc";
			Collections.sort(entryList, new Comparator<FileEntity>() {

				@Override
				public int compare(FileEntity o1, FileEntity o2) {
					return new Double(o1.getAvgValence()).compareTo(new Double(o2.getAvgValence()));
				}
			});
		}
		req.setAttribute("sortBy", sortBy);
		req.setAttribute("desc", desc);
	    req.setAttribute("entryList", entryList);
	    req.getRequestDispatcher("list.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
