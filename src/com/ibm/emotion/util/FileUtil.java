package com.ibm.emotion.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.csvreader.CsvReader;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class FileUtil {

	/**
	 * 读取CSV文件
	 * @return 
	 */
	public static String readCsv(String filePath, Double alertValue, Boolean isAlertRegion) {
		JSONObject outputObj = new JSONObject();
		JSONArray datasArray = new JSONArray();
		JSONArray regionArray = new JSONArray();
		try {
			CsvReader reader = new CsvReader(filePath, ',', Charset.forName("SJIS")); // 一般用这编码读就可以了
//			reader.readHeaders(); // 跳过表头 如果需要表头的话，不要写这句。
			int row = 1;
			//需要警示区域
			int from = -1;
			int to = -1;
			while (reader.readRecord()) { // 逐行读入除表头的数据
				String cell = reader.getValues()[0];
				Double value = Double.valueOf(cell);
				JSONArray array = new JSONArray();
				array.add(row*40);
				array.add(value);
				datasArray.add(array);
				// 是否计算警示区域
				if (isAlertRegion) {
					//计算警示区域
					if (value < alertValue) {
						if (from == -1 && to == -1) {
							from = row*40;//开始记录初始位置
						}
					}else {
						if (from > 0) { // 已经记录初始位置
							to = (row-1)*40;
							JSONObject alertRegion = new JSONObject();
							alertRegion.put("from", from);
							alertRegion.put("to", to);
							alertRegion.put("color", "rgba(21, 90, 131, 0.5)");
							regionArray.add(alertRegion);
						}
						from = -1;
						to = -1;
					}
				}
				row ++;
			}
			if (from > 0 && to == -1) {
				to = (row-1)*40;
				JSONObject alertRegion = new JSONObject();
				alertRegion.put("from", from);
				alertRegion.put("to", to);
				alertRegion.put("color", "rgba(21, 90, 131, 0.5)");
				regionArray.add(alertRegion);
			}
			reader.close();
		} catch (Exception ex) {
			System.out.println(ex);
		}
		outputObj.put("dataArray", datasArray);
		outputObj.put("hasAlertArray", isAlertRegion);
		outputObj.put("alertArray", regionArray);
		return outputObj.toString();
	}
	
	/**
	 * 读取文件
	 * @return
	 */
	public static String readFile(File file){
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String temp = null;
			while ((temp = reader.readLine()) != null) {
				sb.append(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 写文件
	 * @param content
	 */
	public static void writeFile(String filePath, String content){
		try {
			FileWriter writer = new FileWriter(filePath);
			PrintWriter out = new PrintWriter(writer);
			out.write(content);
			writer.close();  
	        out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** 
     * 获取视频总时间 
     * @param viedo_path    视频路径 
     * @param ffmpeg_path   ffmpeg路径 
     * @return [0]时长字符串，[1]时长毫秒数
     */  
    public static Object[] getAudioTime(String audio_path, String ffmpeg_path) {
    	Object[] object = new Object[2];
        List<String> commands = new ArrayList<String>();
        commands.add(ffmpeg_path);  
        commands.add("-i");  
        commands.add(audio_path);  
        try {  
            ProcessBuilder builder = new ProcessBuilder();  
            builder.command(commands);  
            final Process p = builder.start();  
            //从输入流中读取视频信息  
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));  
            StringBuffer sb = new StringBuffer();  
            String line = "";  
            while ((line = br.readLine()) != null) {  
                sb.append(line);  
            }  
            br.close();  
            
            //从视频信息中解析时长  
            String regexDuration = "Duration: (.*?), bitrate: (\\d*) kb\\/s";
            Pattern pattern = Pattern.compile(regexDuration);  
            Matcher m = pattern.matcher(sb.toString());  
            if (m.find()) {
                int time = getTimelen(m.group(1));
//                System.out.println(audio_path + ",视频时长：" + time + ",比特率：" + m.group(2) + "kb/s");
                object[0] = m.group(1);
                object[1] = time;
                return object;  
            }  
        } catch (Exception e) {
            e.printStackTrace();  
        }  
        return null;
    }  
      
    //格式:"00:00:10.68"  
    private static int getTimelen(String timelen){
        int min = 0;
        String strs[] = timelen.split(":");  
        if (strs[0].compareTo("0") > 0) {
            min+=Integer.valueOf(strs[0])*60*60;//秒  
        }  
        if(strs[1].compareTo("0")>0){  
            min+=Integer.valueOf(strs[1])*60;  
        }  
        if(strs[2].compareTo("0")>0){  
            min+=Math.round(Float.valueOf(strs[2]));  
        }  
        return min;  
    }
    
    /**
	 * 读取文件
	 * @return
	 */
	public static String readOutputFile(File file){
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String temp = null;
			boolean isAppend = false;
			while ((temp = reader.readLine()) != null) {
				if (isAppend) {
					sb.append(",");
				}
				sb.append(temp);
				isAppend = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
    
    public static void main(String[] args) {
    	// File file = new File("/"); D盘根目录
    	// ../ 回到项目所在路径
    	// ./  回到项目根目录
		File file = new File("./WebContent/face_range.txt");
		System.out.println(FileUtil.readFile(file));
	}
}
