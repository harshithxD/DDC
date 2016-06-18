package com.example.harshith.ddc;

import android.app.Application;
import android.os.Handler;

/**
 * Created by harshith on 18/6/16.
 */

public class GlobalClass extends Application {
    Handler processHandler;

    public void setProcessHandler(Handler processHandler) {
        this.processHandler = processHandler;
    }

    public Handler getProcessHandler() {
        return processHandler;
    }
}
