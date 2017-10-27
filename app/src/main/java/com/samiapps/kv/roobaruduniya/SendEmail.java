package com.samiapps.kv.roobaruduniya;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by KV on 23/8/17.
 */

public class SendEmail extends AsyncTask<RoobaruDuniya, Void, Void> {
    public SendEmail() {
    }

    @Override
    protected Void doInBackground(RoobaruDuniya... params) {
        RoobaruDuniya roobaruDuniya = params[0];
        String title = roobaruDuniya.getTitle();
        String uName = roobaruDuniya.getUser();
        String uId = roobaruDuniya.getuserId();
        String content = roobaruDuniya.getContent() + "\n" + " by" + uName + "\n" + " uid " + uId;



           /* String email = "roobaru.duniya@gmail.com";
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
            intent.putExtra(Intent.EXTRA_TITLE,title);
            intent.putExtra(Intent.EXTRA_TEXT,content);
            //  intent.putExtra(Intent.EXTRA_EMAIL,"roobaru.duniya@gmail.com");
            // intent.setType("text/html");

            // intent.putExtra(Intent.EXTRA_EMAIL,"roobaru.duniya@gmail.com");
            //  intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            //intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");

            startActivity(Intent.createChooser(intent, "Send Email"));
            */
        try {


            GMailSender sender = new GMailSender("roobaru.data@gmail.com", "roobaruduniya");
            // sender.sendMail("Subject",
            //  "Body",
            //"roobaru.data@gmail.com",
            //"roobaru.duniya@gmail.com");
            sender.sendMail(title,
                    content,
                    "roobaru.data@gmail.com",
                    "roobaru.duniya@gmail.com");
         //   Log.d("checkemail", "Email sent");

        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }
        return null;
    }
}

