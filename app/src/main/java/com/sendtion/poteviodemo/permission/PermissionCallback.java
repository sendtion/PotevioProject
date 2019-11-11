package com.sendtion.poteviodemo.permission;

import java.io.Serializable;

public interface PermissionCallback extends Serializable {
    void onPermissionGranted();
}
