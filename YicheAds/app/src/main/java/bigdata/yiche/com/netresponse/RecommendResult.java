package bigdata.yiche.com.netresponse;

import java.util.List;

import bigdata.yiche.com.bean.TopicBean;

/**
 * Created by yiche on 16/10/11.
 */

public class RecommendResult extends BaseResult {
    private List<TopicBean> list ;

    public List<TopicBean> getList() {
        return list;
    }

    public void setList(List<TopicBean> list) {
        this.list = list;
    }
}
