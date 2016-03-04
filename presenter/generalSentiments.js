var exports = module.exports = {};
var async = require("async");
var db = require('./mongoconnector');

function getArray(collection, sentimentScores, done) {
	collection.count({

		'tag.result' : {
			$in : sentimentScores

		}
	}, function(err, count) {

		collection.aggregate([ {
			$match : {
				'tag.result' : {
					$in : sentimentScores
				}
			}
		}, {
			$group : {
				_id : "$mentionedPersonId",
				total : {
					$sum : 1
				}
			}
		}, {
			"$project" : {
				"value" : {
					"$multiply" : [ {
						"$divide" : [ 100, count ]
					}, "$total" ]
				}
			}
		} ], function(err, result) {
			async.each(result, function(docs, callback) {
				db.getCollection('fetching_people', function(collection) {
					collection.findOne({
						personId : docs._id
					}, function(err, person) {
						docs.label = person.fullName;
						docs.color = person.color;
						docs.value = Math.round(docs.value);
						callback();
					});
				});

			}, function(err) {
				done(null, result);
			});

		});

	});

}
exports.getData = function(writeStream) {

	db.getCollection("tweets", function(collection) {

		var resultObject = {};
		async.parallel({
			positive : function(callback) {
				getArray(collection, [ 1, 3 ], callback);

			},
			negative : function(callback) {
				getArray(collection, [ 2, 4 ], callback);
			}
		}, function(err, results) {
			writeStream(results);
		});
	});
};