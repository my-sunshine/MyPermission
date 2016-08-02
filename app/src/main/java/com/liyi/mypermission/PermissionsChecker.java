package com.liyi.mypermission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;


public class PermissionsChecker {

    private Activity context;

    public PermissionsChecker(Activity context) {
        this.context = context;
    }

    /**
     * 是否缺少某个权限
     */
    public boolean lacksPermissions(String... permissions) {
        if (permissions.length < 1) {
            return false;
        }
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    private boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED;
    }


    /**
     * 是否授予权限
     */
    public boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否存在被拒绝的权限
     * 在权限被回收，activity被重启之后，避免多次请求权限
     */
 /*   public boolean shouldShowRequestPermissionRationale(String... permissions) {
        if (permissions.length < 1) {
            return false;
        }

        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
                return true;
            }
        }
        return false;
    }

    public boolean shouldNotShowRequestPermissionRationale(String... permissions) {
        if (permissions.length < 1) {
            return false;
        }

        for (String permission : permissions) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
                return true;
            }
        }
        return false;
    }

    public String showMessages(String... permissions){
        String message="";
        if (permissions.length < 1) {
            return "";
        }
        for(String permission : permissions){
            if(!ActivityCompat.shouldShowRequestPermissionRationale(context,permission)){
                message=message+permission+",";
            }
        }

        return message;
    }*/

}
