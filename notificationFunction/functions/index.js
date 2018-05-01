'use strict'
const functions = require('firebase-functions');
const admin = require('firebase-admin');
//Object.values = require('object.values');
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
      const payload={

        data : {
          title:"New Article",
          body: msg_title,
          msgid : msg_id,
          userid : user_id

        }
    };
   



 const getDeviceTokensPromise = admin.database().ref('/FCMToken').once('value');
 return Promise.all([getDeviceTokensPromise, msg_title]).then(results => {


   const tokensSnapshot = results[0];
    const msgi = results[1];
 
  if (!tokensSnapshot.hasChildren()) {
      return console.log('There are no notification tokens to send to.');
    }
    console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');
    console.log("tokenslist",tokensSnapshot.val());
    
   const tokens= Object.keys(tokensSnapshot.val()).map(e => tokensSnapshot.val()[e]);
   //var values = Object.keys(o).map(e => obj[e])]
   const keyLists = Object.keys(tokensSnapshot.val());
   console.log("keyList",keyLists[0]);

    

     return admin.messaging().sendToDevice(tokens, payload).then(response => {
      // For each message check if there was an error.
      const tokensToRemove = [];
      response.results.forEach((result, index) => {
        const error = result.error;
        if (error) {
          console.error('Failure sending notification to', tokens[index], error);
            if (error.code === 'messaging/invalid-registration-token' || error.code === 'messaging/registration-token-not-registered') {
              const kk=tokensSnapshot.ref.child(keyLists[index]);
              console.log("deleted is",kk);
              tokensToRemove.push(tokensSnapshot.ref.child(keyLists[index]).remove());
         // tokensToRemove.push(tokensSnapshot.ref.child((tokensSnapshot.child(tokens[index]).key).remove()));
        
        }
          // Cleanup the tokens who are not registered anymore.
          
        }
        else
        {
          console.log("Successful sent to",tokens[index]);
        }
      });
      return Promise.all(tokensToRemove);
    });
  
});
});
});
const functio=require('firebase-functions')
exports.audioNotification = functio.database.ref('/audioPublished/{msg_id}').onWrite(event => {
  const snapshot = event.data;
  // Only send a notification when a new message has been created.
  if (snapshot.previous.val()) {
    return;
  }
  const msg_id = event.params.msg_id;

  const msg_val=admin.database().ref(`audiodb/${msg_id}`).once('value');
  return msg_val.then(msgResult =>{
    const msg_title=msgResult.val().title;
    const user_id=msgResult.val().teller;
    console.log('msg title is',msg_title);
     console.log('We have a new audio : ', msg_id);
      const payload={

        data : {
          title:"New Audio",
          body: msg_title,
          msgid : msg_id,
          userteller : user_id

        }
    };
   



 const getDeviceTokensPromise = admin.database().ref('/FCMToken').once('value');
 return Promise.all([getDeviceTokensPromise, msg_title]).then(results => {


   const tokensSnapshot = results[0];
    const msgi = results[1];
 
  if (!tokensSnapshot.hasChildren()) {
      return console.log('There are no notification tokens to send to.');
    }
    console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');
    console.log("tokenslist",tokensSnapshot.val());
    
   const tokens= Object.keys(tokensSnapshot.val()).map(e => tokensSnapshot.val()[e]);
   //var values = Object.keys(o).map(e => obj[e])]
   const keyLists = Object.keys(tokensSnapshot.val());
   console.log("keyList",keyLists[0]);

    

     return admin.messaging().sendToDevice(tokens, payload).then(response => {
      // For each message check if there was an error.
      const tokensToRemove = [];
      response.results.forEach((result, index) => {
        const error = result.error;
        if (error) {
          console.error('Failure sending notification to', tokens[index], error);
            if (error.code === 'messaging/invalid-registration-token' || error.code === 'messaging/registration-token-not-registered') {
              const kk=tokensSnapshot.ref.child(keyLists[index]);
              console.log("deleted is",kk);
              tokensToRemove.push(tokensSnapshot.ref.child(keyLists[index]).remove());
         // tokensToRemove.push(tokensSnapshot.ref.child((tokensSnapshot.child(tokens[index]).key).remove()));
        
        }
          // Cleanup the tokens who are not registered anymore.
          
        }
        else
        {
          console.log("Successful sent to",tokens[index]);
        }
      });
      return Promise.all(tokensToRemove);
    });
  
});
});
});


const functi=require('firebase-functions')
exports.photoNotification = functi.database.ref('/photoPublishedDb/{msg_id}').onWrite(event => {

  const snapshot = event.data;
  // Only send a notification when a new message has been created.
  if (snapshot.previous.val()) {
    return;
  }
  const msg_id = event.params.msg_id;

  const msg_val=admin.database().ref(`photoDb/${msg_id}`).once('value');
  return msg_val.then(msgResult =>{
    const msg_title=msgResult.val().title;
    const user_id=msgResult.val().user;
    console.log('msg title is',msg_title);
     console.log('We have a new photo : ', msg_id);
      const payload={

        data : {
          title:"New Photo",
          body: msg_title,
          msgid : msg_id,
          photoid : user_id

        }
    };
   



  const getDeviceTokensPromise = admin.database().ref('/FCMToken').once('value');
 return Promise.all([getDeviceTokensPromise, msg_title]).then(results => {


   const tokensSnapshot = results[0];
    const msgi = results[1];
 
  if (!tokensSnapshot.hasChildren()) {
      return console.log('There are no notification tokens to send to.');
    }
    console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');
    console.log("tokenslist",tokensSnapshot.val());
    
   const tokens= Object.keys(tokensSnapshot.val()).map(e => tokensSnapshot.val()[e]);
   //var values = Object.keys(o).map(e => obj[e])]
   const keyLists = Object.keys(tokensSnapshot.val());
   console.log("keyList",keyLists[0]);

    

     return admin.messaging().sendToDevice(tokens, payload).then(response => {
      // For each message check if there was an error.
      const tokensToRemove = [];
      response.results.forEach((result, index) => {
        const error = result.error;
        if (error) {
          console.error('Failure sending notification to', tokens[index], error);
            if (error.code === 'messaging/invalid-registration-token' || error.code === 'messaging/registration-token-not-registered') {
              const kk=tokensSnapshot.ref.child(keyLists[index]);
              console.log("deleted is",kk);
              tokensToRemove.push(tokensSnapshot.ref.child(keyLists[index]).remove());
         // tokensToRemove.push(tokensSnapshot.ref.child((tokensSnapshot.child(tokens[index]).key).remove()));
        
        }
          // Cleanup the tokens who are not registered anymore.
          
        }
        else
        {
          console.log("Successful sent to",tokens[index]);
        }
      });
      return Promise.all(tokensToRemove);
    });
  
});
});
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

       
       const error=response.error;
       
       if(error)
       {

       console.log("err",error);
     }


        console.log('This was the notification Feature');

      });

  });
});
});