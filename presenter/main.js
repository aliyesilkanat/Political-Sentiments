var express = require('express');
var app = express();
var http = require('http');
var serveStatic = require('serve-static');
var db = require("./mongoconnector.js");
var generalPercentage = require('./generalPercentage.js');
var generalSentiments = require('./generalSentiments.js');
app.use(express.static('public'));

app.listen(1337);
app.get("/GeneralPercentage", function(req, res) {
	res.setHeader('Access-Control-Allow-Origin', '*');

	generalPercentage.getData(function(resultObject) {

		res.send(resultObject);
	});

});
app.get("/GeneralSentiment", function(req, res) {
	res.setHeader('Access-Control-Allow-Origin', '*');

	generalSentiments.getData(function(resultObject) {

		res.send(resultObject);
	});

});