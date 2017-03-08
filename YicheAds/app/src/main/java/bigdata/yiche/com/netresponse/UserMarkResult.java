package bigdata.yiche.com.netresponse;

import android.widget.BaseAdapter;

import java.util.List;

import bigdata.yiche.com.bean.TagBean;

/**
 * Created by yiche on 16/10/10.
 */
public class UserMarkResult extends BaseResult {
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDevice_ids() {
        return device_ids;
    }

    public void setDevice_ids(String device_ids) {
        this.device_ids = device_ids;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public List<TagBean> getTags() {
        return tags;
    }

    public void setTags(List<TagBean> tags) {
        this.tags = tags;
    }

    private String city;
    private String device_ids;
    private String update_time;
    private List<TagBean> tags;


}
