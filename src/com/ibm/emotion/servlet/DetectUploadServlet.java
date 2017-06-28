package com.ibm.emotion.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.emotion.util.FileUtil;
import com.ibm.emotion.util.LogUtil;
import com.ibm.emotion.util.StringUtils;
import com.ibm.emotion.util.UploadUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DetectUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 6293542699139646494L;
	private static final String INPUT_IMG_FILE_PATH = "/home/ibm/project/EM_demo/input.jpg";
	private static final String FINISH_LOG_FILE_PATH = "/home/ibm/project/EM_demo/finish.log";
	private static final String OUTPUT_TXT_FILE_PATH = "/home/ibm/project/EM_demo/output.txt";
	private static final String FACE_RANGE_FILE_PATH = "/home/ibm/project/EM_demo/face_range.txt";
	private static final String[] EMOTION_TAGS = {"neutral", "happy", "amazing", "sad", "angry", "hate", "fear", "scorn"};
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long start = System.currentTimeMillis();
		req.setCharacterEncoding("utf-8");
		resp.setContentType("application/json;charset=utf-8");
		JSONArray dect_results = new JSONArray();
		File inputJpg = new File(INPUT_IMG_FILE_PATH);
		if (!inputJpg.exists()) {
			long saveStart = System.currentTimeMillis();
			JSONObject object = UploadUtil.saveUploadFile(req);
			long saveEnd = System.currentTimeMillis();
			LogUtil.printlog("存储图片用时：" + (saveEnd - saveStart));
			if (object.getBoolean("status")) {
				long detect_start = System.currentTimeMillis();
				while (true) {
					File finishLogFile = new File(FINISH_LOG_FILE_PATH);
					if (finishLogFile.exists()) {
						// x,y width height
						File outputFile = new File(OUTPUT_TXT_FILE_PATH);
						String outputString = FileUtil.readFile(outputFile);
						if (!StringUtils.isEquals("no found", outputString)) {
							String emotionScores = FileUtil.readOutputFile(outputFile);
							JSONObject faceScoreObj = new JSONObject();
							if (!StringUtils.isEmpty(emotionScores)) {
								long detect_end = System.currentTimeMillis();
								LogUtil.printlog("识别表情用时：" + (detect_end - detect_start));
								
								String[] scores = emotionScores.split(",");
								if (scores.length == 8) {
									faceScoreObj = getHighLightEmotion(scores, faceScoreObj);
								}else {
									LogUtil.printlog("scores.length:" + scores.length);
								}
							}else {
								LogUtil.printlog("emotionScores is empth or null");
							}
							File faceFile = new File(FACE_RANGE_FILE_PATH);
							if (faceFile.exists()) {
								String faceRange = FileUtil.readFile(faceFile);
								if (!StringUtils.isEmpty(faceRange)) {
									String[] faceRangeValue = faceRange.split(",");
									int x = Integer.valueOf(faceRangeValue[0]);
									int y = Integer.valueOf(faceRangeValue[1]);
									int width = Integer.valueOf(faceRangeValue[2]);
									int height = Integer.valueOf(faceRangeValue[3]);
									JSONObject out = new JSONObject();
									out.put("xmin", x);
									out.put("ymin", y);
									out.put("xmax", x + width);
									out.put("ymax", y + height);
									out.put("face_scores", faceScoreObj);
									dect_results.add(out);
								}
								faceFile.delete();
							}
						}else {
							LogUtil.printlog("outputFile str is no found");
						}
						outputFile.delete();
						finishLogFile.delete();
						break;
					}
				}
			}
		}else {
			LogUtil.printlog("input.jpg" + "已存在");
		}
		resp.getWriter().write(dect_results.toString());
		long end = System.currentTimeMillis();
		LogUtil.printlog(dect_results.toString());
		LogUtil.printlog("处理总用时" + (end - start) + "ms");
		/*
		 * 保存input.jpg用时：7 保存input.jpg后到产生outputFile用时：78 共用时：85
		 */
	}

	/**
	 * 获取分值最大的表情
	 * 
	 * @param scores
	 * @return
	 */
	private JSONObject getHighLightEmotion(String[] scores, JSONObject dest) {
		String[] sort_tags = {"neutral", "happy", "amazing", "sad", "angry", "hate", "fear", "scorn"};
		String[] scores_copy = scores.clone();
		for (int i = 0; i < scores.length; i++) {
			JSONObject obj = new JSONObject();
			obj.put("score", scores[i]);
			obj.put("highLight", "false");
			dest.put(EMOTION_TAGS[i], obj);
		}
		for (int i = 0; i < scores_copy.length - 1; i++) {
			for (int j = 0; j < scores_copy.length - i - 1; j++) {
				if (Double.valueOf(scores_copy[j]) < Double.valueOf(scores_copy[j+1])) {
					// 交换标签
			        String temp = sort_tags[j];   
			        sort_tags[j] = sort_tags[j + 1];   
			        sort_tags[j + 1] = temp;
			        // 交换分数
			        String score_temp = scores_copy[j];   
			        scores_copy[j] = scores_copy[j + 1];   
			        scores_copy[j + 1] = score_temp;
				}
			}
		}
		// 取出分值最大的
		JSONObject maxScoreObj = dest.getJSONObject(sort_tags[0]);
		maxScoreObj.put("highLight", "true");
		JSONObject sortedObj = new JSONObject();
		if (Double.valueOf(maxScoreObj.getString("score")) < 0.8) {
			sortedObj.put("highLightNum", 2);
			dest.getJSONObject(sort_tags[1]).put("highLight", "true");
		}else {
			sortedObj.put("highLightNum", 1);
		}
		// 排序后的tag和分值
		JSONArray sortArray = new JSONArray();
		for (String tag : sort_tags) {
			JSONObject each_obj = dest.getJSONObject(tag);
			JSONObject sortObj = new JSONObject();
			sortObj.put("tag", tag);
			sortObj.put("score", each_obj.getString("score"));
			sortArray.add(sortObj);
		}
		sortedObj.put("sortArray", sortArray);
		dest.put("sorted", sortedObj);
		return dest;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
}
