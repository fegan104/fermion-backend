package com.fermion.util;

import com.amazonaws.services.lambda.runtime.Context;

/**
 * Created by @author frankegan on 11/13/18.
 */
public class Logger {
    private static Context context;

    private Logger(Context context) {
        Logger.context = context;
    }

    public static Logger init(Context context) {
        return new Logger(context);
    }

    public static void log(String error) {
        System.out.println(error);
        if (context == null) return;
        context.getLogger().log(error);
    }
}
