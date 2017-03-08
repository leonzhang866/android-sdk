package bigdata.yiche.com.json;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import bigdata.yiche.com.bean.TagBean;
import bigdata.yiche.com.bean.TopicBean;
import bigdata.yiche.com.constants.ErrorCode;
import bigdata.yiche.com.constants.JsonConstants;
import bigdata.yiche.com.netresponse.BaseResult;
import bigdata.yiche.com.netresponse.RecommendResult;
import bigdata.yiche.com.netresponse.UserMarkResult;

/**
 * Created by wanglirong on 16/10/10.
 */
public class JsonParser {

    public static UserMarkResult parserUserMark(String string){
        UserMarkResult userMarkResult=new UserMarkResult();
        try{
            JSONObject jsonObject=new JSONObject(string);
            int status=jsonObject.optInt(JsonConstants.JSON_STATUS);
            String message=jsonObject.optString(JsonConstants.JSON_MESSAGE);
            userMarkResult.setStatus(status);
            userMarkResult.setMessage(message);
            if (status!=ErrorCode.SUCCESS){
                return userMarkResult;
            }

            userMarkResult.setCity(jsonObject.optString(JsonConstants.JSON_1_CITY));
            userMarkResult.setDevice_ids(jsonObject.optString(JsonConstants.JSON_1_DEVICE_IDS));
            userMarkResult.setUpdate_time(jsonObject.optString(JsonConstants.JSON_1_UPDATE_TIME));

            JSONArray jsonArray= jsonObject.optJSONArray(JsonConstants.JSON_1_TAGS);
            List<TagBean> tagBeanList=new ArrayList<>();
            for(int i=0;i<jsonArray.length();i++){
                TagBean tagBean=new TagBean();
                JSONObject jo=(JSONObject)jsonArray.get(i);
                tagBean.setId(jo.optString(JsonConstants.JSON_1_ID));
                tagBean.setName(jo.optString(JsonConstants.JSON_1_NAME));
                tagBean.setValue(jo.optInt(JsonConstants.JSON_1_VALUE));
                tagBeanList.add(tagBean);
            }
            userMarkResult.setTags(tagBeanList);

        }catch (Exception ignored){

        }
        return userMarkResult;

    }
    public static BaseResult parserDelete(String string){
        BaseResult baseResult=new BaseResult();
        try{
            JSONObject jsonObject=new JSONObject(string);
            int status=jsonObject.optInt(JsonConstants.JSON_STATUS);
            String message=jsonObject.optString(JsonConstants.JSON_MESSAGE);
            baseResult.setStatus(status);
            baseResult.setMessage(message);
            if (status!=ErrorCode.SUCCESS){
                return baseResult;
            }

        }catch (Exception ignored){

        }
        return baseResult;

    }


    public static RecommendResult parserRecommend(String response) {
        RecommendResult recommendResult=new RecommendResult();
        try{
            JSONObject jsonObject=new JSONObject(response);
            int status=jsonObject.optInt(JsonConstants.JSON_STATUS);
            String message=jsonObject.optString(JsonConstants.JSON_MESSAGE);
            recommendResult.setStatus(status);
            recommendResult.setMessage(message);
            if (status!=ErrorCode.SUCCESS){
                return recommendResult;
            }
            JSONArray jsArr=jsonObject.optJSONArray(JsonConstants.JSON_3_NEWS);
            List<TopicBean> list=new ArrayList<>();
            for(int i=0;i<jsArr.length();i++){
                TopicBean topicBean=new TopicBean();
                JSONObject jo=(JSONObject) jsArr.get(i);
                topicBean.setNews_id(jo.optString(JsonConstants.JSON_3_NEWS_ID));
                topicBean.setPic_url(jo.optString(JsonConstants.JSON_3_PIC_URL).split(","));
                topicBean.setComments(new Random().nextInt(100));
                topicBean.setSource(jo.optString(JsonConstants.JSON_3_SOURCE));
                topicBean.setTitle(jo.optString(JsonConstants.JSON_3_TITLE));
                topicBean.setType(jo.optString(JsonConstants.JSON_3_TYPE));
                if(jo.optString(JsonConstants.JSON_3_TYPE).equals("0")){
                    topicBean.setType("0");
                    topicBean.setSubType(0);
                }else if(jo.optString(JsonConstants.JSON_3_TYPE).equals("1")){
                    topicBean.setType(0+"");
                    topicBean.setSubType(1);
                }else if(jo.optString(JsonConstants.JSON_3_TYPE).equals("2")){
                    topicBean.setType(0+"");
                    topicBean.setSubType(2);
                }else{
                    topicBean.setType(jo.optString(JsonConstants.JSON_3_TYPE));
                }
                topicBean.setUrl(jo.optString(JsonConstants.JSON_3_URL));
                list.add(topicBean);
            }
            recommendResult.setList(list);
        }catch (Exception ignored){

        }
        return recommendResult;

    }


}
