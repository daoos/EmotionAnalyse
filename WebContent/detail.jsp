<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<script type="text/javascript" src="plugins/highcharts-5.0.6/highstock.js"></script>
<script type="text/javascript" src="http://code.highcharts.com/modules/exporting.js"></script>
<script type="text/javascript" src="plugins/wavesurfer/wavesurfer.min.js"></script>
<script type="text/javascript" src="plugins/highcharts-5.0.6/dark-unica.js"></script>
<link rel="stylesheet" href="plugins/bootstrap-3.3.7/css/bootstrap.min.css" type="text/css">
<link rel="stylesheet" href="css/detail.css" type="text/css">
<script>
$(function () {
    var seriesOptions = [],
        seriesCounter = 0,
        names = ['arousal', 'valence'];
    var plotBandsAlert = [];
    var  wavName = '<%=request.getParameter("wave") %>';
    /**
     * Create the chart when all data is loaded
     * @returns {undefined}
     */
    function createChart(plotBands) {
        $('#container').highcharts('StockChart', {
        	chart: { //图表，图表区、图形区和通用图表配置选项
        		zoomType:'x'
        	},
        	credits: { //版权信息，Highstock 在图表的右下方放置的版权信息及链
        		enabled: false
        	},
        	colors: ['#4FC9D9', '#3BCE87', '#90ed7d', '#f7a35c', '#8085e9', 
        	         '#f15c80', '#e4d354', '#8085e8', '#8d4653', '#91e8e1'],
        	exporting: { //导出模块，导出功能配置，导出即将图表下载为图片或打印图表
        		enabled: true
        	},
			legend: {//图例，用不同形状、颜色、文字等 标示不同数据列，通过点击标示可以显示或隐藏该数据列
				enabled: true
            },
            loading: {//加载中，加载选项控制覆盖绘图区的加载屏的外观和文字
            	
            },
            navigation: {//导航，导出模块按钮和菜单配置选项组
            	
            },
            navigator: {
                height: 50
            },
            plotOptions: { //数据点配置，针对不同类型图表的配置
                series: {
                    //compare: 'value',
                    showInNavigator: true
                }
            },
            rangeSelector : { //范围选择器，选择查看指定时间的数据，可以是年、月为单位或全部数据，也可以是输入具体的时间范围查看
                buttons : [{
                    type : 'all',
                    count : 1,
                    text : 'All'
                }],
                selected : 1,
                inputEnabled : true,
                inputDateFormat: '%H:%M:%S.%L',
                inputEditDateFormat: '%H:%M:%S.%L'
            },
            scrollbar: { //滚动条，图表底部的滚动条，大数据量时可以方便查看局部数据
            	
            },
            series: seriesOptions, //数据列，图表上一个或多个数据系列，比如图表中的一条曲线，一个柱形
            title: { //标题，包括即标题和副标题，其中副标题为非必须的/情感分析图示
            	text: ''
            },
            tooltip: { //数据点提示框
                pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b><br/>',
                valueDecimals: 6,
                split: true
            },
            xAxis:{
            	type: 'datetime',
            	dateTimeLabelFormats: {
            		millisecond: '%M:%S.%L'
            	},
            	showFirstLabel: false,
                startOnTick: false,
            	plotBands: plotBands
            },
            yAxis: {
                labels: {
                    formatter: function () {
                        return (this.value > 0 ? ' + ' : '') + this.value;
                    },
                	enabled:true
                },
                plotLines: [{
                    value: 0.1,
                    width: 2,
                    color: 'silver',
                    label: {
                        text: '0.1',
                        align: 'right',
                        x: 0,
                        style: {"color": "#E0E0E3"}
                    }
                }]
            }
        });
    }
    // line：直线图  spline：曲线图  area：面积图  areaspline：曲线面积图  arearange：面积范围图  areasplinerange：曲线面积范围图
    //column：柱状图  columnrange：柱状范围图  flags：柱状范围图  ohlc：K线图  candlestick：阴阳烛图  scatter：散点图
    $.each(names, function (i, name) {
        $.getJSON('emotion_detail?wavName='+ wavName + '&folderName=' + name.toLowerCase() + '&callback=?', function (data) {
            seriesOptions[i] = {
                name: name,
                data: data.dataArray,
                type: 'areaspline',
                threshold: null,
                cursor: 'pointer',
                tooltip : {
                    valueDecimals : 8
                },
                fillColor : {
                    linearGradient : {
                        x1: 0,
                        y1: 0,
                        x2: 0,
                        y2: 1
                    },
                    stops : [
                        [0, Highcharts.Color(Highcharts.getOptions().colors[i]).setOpacity(0.2).get('rgba')],
                        [1, Highcharts.Color(Highcharts.getOptions().colors[i]).setOpacity(0.2).get('rgba')]
                    ]
                }
            };
         // 有警示区域
	        if(data.hasAlertArray){
	        	plotBandsAlert = data.alertArray;
	        }
            seriesCounter += 1;
            if (seriesCounter === names.length) {
                createChart(plotBandsAlert);
            }
        });
    });
    
    //音频播放 https://wavesurfer-js.org/docs/
    var wavesurfer = WaveSurfer.create({
        container: '#waveform',
        waveColor: '#00CCFF',
        progressColor: '#00CCFF',
        cursorColor: '#ffffff'
    });
    var filePath = '<%=serverPath%>/emotionFiles/callcenter/' + wavName + '.wav';
    wavesurfer.load(filePath);
    wavesurfer.on('ready', function () {
        //wavesurfer.play();
    	//wavesurfer.seekTo(0.5);
    });
    var playRate = 1;
    $("#play_slow").click(function() {
    	playRate = playRate/2;
    	wavesurfer.setPlaybackRate(playRate);
    });
    $("#play").click(function(){
    	//isPlaying()
    	var duration = wavesurfer.getDuration()*1000;//毫秒数计算
    	var inputStart = $(".highcharts-label.highcharts-range-input > text").first().text();
    	var timesplit = inputStart.split(":");
    	var secondsAndMilliseconds = timesplit[2].split(".");
    	var selectedPosition = (parseInt(timesplit[0]*60*60) + parseInt(timesplit[1]*60) + parseInt(secondsAndMilliseconds[0]))*1000 + parseInt(secondsAndMilliseconds[1]);
    	wavesurfer.seekTo(selectedPosition/duration);
    	wavesurfer.play();
    });
    function stopPlay(){
    	var currProgress = wavesurfer.getCurrentTime()*1000;
    	var inputEnd = $($(".highcharts-label.highcharts-range-input > text")[1]).text();
    	var timesplit = inputEnd.split(":");
    	var secondsAndMilliseconds = timesplit[2].split(".");
    	var selectedEndPosition = (parseInt(timesplit[0]*60*60) + parseInt(timesplit[1]*60) + parseInt(secondsAndMilliseconds[0]))*1000 + parseInt(secondsAndMilliseconds[1]);
    	if(currProgress >= selectedEndPosition){
    		wavesurfer.pause();
    		clearInterval();
    	}
    }
 	// 监听播放
    wavesurfer.on('play', function () {
    	setInterval(stopPlay, 1000);
    });
    $("#pause").click(function(){
    	wavesurfer.pause();
    });
    $("#play_fast").click(function(){
    	playRate = playRate*2;
    	wavesurfer.setPlaybackRate(playRate);
    });
});
</script>
</head>
<body>
	<%@include file="header.jsp"%>
	<div class="content">
		<div id="container" style="height: 65%"></div>
		<div id="report"></div>
		<div class="wave-container">
			<div class="wave-opration-container">
				<div class="option-btn-container">
					<div class="option-btn" id="play_slow"><img src="images/icon_wav_slow.png"></div>
					<div class="option-btn" id="play"><img src="images/icon_wav_play.png"></div>
					<div class="option-btn" id="pause"><img src="images/icon_wav_pause.png"></div>
					<div class="option-btn" id="play_fast"><img src="images/icon_wav_fast.png"></div>
					<div class="clearfix"></div>
				</div>
			</div>
			<div id="waveform"></div>
		</div>
	</div>
</body>
</html>