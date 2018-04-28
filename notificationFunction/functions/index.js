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
   const tokennum= Object.keys(tokensSnapshot.val()).map(e => tokensSnapshot.val()[e]);
   //var values = Object.keys(o).map(e => obj[e])
   //const tokennum='fG5lxw4UCAc:APA91bEHhrOBlmcM7lnHcOdoGAw6ugWCiA534uF4xy3Dwy3R0J74KCP33CQmdtwtH6Pf1YJXfe2ZHG6SRmoC0EUSf-ru9RiJvNCvc5zk2LUhD-MtSW2v_VSsoYo_oubp2CwenM81Dh0Q';
    const tokennums=['eI5ElhIIs9k:APA91bHFnJCYl4fcEQgTP1Y7-vQe0ztgaxL4NivnWy0EA1sfEo64B0KbaXvxRFD9PO8xGGAc1ECOq21pbJviovsAHWDsFRRmDMkgje6_GFmLHAbsxJv5rPGIIFmqu_iV-ezQz5A9qSdQ','fG5lxw4UCAc:APA91bEHhrOBlmcM7lnHcOdoGAw6ugWCiA534uF4xy3Dwy3R0J74KCP33CQmdtwtH6Pf1YJXfe2ZHG6SRmoC0EUSf-ru9RiJvNCvc5zk2LUhD-MtSW2v_VSsoYo_oubp2CwenM81Dh0Q','cm0dmwrUGNM:APA91bGw-eLI_ISiFekZEtcjcFQfcMocBo1pP5wT1uKyC3X4Ue3dTe6k1mhGrSgrF3ts3xZdn6zhGYxDWTcZNXVlRNt-BSS9yQ80iqCY-H_AD91u1b88OjlQMDCPDtERbZIWFD7Sc_c7','ePTV3pnyG50:APA91bFbC9oIdzpPEHDQSIWStghKC65GnV-mw_HlqDqnpP57yAUCPr-1FCR3aTMUs7q-ZuTM-yW38375_TBJ9Gi4tK1cYcQqrkInp_kfcf46nDEeUul1XGM3AfqfKbEwvyujVhvqaAMY','d8PjTc-3k_s:APA91bFLtbNOQOjQ-UNIqbq8x851RlqQ9vAJjV607Y6-RWz9F35Pkc-OtXMhESIKYFVs5wUqjgsMXjemdO6RCAgqQTZAZl_BGDsHOqce2H08ktd46KKK3cXuuaGyEvCPwbiWL8Fade5a','fm29WCTjPkc:APA91bGKYLPijunDum_YJSs_qoVae9VuiTqu-Hklc0O9aiZIjM3TpX2EMKLf7Fbm_S_n2vO2gbzF4yFjFjVe2Dys2wK_FZjQpiIO3DFNB96KY1n8Plw3XrDcsLrfIfHSi0u-93dYmqeE,diROnczhQ5s:APA91bHaoMBLcc8tVgeAq5ek711F7AMTmAo5t9EnBdVDtocxMeGj77XvjUprm3eEJdVe1Jc_Ynb0Xxa3TXx5m_4q0f_w_sSa9T7gSOnsc-xZzzqSLvYGyQpYNcdUu2a5KSPyRFDDPvfP'];
//tokens
//todo after checking change tokennums to token num
     return admin.messaging().sendToDevice(tokennums, payload).then(response => {
      // For each message check if there was an error.
      const tokensToRemove = [];
      response.results.forEach((result, index) => {
        const error = result.error;
        if (error) {
          console.error('Failure sending notification to', tokennums[index], error);
          // Cleanup the tokens who are not registered anymore.
          
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
   const tokennum= Object.keys(tokensSnapshot.val()).map(e => tokensSnapshot.val()[e]);
   const tokennums=['eI5ElhIIs9k:APA91bHFnJCYl4fcEQgTP1Y7-vQe0ztgaxL4NivnWy0EA1sfEo64B0KbaXvxRFD9PO8xGGAc1ECOq21pbJviovsAHWDsFRRmDMkgje6_GFmLHAbsxJv5rPGIIFmqu_iV-ezQz5A9qSdQ','fG5lxw4UCAc:APA91bEHhrOBlmcM7lnHcOdoGAw6ugWCiA534uF4xy3Dwy3R0J74KCP33CQmdtwtH6Pf1YJXfe2ZHG6SRmoC0EUSf-ru9RiJvNCvc5zk2LUhD-MtSW2v_VSsoYo_oubp2CwenM81Dh0Q','cm0dmwrUGNM:APA91bGw-eLI_ISiFekZEtcjcFQfcMocBo1pP5wT1uKyC3X4Ue3dTe6k1mhGrSgrF3ts3xZdn6zhGYxDWTcZNXVlRNt-BSS9yQ80iqCY-H_AD91u1b88OjlQMDCPDtERbZIWFD7Sc_c7','ePTV3pnyG50:APA91bFbC9oIdzpPEHDQSIWStghKC65GnV-mw_HlqDqnpP57yAUCPr-1FCR3aTMUs7q-ZuTM-yW38375_TBJ9Gi4tK1cYcQqrkInp_kfcf46nDEeUul1XGM3AfqfKbEwvyujVhvqaAMY','d8PjTc-3k_s:APA91bFLtbNOQOjQ-UNIqbq8x851RlqQ9vAJjV607Y6-RWz9F35Pkc-OtXMhESIKYFVs5wUqjgsMXjemdO6RCAgqQTZAZl_BGDsHOqce2H08ktd46KKK3cXuuaGyEvCPwbiWL8Fade5a','fm29WCTjPkc:APA91bGKYLPijunDum_YJSs_qoVae9VuiTqu-Hklc0O9aiZIjM3TpX2EMKLf7Fbm_S_n2vO2gbzF4yFjFjVe2Dys2wK_FZjQpiIO3DFNB96KY1n8Plw3XrDcsLrfIfHSi0u-93dYmqeE,diROnczhQ5s:APA91bHaoMBLcc8tVgeAq5ek711F7AMTmAo5t9EnBdVDtocxMeGj77XvjUprm3eEJdVe1Jc_Ynb0Xxa3TXx5m_4q0f_w_sSa9T7gSOnsc-xZzzqSLvYGyQpYNcdUu2a5KSPyRFDDPvfP'];
   
 // const tokennum='fG5lxw4UCAc:APA91bEHhrOBlmcM7lnHcOdoGAw6ugWCiA534uF4xy3Dwy3R0J74KCP33CQmdtwtH6Pf1YJXfe2ZHG6SRmoC0EUSf-ru9RiJvNCvc5zk2LUhD-MtSW2v_VSsoYo_oubp2CwenM81Dh0Q';
    
//tokens
     return admin.messaging().sendToDevice(tokennums, payload).then(response => {
      // For each message check if there was an error.
      const tokensToRemove = [];
      response.results.forEach((result, index) => {
        const error = result.error;
        if (error) {
          console.error('Failure sending notification to', tokennums[index], error);
          // Cleanup the tokens who are not registered anymore.
          
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

        console.log('This was the notification Feature');

      });

  });
});
});