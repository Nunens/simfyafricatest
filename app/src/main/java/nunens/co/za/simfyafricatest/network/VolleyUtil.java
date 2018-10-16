package nunens.co.za.simfyafricatest.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import menolla.co.za.itsi_test.utils.Globals;
import nunens.co.za.simfyafricatest.listener.VolleyListener;

/**
 * Created by SiphoKobue on 2018/10/09.
 */

public class VolleyUtil {
    static VolleyListener listener;

    public static void firstRequest(Context ctx, final String number, VolleyListener volleyListener) {
        listener = volleyListener;
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        StringRequest request = new StringRequest(Request.Method.GET, Globals.Companion.getFIRST_URL() + number, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error);
            }
        });
        requestQueue.add(request);
    }

    public static void thirdRequest(Context ctx, VolleyListener volleyListener) {
        listener = volleyListener;
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        StringRequest request = new StringRequest(Request.Method.GET, Globals.Companion.getTHIRD_URL(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error);
            }
        });
        requestQueue.add(request);
    }
}
