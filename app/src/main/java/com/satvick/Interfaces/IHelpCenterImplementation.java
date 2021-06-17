 package com.satvick.Interfaces;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.satvick.R;

public class IHelpCenterImplementation extends AppCompatActivity implements IHelpCenter {

    private Context context;

    @Override
    public void mailUs(Context context,String subject) {
        ShareCompat.IntentBuilder.from((Activity) context)
                .setType("message/rfc822")
                .addEmailTo("customecare@soulahe.com")
                .setSubject(subject.toUpperCase())
                .setText("Hi Team,")
                .setChooserTitle("Send an email")
                .startChooser();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void callus(Context context) {
        if(showPermission(context))
        {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+918929529529"));
            Log.e("context:", String.valueOf(context));
            context.startActivity(intent);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean showPermission(Context context)
    {
        boolean ret=true;

        if(context.checkSelfPermission(Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
        {
            ret=false;
            ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.CALL_PHONE},PackageManager.PERMISSION_GRANTED);

        }

       return ret;
    }


    @Override
    public void showBottomSheet(Context context, final String subject) {
        this.context=context;
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1 =inflater.inflate(R.layout.pop_up_contact_us, null);
        bottomSheetDialog.setContentView(view1);
        bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        RelativeLayout callusRl=view1.findViewById(R.id.callusRl);
        RelativeLayout rlmailus=view1.findViewById(R.id.rlmailus);
        final IHelpCenter center=new IHelpCenterImplementation();


        rlmailus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                center.mailUs(v.getContext(),subject);
            }
        });
        callusRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                center.callus(v.getContext());

            }
        });
        bottomSheetDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0)
        {
            switch (requestCode)
            {
                case 121:
                    boolean phone_call=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if(!phone_call)
                    {
                        showPermission(context);
                    }

                break;
            }


        }

    }
}
