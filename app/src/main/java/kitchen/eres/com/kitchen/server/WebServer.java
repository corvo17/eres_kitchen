package kitchen.eres.com.kitchen.server;


import android.util.Log;

import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
import kitchen.eres.com.kitchen.App;

public class WebServer extends NanoHTTPD {

    public WebServer(int port) {
        super(port);
        init();
    }

    public void init() {
        Log.d("TAG_R", "init: ");

    }

    public WebServer(String hostname, int port) {
        super(hostname, port);
        init();
    }

    @Override
    public Response serve(IHTTPSession session) {
        Map<String, String> files = new HashMap<String, String>();
        try {
            session.parseBody(files);
        } catch (IOException ioe) {
            return newFixedLengthResponse(Response.Status.OK,"application/json","bad request");


        } catch (ResponseException re) {
            return newFixedLengthResponse(Response.Status.OK,"application/json","bad request");

        }
        if (files.isEmpty())
            return newFixedLengthResponse(Response.Status.OK,"application/json","bad request");

        String bodyJson = "";// session.getQueryParameterString();
        for (Map.Entry<String, String> entry : files.entrySet()) {
            bodyJson = entry.getValue();
            break;
        }
        if (bodyJson == "")
            return newFixedLengthResponse(Response.Status.OK,"application/json","bad request");
        try {

            //localM = App.gson.fromJson(bodyJson, new TypeToken<ObservableCollection<ERESNotification>>(){}.getType());
            Log.d("TAG_R", "serve: "+bodyJson.toString());

            App app =App.getApp();
            app.loadLocalServer(App.gson.fromJson(bodyJson, Notes.class).getNotes());

        } catch (JsonSyntaxException ee) {
            return newFixedLengthResponse(Response.Status.OK,"application/json","bad request");
        }

        return newFixedLengthResponse(new Response.IStatus() {
            @Override
            public String getDescription() {
                return "200";
            }

            @Override
            public int getRequestStatus() {
                return 200;
            }
        }, "application/json", "{ret:200}");
    }

//    public static void removeNote(ERESNotification note) {
//        Messages.remove(note);
//    }
}