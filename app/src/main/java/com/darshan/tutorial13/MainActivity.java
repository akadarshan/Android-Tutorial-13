package com.darshan.tutorial13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    TextInputEditText tv_call, tv_sms;
    TextInputLayout tl_call, tl_sms;

    protected static final int REQUESTCODE_CALL = 1;
    protected static final int REQUESTCODE_SMS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_call = findViewById(R.id.tv_call);
        tl_call = findViewById(R.id.tl_call);
        tv_sms = findViewById(R.id.tv_sms);
        tl_sms = findViewById(R.id.tl_sms);
    }

    public void btnCall(View view) {
        if (isCallPermissionAllowed()) {
            if (tv_call.getText().toString().equals("")) {
                tl_call.setError("Number required");
                return;
            } else {
                tl_call.setErrorEnabled(false);
                call();
            }
        }
    }

    public void btnSMS(View view) {
        if (isSMSPermissionAllowed()) {
            if (tv_sms.getText().toString().equals("") || tv_call.getText().toString().equals("")) {
                tl_sms.setError("Text required");
                tl_call.setError("Number required");
                return;
            } else {
                tl_sms.setErrorEnabled(false);
                tl_call.setErrorEnabled(false);
                sms();
            }
        }
    }

    private boolean isSMSPermissionAllowed() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUESTCODE_SMS);
                return false;
            }
        } else {
            return true;
        }
    }

    private boolean isCallPermissionAllowed() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUESTCODE_CALL);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 11:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            case 21:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sms();
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void call() {
        try {
            String number = tv_call.getText().toString();
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + number));
            startActivity(intent);
        } catch (Exception objExc) {
            Toast.makeText(this, objExc.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void sms() {
        try {
            String number = tv_call.getText().toString();
            String Message = tv_sms.getText().toString();
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, Message, null, null);
            Toast.makeText(this, "SMS Send Successfully", Toast.LENGTH_SHORT).show();

        } catch (Exception objExc) {
            Toast.makeText(this, objExc.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }
}