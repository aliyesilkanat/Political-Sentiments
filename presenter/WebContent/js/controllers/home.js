var app = angular.module('app', []);
app.controller('HomeCtrl', function($scope, $http) {
	$scope.firstName = "John";
	$scope.lastName = "Doe";
	$http({
		method : 'GET',
		url : 'GeneralPercentageServlet'
	}).success(function(data) {
		drawGeneralRatios(data.values);
		$scope.totalTweetCount = data.count;
	});
	$http({
		method : 'GET',
		url : 'GeneralSentimentServlet'
	}).success(function(data) {
		drawSentimentRatios(data);
	});

});
function drawSentimentRatios(data) {
	drawPieChart(data.positive, "80%", positiveRatio);
	drawPieChart(data.negative, "80%", negativeRatio);
}

function drawGeneralRatios(data) {
	drawPieChart(data, "100%", "generalRatio");
}
function drawPieChart(data, radius, id) {
	var pie = new d3pie(id, {

		"size" : {
			"canvasHeight" : 400,
			"canvasWidth" : 590,
			"pieOuterRadius" : radius
		},
		"data" : {
			"content" : data
		},
		"labels" : {
			"outer" : {
				"pieDistance" : 32
			},
			"inner" : {
				"format" : "value"
			},
			"mainLabel" : {
				"font" : "Open Sans",
				"fontSize" : 15
			},
			"percentage" : {
				"color" : "#e1e1e1",
				"font" : "verdana",
				"decimalPlaces" : 0
			},
			"value" : {
				"color" : "#e1e1e1",
				"font" : "verdana"
			},
			"lines" : {
				"enabled" : true,
				"color" : "#cccccc"
			},
			"truncation" : {
				"enabled" : true
			}
		},
		"effects" : {
			"pullOutSegmentOnClick" : {
				"effect" : "linear",
				"speed" : 400,
				"size" : 8
			}
		}
	});
}