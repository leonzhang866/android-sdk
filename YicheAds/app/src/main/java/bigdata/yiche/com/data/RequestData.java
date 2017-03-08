package bigdata.yiche.com.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import bigdata.yiche.com.utils.Util;

/**
 * Created by yiche on 16/10/11.
 */

public class RequestData {
    public static String  createDeleteData(String token ,String deviceId,String op_type){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("token",token);
            jsonObject.put("device_id",deviceId);
            jsonObject.put("op_type",op_type);
            jsonObject.put("tag_ids",new JSONArray());
            jsonObject.put("tag_type", "new");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
}
