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
		if(data == undefined) {
			data = appMetrics.applicationDetails["thread-metrics"]
		}
		
		let labels = Object.keys(data);
		let datasets = new Array();
		let dataValue = {
			  label: "Thread status",
		      backgroundColor: "rgba(54, 162, 235, 1)",
		      borderColor: "rgba(54, 162, 235, 1)",
		      pointColor: "rgba(54, 162, 235, 1)",
		    }
		let pointValues = new Array();
		for(let counter = 0; counter < Object.keys(data).length; counter++) {
			let pointValue = data[Object.keys(data)[counter]];
		    pointValues.push(pointValue);
		}
		dataValue["data"] = pointValues;
		datasets.push(dataValue);
		let chartData = {labels: labels, datasets: datasets}
		var ctx = document.getElementById("thread-metrics-chart").getContext("2d");
		let threadDetailsChart = new Chart(ctx, {
			type: "bar",
			data: chartData,
			options: {
				animation: false
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
		         backgroundColor: context.getRandomColor()
	    }
	    
	    let useddataset = {
	         label: "used",
	         data: [],
	         backgroundColor: context.getRandomColor()
	    }
	    
	    let committeddataset = {
	         label: "committed",
	         data: [],
	         backgroundColor: context.getRandomColor()
	    }
	    
	    let maxdataset = {
	         label: "max",
	         data: [],
	         backgroundColor: context.getRandomColor()
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
		if(data == undefined) {
			data = appMetrics.applicationDetails["http-trace-metrics"]
		}
		console.log(data);
		//$("#http-trace-metrics-content").html(JSON.stringify(data));
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
    }            
}