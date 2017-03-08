package bigdata.yiche.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.yiche.bigdata.ad.ISDKCallBack;
import com.yiche.bigdata.ad.YCAdPlatform;
import com.yiche.bigdata.ad.bean.AdBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import bigdata.yiche.com.R;
import bigdata.yiche.com.adapter.RecommendAdapter;
import bigdata.yiche.com.bean.TopicBean;
import bigdata.yiche.com.constants.Constant;
import bigdata.yiche.com.constants.ErrorCode;
import bigdata.yiche.com.json.JsonParser;
import bigdata.yiche.com.net.manager.RequestManager;
import bigdata.yiche.com.netresponse.RecommendResult;
import bigdata.yiche.com.utils.DeviceId;
import bigdata.yiche.com.utils.MyLogger;
import bigdata.yiche.com.utils.Util;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
/**
 * Created by yiche on 16/10/8.
 */
public class Fragment_two extends Fragment {
    private static MyLogger mLogger = MyLogger.getLogger(Fragment_two.class.getName());
    private List<TopicBean> list = new ArrayList<>();
    private PtrClassicFrameLayout mPtrFrame;
    RecommendAdapter recommendAdapter;
    int[] pids;
    int refreshIndex = 0;
    int recommendNum=12;
    private LinearLayout ll_no_net;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View contentView = inflater.inflate(R.layout.fragment_two, null);
        final ListView listView = (ListView) contentView.findViewById(R.id.listView);
        ll_no_net=(LinearLayout) contentView.findViewById(R.id.no_net);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TopicBean topicBean = (TopicBean) recommendAdapter.getItem(position);
                Intent it = new Intent(getActivity(), WebActivity.class);
                it.putExtra("url", topicBean.getUrl());
                if(topicBean.getType().equals("0")||topicBean.getType().equals("3")||topicBean.getType().equals("4")){
                    it.putExtra("title", topicBean.getTitle());
                }else{
                    it.putExtra("title", topicBean.getAd_title());
                }
                if(topicBean.getType().equals("5")||topicBean.getType().equals("6")||topicBean.getType().equals("7")){
                    YCAdPlatform.sendClick(topicBean.getAd_resourceId(),view);

                    mLogger.v(topicBean.getAd_resourceId()+"////resourceId()");
                    mLogger.v("发送广告点击");
                }

                startActivity(it);
            }
        });
        ll_no_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Util.isOpenNetwork(getActivity())){
                    mPtrFrame.setVisibility(View.GONE);
                    ll_no_net.setVisibility(View.VISIBLE);
                    return;
                }else{
                    mPtrFrame.setVisibility(View.VISIBLE);
                    ll_no_net.setVisibility(View.GONE);
                    mPtrFrame.autoRefresh();
                }
            }
        });

        mPtrFrame = (PtrClassicFrameLayout) contentView.findViewById(R.id.rotate_header_list_view_frame);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        recommendAdapter = new RecommendAdapter(getActivity(), list);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //----------测试start---------------

//                if (refreshIndex == 0) {
//                    pids = new int[]{11};
//                } else if (refreshIndex == 1) {
//                    pids = new int[]{12};
//                } else if (refreshIndex == 2) {
//                    pids = new int[]{13};
//                } else {
//                    pids = new int[]{14};
//                }
//
//
//                for(int i=0;i<5;i++){
//                    TopicBean topicBean=new TopicBean();
//                    topicBean.setNews_id("111");
//                    topicBean.setPic_url(new String[]{"http://image.bitautoimg.com/wapimg-180-120/appimage/cms/20160913/w550_h367_f9763741df324a189cc4427d5b80adf3.jpg","http://image.bitautoimg.com/wapimg-172-124/appimage/cms/20160913/w550_h367_f9763741df324a189cc4427d5b80adf3.jpg","http://image.bitautoimg.com/wapimg-172-124/appimage/cms/20160913/w550_h367_f9763741df324a189cc4427d5b80adf3.jpg"});
//                    topicBean.setComments(new Random().nextInt(100));
//                    topicBean.setSource("网易汽车");
//                    topicBean.setTitle("要买SUV的一定要再等等！这些新车不久将上市");
//                    if(i==0){
//                        topicBean.setType(0+"");
//                        topicBean.setSubType(0);
//                    }else if(i==1){
//                        topicBean.setType(0+"");
//                        topicBean.setSubType(1);
//                    }else if(i==2){
//                        topicBean.setType(0+"");
//                        topicBean.setSubType(2);
//                    }else{
//                        topicBean.setType(i+"");
//                    }
//                    topicBean.setUrl("http://h5.ycapp.yiche.com/news/51999.html");
//                    list.add(topicBean);
//                }
//
//                YCAdPlatform.getAd(getActivity(), "b0e143a5-9e4a-6517-b63d-d5590bc25ec7", pids, new ISDKCallBack() {
//                    @Override
//                    public void onResponse(int status, List<AdBean> adBeanList) {
//                        mPtrFrame.refreshComplete();
//                        if (status == 2000 && adBeanList != null) {
//                            if(adBeanList.size()>0&&adBeanList.get(0).getErrorCode()==3000){
//                                TopicBean topicBean=new TopicBean();
//                                topicBean.setAd_url(adBeanList.get(0).getUrl());
//                                topicBean.setUrl(adBeanList.get(0).getUrl());
//                                topicBean.setPic_url(adBeanList.get(0).getPicUrls());
//
//                                topicBean.setAd_title(adBeanList.get(0).getTitle());
//                                topicBean.setAd_resourceId(adBeanList.get(0).getResourceId());
//                                if(adBeanList.get(0).getType()==0){
//                                    topicBean.setType("5");
//                                }else if(adBeanList.get(0).getType()==1){
//                                    topicBean.setType("6");
//                                }else if(adBeanList.get(0).getType()==2){
//                                    topicBean.setType("7");
//                                }else if(adBeanList.get(0).getType()==3){
//                                    topicBean.setType("8");
//                                }
//                                list.add(3,topicBean);
//                            }
//                        }
//
//                        if(refreshIndex==0){
//                            listView.setAdapter(recommendAdapter);
//                        }else{
//                            recommendAdapter.notifyDataSetChanged();
//                        }
//                        refreshIndex++;
//                    }
//
//                });
//                if(refreshIndex<1000){
//                    return;
//                }
                //-----------测试end------------------



                if (refreshIndex == 0) {
                    pids = new int[]{2378};
                    recommendNum=12;
                } else if (refreshIndex == 1) {
                    pids = new int[]{2379};
                    recommendNum=8;
                } else if (refreshIndex == 2) {
                    pids = new int[]{2380};
                    recommendNum=8;
                } else {
                    pids = new int[]{2381};
                    recommendNum=8;
                }

                if(!Util.isOpenNetwork(getActivity())){
                    mPtrFrame.refreshComplete();
                    if(refreshIndex==0){
                        mPtrFrame.setVisibility(View.GONE);
                        ll_no_net.setVisibility(View.VISIBLE);
                    }else{
                        Toast.makeText(getActivity(),"网络异常，请检测网络设置！",Toast.LENGTH_SHORT).show();
                    }
                    return;
                }else{
                    mPtrFrame.setVisibility(View.VISIBLE);
                    ll_no_net.setVisibility(View.GONE);
                }
                Map<String, String> headers=new HashMap<String, String>();
                headers.put("content-type","application/json; charset=utf-8");
                RequestManager.getInstance().post("http://ycapp.pre.ctags.cn/recommend_template/v1/content",getUrl(Util.getToken(DeviceId.getDeviceID(getActivity())), DeviceId.getDeviceID(getActivity()), recommendNum), headers,new RequestManager.RequestListener() {
                    @Override
                    public void onRequest() {

                    }

                    @Override
                    public void onSuccess(String response, Map<String, String> headers, String url, int actionId) {
                        final RecommendResult recommendResult = JsonParser.parserRecommend(response);
                        if (recommendResult.getStatus() == ErrorCode.SUCCESS) {
//                            list.addAll(0, recommendResult.getList());
                        }else{
                            mPtrFrame.refreshComplete();
                            return;
                        }

                        YCAdPlatform.getAd(getActivity(), "12f7281f-6ecb-4f03-865e-e605c25d4f5c", pids, new ISDKCallBack() {
                            @Override
                            public void onResponse(int status, List<AdBean> adBeanList) {
                                mPtrFrame.refreshComplete();
                                if (recommendResult.getStatus() == ErrorCode.SUCCESS&&recommendResult.getList().size()>3) {
                                    list.addAll(0, recommendResult.getList());
                                }
                                if (status == 2000 && adBeanList != null) {
                                    if(adBeanList.size()>0&&adBeanList.get(0).getErrorCode()==3000){
                                        TopicBean topicBean=new TopicBean();
                                        topicBean.setAd_url(adBeanList.get(0).getUrl());
                                        topicBean.setUrl(adBeanList.get(0).getUrl());
                                        topicBean.setPic_url(adBeanList.get(0).getPicUrls());
                                        topicBean.setAd_title(adBeanList.get(0).getTitle());
                                        topicBean.setAd_resourceId(adBeanList.get(0).getResourceId());
                                        if(adBeanList.get(0).getType()==0){
                                            topicBean.setType("5");
                                        }else if(adBeanList.get(0).getType()==1){
                                            topicBean.setType("6");
                                        }else if(adBeanList.get(0).getType()==2){
                                            topicBean.setType("7");
                                        }else if(adBeanList.get(0).getType()==3){
                                            topicBean.setType("8");
                                        }
                                        if(list.size()>3){
                                            list.add(3,topicBean);
                                        }
                                    }
                                }

                                if(refreshIndex==0){
                                    listView.setAdapter(recommendAdapter);
                                }else{
                                    recommendAdapter.notifyDataSetChanged();
                                }
                                refreshIndex++;

                            }

                        });
                    }

                    @Override
                    public void onError(String errorMsg, String url, int actionId) {
                        mPtrFrame.refreshComplete();
                    }
                }, 3);

            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        // the following are default settings
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        // default is false
        mPtrFrame.setPullToRefresh(false);
        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh();
            }
        }, 100);
        return contentView;
    }

    private String getUrl(String token, String deviceid, int num) {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("token",token);
        jsonObject.put("dv_id",deviceid);
        jsonObject.put("acquire_num",num);
        jsonObject.put("pos_id","6c8349cc7260ae62e3b1396831a8398f");
        return jsonObject.toString();
    }
    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

}