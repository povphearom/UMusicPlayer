package com.phearom.um.config;

/**
 * Created by phearom on 7/18/16.
 */
public interface Config {
    String SEPARATE_AT = "@";
    String SERVICE = "musicservice/web_server/";

    class AUTH {
        public static String SERVER_IP = "";

        public static String getPass() {
            return "Lika" + SEPARATE_AT + "2222";
        }
    }
}
