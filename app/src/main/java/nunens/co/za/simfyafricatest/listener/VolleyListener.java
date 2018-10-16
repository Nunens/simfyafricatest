package nunens.co.za.simfyafricatest.listener;

import com.android.volley.VolleyError;

/**
 * Created by SiphoKobue on 2018/10/11.
 */

public interface VolleyListener {
    void onResponse(String resp);

    void onError(VolleyError volleyError);
}
