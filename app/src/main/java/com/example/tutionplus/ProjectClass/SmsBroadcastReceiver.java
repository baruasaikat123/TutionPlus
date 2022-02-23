package com.example.tutionplus.ProjectClass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class SmsBroadcastReceiver extends BroadcastReceiver {

    public SmsBroadcastReceiverListener smsBroadcastReceiverListener;

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction() == SmsRetriever.SMS_RETRIEVED_ACTION){

            Bundle bundle = intent.getExtras();

            if(bundle!=null){

                Status status = (Status) bundle.get(SmsRetriever.EXTRA_STATUS);

                switch (status.getStatusCode()){

                    case CommonStatusCodes.SUCCESS:
                        Intent message = bundle.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                        smsBroadcastReceiverListener.onSuccess(message);
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        smsBroadcastReceiverListener.onFailure();
                        break;

                }
            }

        }
    }

    public interface SmsBroadcastReceiverListener {

        void onSuccess(Intent intent);

        void onFailure();
    }

}









/*public class SmsBroadcastReceiver extends BroadcastReceiver {

   private SmsBroadcastReceiverLister smsBroadcastReceiverLister;

   public void initListener(SmsBroadcastReceiverLister smsBroadcastReceiverLister){
       this.smsBroadcastReceiverLister = smsBroadcastReceiverLister;
   }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction() == SmsRetriever.SMS_RETRIEVED_ACTION){

            Bundle bundle = intent.getExtras();

            if(bundle != null){
                Status smsRetrieverStatus = (Status) bundle.get(SmsRetriever.EXTRA_STATUS);

                switch (smsRetrieverStatus.getStatusCode()){
                    case CommonStatusCodes
                            .SUCCESS:
                        String message = (String) bundle.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                        if(message!=null){
                            Pattern pattern = Pattern.compile("\\d{6}");
                            Matcher matcher = pattern.matcher(message);

                            if(matcher.find()){
                                String get_otp = matcher.group(0);
                                if(this.smsBroadcastReceiverLister !=null){
                                    this.smsBroadcastReceiverLister.onSuccess(get_otp);
                                }
                            }
                        }
                        break;

                    case CommonStatusCodes.TIMEOUT:
                        this.smsBroadcastReceiverLister.onFailure();
                        break;
                }

            }

        }
    }

    public interface SmsBroadcastReceiverLister{

        void onSuccess(String otp);

        void onFailure();
    }
}
*/