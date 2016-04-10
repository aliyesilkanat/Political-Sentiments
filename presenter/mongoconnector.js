var exports = module.exports = {};

var MongoClient = require('mongodb').MongoClient;
var url = 'mongodb://admin:121121121@ds019048.mlab.com:19048/politicalsentiments';

var db = null;
MongoClient.connect(url, function(err, database) {
	if (err) {
		console.log("Cannot connect mongo database");
		throw err;
	}
	console.log("Connected correctly to mongo database");
	db = database;
});
exports.getDatabase = function(callback) {
	callback(db);

};
exports.getCollection = function(collectionName, callback) {

	callback(db.collection(collectionName));
};