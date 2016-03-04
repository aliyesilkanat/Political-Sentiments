var exports = module.exports = {};
var async = require("async");
var db = require('./mongoconnector');
exports.getData = function(writeStream) {

	db.getCollection("tweets", function(collection) {
		collection.count(function(err, count) {
			collection.aggregate([ {
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
					var resultObject = {
						values : result,
						count : count
					};
					writeStream(resultObject);
				});

			});

		});
	});

};