
'use-strict'

const functions = require('firebase-functions');
//this is a function that listen to when the node 'notifications'
const admin = require('firebase-admin');
admin.initialzeApp(functions.config().firebase);

exports.sendNotification = functions.database.ref("Notification")
.onWrite(event => {
	var request = event.data.val();

	var payload = {
			title : "Horical",
			message : "vuduy309@gmail.com"		
	};

	admin.messaging().sendToDevice(request.token, payload)
	.then(function(response){
		console.log("Success: ", response);
	})
	.catch(function(error){
		console.log("Error: ", error);
	})

})


	//we need craete a 'payload' that will be sent to the device
	//the payload need to to atleast have a data or notification 
	//filed in it. i have decide to only use the data field
	//this payload be sent to the dive as a Map<String, String>
	

	//this allows us to use FCM to send to the notification
	//to the device
	
