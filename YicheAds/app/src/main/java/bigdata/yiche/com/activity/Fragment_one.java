package bigdata.yiche.com.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import bigdata.yiche.com.R;
import bigdata.yiche.com.adapter.MarkAdapter;
import bigdata.yiche.com.bean.AdBean;
import bigdata.yiche.com.bean.TagBean;
import bigdata.yiche.com.constants.Constant;
import bigdata.yiche.com.constants.ErrorCode;
import bigdata.yiche.com.data.RequestData;
import bigdata.yiche.com.json.JsonParser;
import bigdata.yiche.com.net.manager.RequestManager;
import bigdata.yiche.com.netresponse.BaseResult;
import bigdata.yiche.com.netresponse.UserMarkResult;
import bigdata.yiche.com.utils.DeviceId;
import bigdata.yiche.com.utils.MyLogger;
import bigdata.yiche.com.utils.Util;
import bigdata.yiche.com.view.NoScrollListview;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by yiche on 16/10/8.
 */
public class Fragment_one extends Fragment {
    private static MyLogger mLogger = MyLogger.getLogger(Fragment_one.class.getName());
    private PtrClassicFrameLayout mPtrFrame;
    private ScrollView mScrollView;
    private TextView tv_time, tv_address, tv_money;
    private Button btn_clear;
    private String downloadUrl;
    private NoScrollListview listView;
    private LinearLayout ll_null;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        initUpdate(getActivity());
        final View contentView = inflater.inflate(R.layout.fragment_one, null);
        tv_time = (TextView) contentView.findViewById(R.id.tv_refresh_time);
        tv_address = (TextView) contentView.findViewById(R.id.tv_address);
        tv_money = (TextView) contentView.findViewById(R.id.tv_money);
        btn_clear = (Button) contentView.findViewById(R.id.btn_clear);
        listView = (NoScrollListview) contentView.findViewById(R.id.listView);

        ll_null = (LinearLayout) contentView.findViewById(R.id.ll_null);
        mScrollView = (ScrollView) contentView.findViewById(R.id.rotate_header_scroll_view);

        mPtrFrame = (PtrClassicFrameLayout) contentView.findViewById(R.id.rotate_header_web_view_frame);
        mPtrFrame.setLastUpdateTimeRelateObject(this);

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/json;charset=utf-8");

                RequestManager.getInstance().post(Constant.ServelUrl + Constant.METHOD_CLRER, RequestData.createDeleteData(Util.getToken(DeviceId.getDeviceID(getActivity())), DeviceId.getDeviceID(getActivity()), "0"), header, new RequestManager.RequestListener() {

                    @Override
                    public void onRequest() {
                    }

                    @Override
                    public void onSuccess(String response, Map<String, String> headers, String url, int actionId) {
                        BaseResult baseResult = JsonParser.parserDelete(response);
                        if (baseResult.getStatus() == ErrorCode.SUCCESS) {
                            listView.setVisibility(View.GONE);
                            ll_null.setVisibility(View.VISIBLE);
                            btn_clear.setBackgroundResource(R.drawable.shape_rec_grey);
                            btn_clear.setTextColor(Color.parseColor("#bdbdbd"));
                        }
                    }

                    @Override
                    public void onError(String errorMsg, String url, int actionId) {
                        Toast.makeText(getActivity(), "清除失败，请重试", Toast.LENGTH_SHORT).show();
                    }
                }, 2);

            }
        });

        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mScrollView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if(!Util.isOpenNetwork(getActivity())){
                    mPtrFrame.refreshComplete();
                    alertNetDialog(getActivity());
                    return;
                }

                RequestManager.getInstance().get(Constant.ServelUrl + Constant.METHOD_ACQUIRETAG + getUrl(DeviceId.getDeviceID(getActivity())), new RequestManager.RequestListener() {
                    @Override
                    public void onRequest() {

                    }

                    @Override
                    public void onSuccess(String response, Map<String, String> headers, String url, int actionId) {
                        mPtrFrame.refreshComplete();
                        UserMarkResult userMarkResult = JsonParser.parserUserMark(response);
                        if (userMarkResult.getStatus() == ErrorCode.SUCCESS) {
                            ll_null.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                            tv_time.setText(Util.getTimeFromMillons(Long.parseLong(userMarkResult.getUpdate_time())));
                            tv_address.setText(userMarkResult.getCity());
                            List<TagBean> list = userMarkResult.getTags();
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getId().equals("price")) {
                                    tv_money.setText(list.get(i).getValue() + " 万元");
                                    list.remove(i);
                                }
                            }
                            if (list.size() == 0) {
                                ll_null.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.GONE);
                                btn_clear.setBackgroundResource(R.drawable.shape_rec_grey);
                                btn_clear.setTextColor(Color.parseColor("#bdbdbd"));
                            } else {
                                btn_clear.setBackgroundResource(R.drawable.shape_rec_blue);
                                btn_clear.setTextColor(Color.parseColor("#1e67e0"));
                                Collections.sort(list,new Comparator<TagBean>(){
                                    @Override
                                    public int compare(TagBean b1,TagBean b2) {
                                        return b2.getValue()-b1.getValue();
                                    }
                                });
                                listView.setAdapter(new MarkAdapter(getActivity(), list));
                            }
                        }
                    }

                    @Override
                    public void onError(String errorMsg, String url, int actionId) {
                        mPtrFrame.refreshComplete();
                    }
                }, 1);
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
        mScrollView.requestFocus();
        return contentView;
    }

    private void initUpdate(final Activity context) {
        new Thread() {
            @Override
            public void run() {
                String json = Util.getVersionJson(Constant.UpdataUrl);
                try {
                    JSONObject jo = new JSONObject(json);
                    int version = jo.optInt("versioncode");
                    downloadUrl=jo.optString("url");
                    int v = Util.getAppVersionName(context);
                    if (v != version) {
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                alertUpdateDialog(context);
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void alertUpdateDialog(final Activity context) {
        new AlertDialog.Builder(context).setTitle("更新提示")//设置对话框标题
                .setMessage("有新的版本，请下载更新")//设置显示的内容
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮

                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        dialog.dismiss();
                        download(context);
                    }

                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮

            @Override
            public void onClick(DialogInterface dialog, int which) {//响应事件
                dialog.dismiss();
            }

        }).show();//在按键响应事件中显示此对话框

    }

    private void alertNetDialog(final Activity context) {
        new AlertDialog.Builder(context).setTitle("提示")//设置对话框标题
                .setMessage("网络异常，请检查网络设置！")//设置显示的内容
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        dialog.dismiss();
                    }
                }).show();//在按键响应事件中显示此对话框
    }

    private void download(Activity context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(downloadUrl);
        intent.setData(content_url);
        context.startActivity(intent);
    }

    private String getUrl(String deviceId) {
        return "?token=" + Util.getToken(deviceId) + "&device_id=" + deviceId+"&tag_type=new"+"&is_merage="+0;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScrollView.scrollTo(0,0);
    }
}