<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String serverPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0">
<title>Emotion</title>
<script type="text/javascript" src="js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="js/json2.js"></script>
<script type="text/javascript" src="plugins/highcharts-5.0.6/highcharts.js"></script>
<script type="text/javascript" src="plugins/highcharts-5.0.6/exporting.js"></script>
<script type="text/javascript" src="plugins/highcharts-5.0.6/dark-unica.js"></script>
<link rel="stylesheet" href="plugins/bootstrap-3.3.7/css/bootstrap.min.css" type="text/css">
<link rel="stylesheet" href="css/index.css" type="text/css">
<script>
$(function () {
// 	createTableChart('607337019124696', 'container45');
// 	createTableChart('yangguang_5', 'container8');
// 	createTableChart('yangguang_6', 'container8');
// 	createTableChart('yangguang_1', 'container9');
// 	createTableChart('yangguang_2', 'container5');
// 	createTableChart('yangguang_3', 'container6');
});

function createTableChart(wavName, containerName){
	var imgUrl = '<%=basePath%>' + 'chartimgs/' + wavName + '.png';
	$.ajax({
	    url : imgUrl,
	    async : false,
	    type : 'HEAD',
	    error : function() {
	    	createHighChart(wavName, containerName);
	    }, success : function() {
			$('#' + containerName).append("<img src='" + imgUrl + "'>");
	    }
	});
}

function createHighChart(wavName, containerName){
	var seriesOptions = [],
    seriesCounter = 0,
    names = ['arousal', 'valence'];
	var plotBandsAlert = [];
	$.each(names, function (i, name) {
		//警示区域
	    $.getJSON('emotion_source?wavName='+ wavName + '&folderName=' + name.toLowerCase() + '&callback=?', function (data) {
	        seriesOptions[i] = {
	            name: name,
	            data: data.dataArray,
	            type: 'areaspline',
	            threshold: null,
	            cursor: 'pointer',
	            lineWidth:1.5,
	            tooltip : {
	            	enabled:false
	            },
	            fillColor : {
	                linearGradient : {
	                    x1: 0,
	                    y1: 0,
	                    x2: 0,
	                    y2: 1
	                },
	                stops : [
	                    [0, Highcharts.Color(Highcharts.getOptions().colors[i]).setOpacity(0).get('rgba')],
	                    [1, Highcharts.Color(Highcharts.getOptions().colors[i]).setOpacity(0).get('rgba')]
	                ]
	            }
	        };
	        // 有警示区域
	        if(data.hasAlertArray){
	        	plotBandsAlert = data.alertArray;
	        }
	        seriesCounter += 1;
	        if (seriesCounter === names.length) {
	            createChart(containerName, seriesOptions, wavName, plotBandsAlert);
	        }
	    });
	});
}

function createChart(containerName, seriesOptions, wavName, plotBands) {
	 var chart = Highcharts.chart(containerName, {
	   	chart: { //图表，图表区、图形区和通用图表配置选项
	   		
	   	},
	   	credits: { //版权信息，Highstock 在图表的右下方放置的版权信息及链
	   		enabled: false
	   	},
	   	colors: ['#4FC9D9', '#3BCE87', '#90ed7d', '#f7a35c', '#8085e9', 
	   	         '#f15c80', '#e4d354', '#8085e8', '#8d4653', '#91e8e1'],
	   	exporting: { //导出模块，导出功能配置，导出即将图表下载为图片或打印图表
	   		enabled: false
	   	},
		legend: {//图例，用不同形状、颜色、文字等 标示不同数据列，通过点击标示可以显示或隐藏该数据列
			enabled: false
       },
       loading: {//加载中，加载选项控制覆盖绘图区的加载屏的外观和文字
       	
       },
       navigation: {//导航，导出模块按钮和菜单配置选项组
       	
       },
       plotOptions: { //数据点配置，针对不同类型图表的配置
           series: {
               //compare: 'value',
               showInNavigator: false
           }
       },
       series: seriesOptions, //数据列，图表上一个或多个数据系列，比如图表中的一条曲线，一个柱形
       title: { //标题，包括即标题和副标题，其中副标题为非必须的
       		text: ''
       },
       xAxis:{
		lineWidth :0,
    	tickWidth:0,
       	type: 'datetime',
       	dateTimeLabelFormats: {
       		millisecond: '%M:%S.%L'
       	},
       	showFirstLabel: false,
        startOnTick: false,
       	plotBands: plotBands,
        labels:{
            enabled:false
        }
       },
       yAxis: {
    	   title:{
    		   text:''
    	   },
           plotLines: [{
               value: 0,
               width: 1,
               color: '#707073'
           }],
           labels:{
               enabled:false
           }
       }
   });
	/*
   var svg = chart.getSVG().replace(/</g, '\n<').replace(/>/g, '>');
   $.ajax({
		type : 'POST',
		url : '<%=basePath%>' + 'upload_svg',
		data : JSON.stringify({
			'svg':svg,
			'wavName':wavName
		}),
		dataType : 'json',
		success : function(data){
			
		},
		error : function() {
			
		}
	});*/
}

/**
 * 根据avgValence或wavLength
 */
function sort(sortBy, desc){
	if(desc == 'asc'){
		window.location.href = "<%=basePath%>emotionlist?sortBy=" + sortBy + "&desc=desc";
	}else{
		window.location.href = "<%=basePath%>emotionlist?sortBy=" + sortBy + "&desc=asc";
	}
}
</script>
</head>
<body>
	<%@include file="header.jsp"%>
	<div class="table-data-container">
		<div class="filter-container">
			<div class="filter-option filter-title">筛选</div>
			<div class="filter-option">
				<c:if test="${sortBy=='avgValence'}">
					<span class="option-selecter" onclick="sort('avgValence', '${desc}')">严重程度</span>
				</c:if>
				<c:if test="${sortBy!='avgValence'}">
					<span onclick="sort('avgValence', '${desc}')">严重程度</span>
				</c:if>
			</div>
			<div class="filter-option">
				<c:if test="${sortBy=='wavLength'}">
					<span class="option-selecter" onclick="sort('wavLength', '${desc}')">时长</span>
				</c:if>
				<c:if test="${sortBy!='wavLength'}">
					<span onclick="sort('wavLength', '${desc}')">时长</span>
				</c:if>
			</div>
			<div class="clearfix"></div>
		</div>
		<table>
			<c:forEach items="${entryList}" var="entry" varStatus="index">
				<tr>
					<c:if test="${index.count==1}">
						<c:choose>
							<c:when test="${sortBy=='avgValence' && desc == 'asc'}">
								<td><div class="cicle-color background-red-one"></div></td>
								<td><span class="number-font-style color-red-one">${index.count}</span></td>
							</c:when>
							<c:otherwise>
								<td><div class="cicle-color background-blue-five"></div></td>
								<td><span class="number-font-style color-blue-five">${index.count}</span></td>
							</c:otherwise>
						</c:choose>
					</c:if>
					<c:if test="${index.count==2}">
						<c:choose>
							<c:when test="${sortBy=='avgValence' && desc == 'asc'}">
								<td><div class="cicle-color background-red-two"></div></td>
								<td><span class="number-font-style color-red-two">${index.count}</span></td>
							</c:when>
							<c:otherwise>
								<td><div class="cicle-color background-blue-five"></div></td>
								<td><span class="number-font-style color-blue-five">${index.count}</span></td>
							</c:otherwise>
						</c:choose>
					</c:if>
					<c:if test="${index.count==3}">
						<c:choose>
							<c:when test="${sortBy=='avgValence' && desc == 'asc'}">
								<td><div class="cicle-color background-orange-three"></div></td>
								<td><span class="number-font-style color-orange-three">${index.count}</span></td>
							</c:when>
							<c:otherwise>
								<td><div class="cicle-color background-blue-five"></div></td>
								<td><span class="number-font-style color-blue-five">${index.count}</span></td>
							</c:otherwise>
						</c:choose>
					</c:if>
					<c:if test="${index.count==4}">
						<c:choose>
							<c:when test="${sortBy=='avgValence' && desc == 'asc'}">
								<td><div class="cicle-color background-orange-four"></div></td>
								<td><span class="number-font-style color-orange-four">${index.count}</span></td>
							</c:when>
							<c:otherwise>
								<td><div class="cicle-color background-blue-five"></div></td>
								<td><span class="number-font-style color-blue-five">${index.count}</span></td>
							</c:otherwise>
						</c:choose>
					</c:if>
					<c:if test="${index.count>4}">
						<td><div class="cicle-color background-blue-five"></div></td>
						<td><span class="number-font-style color-blue-five">${index.count}</span></td>
					</c:if>
					
					<td><div class="customer-number-container">
							<div class="customer-number-container-num">${entry.fileName}.wav</div>
					   		<div class="customer-number-container-num-two">valence均值：${entry.avgValence}</div>
						</div>
					</td>
					<td><div class="time-container">
							<!-- <div><img src="images/icon_calendar.png"><span>Friday, 18 June 2015</span></div> -->
							<div><img src="images/icon_time.png"><span>${entry.audioTimeStr}</span></div>
						</div>
					</td>
					<!--  -->
					<td class="chart-container-td"><div id="container${index.count}" class="chart-container"><img src='<%=serverPath%>/emotionFiles/chartimgs/${entry.fileName}.png'></div></td>
					<td class="button-td"><div class="custom-a-container">
						<a href="detail.jsp?wave=${entry.fileName}" class="btn btn-default custom-a-btn" role="button">MORE+</a></div>
					</td>
				</tr>
			</c:forEach>
		</table>
	</div>
	<div class="footer"></div>
</body>
</html>