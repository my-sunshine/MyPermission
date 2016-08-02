package com.liyi.mypermission;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private PermissionsChecker permissionsChecker;
    private boolean flag;
    private String[] permissions=new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE

    };

    private static final int PERMISSION_REQUEST_CODE = 1;

    private static final int SEETING_PERMISSION_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionsChecker=new PermissionsChecker(this);
        if(savedInstanceState==null){
            flag=true;
        }else{
            flag=savedInstanceState.getBoolean("flag");
        }
    }

    //在权限被回收之后，activity会被重启
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean("flag", flag);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(permissionsChecker.lacksPermissions(permissions)){
            requestMainPermissions();
        }
    }

    private void requestMainPermissions() {
        if(flag){
            flag=false;
            ActivityCompat.requestPermissions(MainActivity.this, permissions, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if(requestCode==PERMISSION_REQUEST_CODE && permissionsChecker.verifyPermissions(grantResults)){
        }else {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    if (keyEvent.getAction() == KeyEvent.KEYCODE_BACK) {
                        finish();
                    }
                    return false;
                }
            });
            builder.setTitle("提示");
            builder.setMessage("缺少必要权限");
            builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, SEETING_PERMISSION_REQUEST_CODE);
                }
            });
            builder.show();

        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SEETING_PERMISSION_REQUEST_CODE){
            if(permissionsChecker.lacksPermissions(permissions)){
                finish();
            }
        }
    }
}
