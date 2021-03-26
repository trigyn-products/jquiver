class ApplicationMetrics {
		
	gcOptions = {
	    animation: false,
	    scaleOverride: true,
	    scaleStartValue: 0,
	    fill: false
	  };	
	  
	constructor() {
		this.gcData = {
			labels : [new Date().getHours() + ":" + new Date().getMinutes()],
			datasets: [{
		      label: "GC Live Data",
		      borderColor: "rgb(54, 162, 235)",
		      backgroundColor: "rgba(54, 162, 235, 1)",
		      pointBackgroundColor: "rgb(54, 162, 235)",
		      pointStrokeColor: "#fff",
		      fill: false,
		      pointHighlightFill: "#fff",
		      data: [0]
		    }]
		}
	}
	
	createClassLoadingDetails = function(data) {
		let classesData = data;
		$("#totalClassLoaded").html(classesData["total-classes-loaded "]);
		$("#classLoaded").html(classesData["classes-loaded "]);
		$("#classUnloaded").html(classesData["classes-unloaded "]);
	}
	
	createGCDetails = function(data) {
		if(data == undefined) {
			data = appMetrics.applicationDetails["gc-metrics"]
		}
		appMetrics.gcData["datasets"][0].data.push(appMetrics.convertFromByteToMegaByte(data["GC Live Data Size "]));
		appMetrics.gcData["labels"].push(new Date().getHours() + ":" + new Date().getMinutes());
		var ctx = document.getElementById("gc-metrics-chart").getContext("2d");
		var gcChart = new Chart(ctx, {
			type: "line",
			data: appMetrics.gcData,
			options: appMetrics.gcOptions
		});
	}
	
	createSystemDetails = function(data) {
		if(data == undefined) {
			data = appMetrics.applicationDetails["system-metrics"]
		}
		$("#serverStartTime").html(new Date(data["start-time"]));
		$("#cpuCores").html(data["system-cpu-count"]);
		$("#cpuUsage").html(data["system-cpu-usage"]);
		$("#processCpuUsage").html(data["process-cpu-usage"]);
		$("#cpuloadAvg").html(data["system-load-average"]);
		let upTimeDate = new Date(data["uptime"])
		$("#uptime").html(upTimeDate.getHours() + ":" + upTimeDate.getMinutes());
	}
	
	createThreadDetails = function(data) {
		debugger;
		if(data == undefined) {
			data = appMetrics.applicationDetails["thread-metrics"]
		}
		
//		let labels = Object.keys(data);
		let labels = new Array();
		let pointValues = new Array();
		
		for(let counter = 0; counter < Object.keys(data).length; counter++) {
			let key = Object.keys(data)[counter];
			let pointValue = data[Object.keys(data)[counter]];
			if(key != "TOTAL_THREAD_COUNT" && pointValue !=0) {
		    	labels.push(key);
			    pointValues.push(pointValue);
		    }
		}
		
		let datasets = new Array();
		let dataValue = {
			  label: "Thread status",
		      backgroundColor: [
	              '#66CCFF',
	              '#FFFF66',
                  '#99CC00',
	              '#2aebbe',
	              '#f57fd9',
                  '#d1eb2a',
				],
		    }
//		let pointValues = new Array();
//		for(let counter = 0; counter < Object.keys(data).length; counter++) {
//			let pointValue = data[Object.keys(data)[counter]];
//		    pointValues.push(pointValue);
//		}
		dataValue["data"] = pointValues;
		datasets.push(dataValue);
		
		let chartData = {labels: labels, datasets: datasets}
		var ctx = document.getElementById("thread-metrics-chart").getContext("2d");
		let threadDetailsChart = new Chart(ctx, {
			type: "doughnut",
			data: chartData,
			options: {
				animation: false,
				/*legend: {
		            display: false
		         },*/
				legend: {
		    		position: 'right',
		    		labels: {		    			
		                fontColor: '#333',
		                fontStyle: 'bold'
		            }
		        }
			}
		});
		
		
		
	}
	
	createMemoryPoolDetails = function(data) {
		let context = this;
		if(data == undefined) {
			data = appMetrics.applicationDetails["memory-pool-metric"]
			context = appMetrics;
		}
		
		let labels = Object.keys(data);
		let datasets = new Array();
		let initdataset = {
		         label: "init",
		         data: [],
		         backgroundColor: "rgba(255, 255, 102, 1)",
			     borderColor: "rgba(255, 255, 102, 1)",
			     pointColor: "rgba(255, 255, 102, 1)",
	    }
	    
	    let useddataset = {
	         label: "used",
	         data: [],
	         backgroundColor: "rgba(153, 204, 0, 1)",
		     borderColor: "rgba(153, 204, 0, 1)",
		     pointColor: "rgba(153, 204, 0, 1)",
	    }
	    
	    let committeddataset = {
	         label: "committed",
	         data: [],
	         backgroundColor: "rgba(255, 102, 204, 1)",
		     borderColor: "rgba(255, 102, 204, 1)",
		     pointColor: "rgba(255, 102, 204, 1)",
	    }
	    
	    let maxdataset = {
	         label: "max",
	         data: [],
	         backgroundColor: "rgba(54, 162, 235, 1)",
		     borderColor: "rgba(54, 162, 235, 1)",
		     pointColor: "rgba(54, 162, 235, 1)",
		}
		
		for(let counter = 0; counter < labels.length; counter++) {
			let attrData = data[labels[counter]];
			initdataset.data.push(context.convertFromByteToMegaByte(attrData["init"]));
			useddataset.data.push(context.convertFromByteToMegaByte(attrData["used"]));
			committeddataset.data.push(context.convertFromByteToMegaByte(attrData["committed"]));
			maxdataset.data.push(context.convertFromByteToMegaByte(attrData["max"]));
		}
		datasets.push(initdataset);
		datasets.push(useddataset);
		datasets.push(committeddataset);
		datasets.push(maxdataset);
		let chartData = {labels: labels, datasets: datasets}
		var ctx = document.getElementById("memory-metrics-chart").getContext("2d");
		let threadDetailsChart = new Chart(ctx, {
			type: "bar",
			data: chartData,
			options: {
			  animation: false,
		      responsive: true,
		      scales: {
		         xAxes: [{
		            stacked: true // this should be set to make the bars stacked
		         }],
		         yAxes: [{
		            stacked: true // this also..
		         }]
		      }
			}
		});
	}

	createHttpTraceDetails = function(data) {
		let context = this;
		if(data == undefined) {
			data = appMetrics.applicationDetails["http-trace-metrics"]
		}
		let details = Object.keys(data);
		
		let tabData = '<div class="cm-boxwrapper"  id="mainTab">';
			tabData = tabData + '<div class="cm-boxleft cm-scrollbar">';
			tabData = tabData + '<div class="tab">';
						for(let counter = 0; counter < details.length; counter++) {
							let tabVal = details[counter];
							tabData = tabData + '<button class="tablinks" id= "'+tabVal+'" onclick="openTab(event, \'' + tabVal.trim() +'\')">'+tabVal +'<img src="'+contextPath+'/webjars/1.0/images/s-information1.svg"></button>';
						}
			tabData = tabData + '</div>'
			tabData = tabData + '</div>'
			tabData = tabData + '<div class="cm-boxright cm-scrollbar">';
			tabData = tabData + '<div id="tabContentData">';
			tabData = tabData + "<div id='http-trace-url-details'> " ;
			tabData = tabData + "<div id='httpDetails'>";
			tabData = tabData + "<div><div id='requestDetails-head'>Request URL  </div>"+"<span id='requestDetails'></span></div>";
			tabData = tabData + "<div><div id='responseDetails-head'>Response Status  </div>" +"<span id='responseDetails'></span></div>";
			tabData = tabData + "<div><div id='auxilaryDetails-head'>Method Description  </div>" +"<span id='auxilaryDetails'></span></div>";
			tabData = tabData + "<div><div id='requestTimestamp-head'>Time  </div>"+"<span id='requestTimestamp'></span></div>";
			tabData = tabData + "<div><div id='requestMinTime-head'>Min Time (in min:sec.ms)  </div>"+"<span id='requestMinTime'></span></div>";
			tabData = tabData + "<div><div id='requestMaxTime-head'>Max Time (in min:sec.ms)  </div>"+"<span id='requestMaxTime'></span></div>";
			tabData = tabData + "<div><div id='requestAvgTime-head'>Average Time (in min:sec.ms)  </div>"+"<span id='requestAvgTime'></span></div>";
			tabData = tabData + "</div> ";
			tabData = tabData +  "</div>";
			tabData = tabData + '</div>';
			tabData = tabData + '</div>';
			tabData = tabData + '</div>';
		let component = $(tabData);
		$("#http-trace-metrics-content").html("");
		$("#http-trace-metrics-content").append(component);

		for(let counter = 0; counter < details.length; counter++) {
			if(counter == 0) {
				document.getElementById(details[counter]).click();
				break;
			}
		}
	}
	
	createSystemEnvironment = function(data){
		for (const property in data) {
			$("#sys-env-content").append('<div class=""><label>'+property+'  </label> <span id="'+data[property]+'">'+data[property]+'</span></div>');
		}
	}
	
	createSystemProperties = function(data){
		for (const property in data) {
			$("#sys-prop-content").append('<div class=""><label>'+property+'  </label> <span id="'+data[property]+'">'+data[property]+'</span></div>');
		}
	}
	
	convertFromByteToMegaByte(bytes) {
		if(bytes < 0) {
			return 0;
		}
		return bytes / (1024*1024);
	}
	
	convertFromByteTokiloByte(bytes) {
		return bytes / 1024;
	}
	
	getRandomColor() {
	  var letters = '0123456789ABCDEF';
	  var color = '#';
	  for (var i = 0; i < 6; i++) {
	    color += letters[Math.floor(Math.random() * 16)];
	  }
	  return color;
	}
	
	initDetails() {
		this.createClassLoadingDetails(this.applicationDetails["classloading-metrics"]);
		
	    this.createGCDetails(this.applicationDetails["gc-metrics"]);
	    setInterval(this.createGCDetails.bind(), 1000 * 30);
	    
	    this.createSystemDetails(this.applicationDetails["system-metrics"]);
	    setInterval(this.createSystemDetails.bind(), 6000);
	    
	    this.createThreadDetails(this.applicationDetails["thread-metrics"]);
	    setInterval(this.createThreadDetails.bind(), 6000);
	    
	    this.createMemoryPoolDetails(this.applicationDetails["memory-pool-metric"]);
	    setInterval(this.createMemoryPoolDetails.bind(), 6000);
	    
	    this.createHttpTraceDetails(this.applicationDetails["http-trace-metrics"]);
	    setInterval(this.createHttpTraceDetails.bind(), 1000 * 60);
	    
	   	this.createSystemEnvironment(this.applicationDetails["system-env-map"]);
	    setInterval(this.createSystemEnvironment.bind(), 1000 * 60);
	    
	   	this.createSystemProperties(this.applicationDetails["system-properties"]);
	    setInterval(this.createSystemProperties.bind(), 1000 * 60);
    }            
	
	millisToMinutesAndSeconds(duration) {
		let milliseconds = (duration % 1000) / 100;
		let seconds = Math.floor((duration / 1000) % 60);
		let minutes = Math.floor((duration / (1000 * 60)) % 60);
		    
		minutes = (minutes < 10) ? "0" + minutes : minutes;
		seconds = (seconds < 10) ? "0" + seconds : seconds;

		return minutes + ":" + seconds + "." + milliseconds;
	}

}
function openTab(evt, url) {
  	var i, tablinks;
  	tablinks = document.getElementsByClassName("tablinks");
  	for (i = 0; i < tablinks.length; i++) {
    	tablinks[i].className = tablinks[i].className.replace(" active", "");
  	}
  	
  	let urldetails = appMetrics.applicationDetails["http-trace-metrics"][url];
  	$("#requestDetails").html("");
	$("#auxilaryDetails").html("");
	$("#responseDetails").html("");
	$("#requestTimestamp").html("");
	$("#requestMinTime").html("");
	$("#requestMaxTime").html("");
	$("#requestAvgTime").html("");
	if(urldetails != null) {
		var requestURL = JSON.parse(urldetails["httpRequestDetails"])["updated-request-url"];
		$("#requestDetails").append(requestURL);
		
		var methodDesc = JSON.parse(urldetails["auxillaryDetails"])["method-description"];
		$("#auxilaryDetails").append(methodDesc);
		
		var responseStatus = JSON.parse(urldetails["httpResponseDetails"])["response-status"];
		$("#responseDetails").append(responseStatus);
		
		$("#requestTimestamp").append(urldetails["requestTimestamp"]);

		$("#requestMinTime").append(appMetrics.millisToMinutesAndSeconds(urldetails["minRequestDuration"]));
		
		$("#requestMaxTime").append(appMetrics.millisToMinutesAndSeconds(urldetails["maxRequestDuration"]));
		
		$("#requestAvgTime").append(appMetrics.millisToMinutesAndSeconds(urldetails["averageRequestDuration"]));
	}
  	evt.currentTarget.className += " active";
}