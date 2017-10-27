'use strict'
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);
exports.sendNotification = functions.database.ref('/published/{msg_id}').onWrite(event => {
  const snapshot = event.data;
  // Only send a notification when a new message has been created.
  if (snapshot.previous.val()) {
    return;
  }
  const msg_id = event.params.msg_id;

  const msg_val=admin.database().ref(`messages/${msg_id}`).once('value');
  return msg_val.then(msgResult =>{
    const msg_title=msgResult.val().title;
    const user_id=msgResult.val().userId;
    console.log('msg title is',msg_title);
     console.log('We have a new article : ', msg_id);
 // const deviceToken = admin.database().ref('/FCMToken/{user_id}').once('value');
admin.database().ref('/FCMToken').on("child_added", function(snapshot)
{

  

  const deviceToken=console.log("device token" + snapshot.val());
  //return deviceToken.then(result => {
    const token_id=snapshot.val();
    const payload={
    
        data : {
       	  title:"New Article",
          body: msg_title,
          msgid : msg_id,
          userid : user_id
         
        }
    };
    return admin.messaging().sendToDevice(token_id,payload).then(response=>{
      console.log("This was notification feature")
    });
  });

  });
  

 

//});
  
});
const func = require('firebase-functions');

exports.sNotification=func.database.ref('/notification/{user_id}/{msg_id}').onWrite(event => {
  const user_id = event.params.user_id;
  const msg_id=event.params.msg_id;
  console.log('You have a like for : ', user_id);


  if(!event.data.val()){

    return console.log('A Notification has been deleted from the database : ', msg_id);

  }

  

  const fromUser = admin.database().ref(`/notification/${user_id}/${msg_id}`).once('value');
  return fromUser.then(fromUserResult => {

    const from_user_id = fromUserResult.val().from;
    const from_type=fromUserResult.val().type;
    

    console.log('You have new notification from  : ', from_user_id);

    const userQuery = admin.database().ref(`user/${from_user_id}/name`).once('value');
    const msg_val=admin.database().ref(`messages/${msg_id}/title`).once('value');
    //return msg_val.then(msgResult =>{
   // const msg_title=msgResult.val();
   // console.log('msg title is',msg_title);
  

    const deviceToken = admin.database().ref(`/FCMToken/${user_id}`).once('value');
     return Promise.all([userQuery,deviceToken,msg_val]).then(result => {
      const userName = result[0].val();
      const token_id = result[1].val();
      const msgTitle=result[2].val();
      const payload = {
       
        data : {
        title : from_type,
          body : msgTitle,
          userName : userName,
          msgid : msg_id,
          userid : user_id
         

        }
      };
      return admin.messaging().sendToDevice(token_id, payload).then(response => {

        console.log('This was the notification Feature');

      });

  });
});
  

  });



