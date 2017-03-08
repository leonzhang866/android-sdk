package com.yiche.ycanalytics.net;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.FutureTask;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import android.os.Handler;
import android.os.Message;
import com.yiche.ycanalytics.YCErrorCode;
import com.yiche.ycanalytics.json.JSONHelper;
import com.yiche.ycanalytics.net.NetMessage.NetMessageType;
import com.yiche.ycanalytics.netresponse.BaseResult;
import com.yiche.ycanalytics.utils.Constants;
import com.yiche.ycanalytics.utils.FileHelper;
import com.yiche.ycanalytics.utils.MyLogger;

/**
 * 网络接口
 * 
 * @author wanglirong
 * 
 */
public class ASIHttpRequest implements Runnable
{

    private MyLogger mLogger = MyLogger.getLogger(ASIHttpRequest.class.getName());
    private FutureTask<Object> mTask = new FutureTask<Object>(this, null);
    private static final int BUFFER_SIZE = 1024 * 10; // 10k byte
    // default support resume broken transfer
    private String mUrl;
    private boolean mStop = false;
    private String mDownloadDstFilePath;
    private int mTimeOutSeconds = 60 * 1000;
    private String mAccpetEcoding;
    private boolean mIsDownLoad = false;
    private String mContentType;
    private String mRequestMethod;
    private String mRequestData;
    private Handler mCbkHandler;
    private int mRequestTag;
    private HttpGet getRequest4Download = null;
    // indicate whether support Resume broken transfer
    private boolean mIsSupportBt = true;
    private int mRepeatCount = 0;
    //
    private int mLoginType;
    private int mRequestType;
    public static final int POST=1;
    public static final int GET=2;

    public int getmRequestType() {
		return mRequestType;
	}

	public void setmRequestType(int mRequestType) {
		this.mRequestType = mRequestType;
	}

    public int getmLoginType()
    {
        return mLoginType;
    }

    public void setmLoginType(int mLoginType)
    {
        this.mLoginType = mLoginType;
    }

    public Handler getCbkHandler()
    {
        return this.mCbkHandler;
    }

    public void setCbkHandler(Handler cbkhandler)
    {
        this.mCbkHandler = cbkhandler;

    }

    public int getRequestTag()
    {
        return this.mRequestTag;
    }

    public void setRequestTag(int requestTag)
    {
        this.mRequestTag = requestTag;
    }

    public String getRequestData()
    {
        return mRequestData;
    }

    public void setRequestData(String requestdata)
    {
        this.mRequestData = requestdata;
    }

    public String getRequestMethod()
    {

        return mRequestMethod;
    }

    public void setRequestMethod(String requestMethod)
    {
        this.mRequestMethod = requestMethod;
    }

    public String getUrl()
    {
        return mUrl;
    }

    public void setUrl(String url)
    {
        this.mUrl = url;
    }

    public boolean getStop()
    {
        return mStop;
    }

    public void cancelRequest()
    {
        this.mStop = true;
        if (getRequest4Download != null)
        {
            getRequest4Download.abort();
            getRequest4Download = null;
        }
    }

    public String getDownloadDstFilePath()
    {
        return mDownloadDstFilePath;
    }

    public void setDownloadDstFilePath(String downloadDstFilePath)
    {
        this.mDownloadDstFilePath = downloadDstFilePath;
        this.mIsDownLoad = true;
    }

    public int getTimeOutSeconds()
    {
        return mTimeOutSeconds;
    }

    public void setTimeOutSeconds(int mTimeOutSeconds)
    {
        this.mTimeOutSeconds = mTimeOutSeconds;
    }

    public String getAccpetEcoding()
    {
        return mAccpetEcoding;
    }

    public void setAccpetEcoding(String mAccpetEcoding)
    {
        this.mAccpetEcoding = mAccpetEcoding;
    }

    public boolean isIsDownLoad()
    {
        return mIsDownLoad;
    }

    public void setIsDownLoad(boolean isDownLoad)
    {
        this.mIsDownLoad = isDownLoad;
    }

    public String getContentType()
    {
        return mContentType;
    }

    public void setContentType(String contentType)
    {
        this.mContentType = contentType;
    }

    public void sendCbkMessage(Message msg)
    {

        if (this.mCbkHandler != null)
        {

            if (msg == null)
            {
                Message _msg = new Message();
                _msg.what = 9;

                mCbkHandler.sendMessage(_msg);
            }
            else
            {
                mCbkHandler.sendMessage(msg);
            }
        }
    }

    private OutputStream initOutPutIO() throws IOException
    {
        OutputStream res = null;

        if (this.mIsDownLoad)
        {
            // create the folder
            new File(mDownloadDstFilePath.substring(0, mDownloadDstFilePath.lastIndexOf("/"))).mkdirs();

            // use a temp file and rename after finish download
            if (mIsSupportBt)
            {
                res = new FileOutputStream(mDownloadDstFilePath + ".temp", true);
            }
            else
            {
                res = new FileOutputStream(mDownloadDstFilePath + ".temp");
            }
        }
        else
        {
            res = new ByteArrayOutputStream();
        }
        return res;
    }
    private void handleNetGetRequest()
    {
    	try {
            URL url = new URL(mUrl);
            HttpURLConnection conn = (HttpURLConnection) url
                    .openConnection();
            int responseCode = conn.getResponseCode();
        	if (responseCode == HttpStatus.SC_OK || responseCode == HttpStatus.SC_PARTIAL_CONTENT)
            {
        		InputStream is =conn.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while(-1 != (len = is.read(buffer))){
                    baos.write(buffer,0,len);
                    baos.flush();
                }
                handleSuccessEvent(baos.toByteArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void handleNetRequest()
    {
        HttpClient client = HttpClientHelper.getHttpClient();
        HttpPost postRequest = null;
        HttpGet getRequest = null;
        HttpResponse response = null;
        OutputStream outputio = null;
        InputStream inio = null;
        try
        {
            do
            {
                byte[] bs = null;
                if (mDownloadDstFilePath != null)
                {
                    setIsDownLoad(true);
                    getRequest = HttpClientHelper.getHttpGetRequest(mUrl);
                    getRequest4Download = getRequest;
                    getRequest.setHeader("User-Agent",
                            "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.6) Gecko/20091201 Firefox/3.5.6");
                    if (this.mIsSupportBt)
                    {
                        long tempfileSize = FileHelper.getFileSize(mDownloadDstFilePath + ".temp");
                        getRequest.setHeader("RANGE", "bytes=" + tempfileSize + "-");
                    }
                    try
                    {
                        response = client.execute(getRequest);
                    } catch (Exception e)
                    {
                        // 解决bug：当取消一个下载之后，快速进行同一个下载，经常出现失败的情况
                        if (getRequest4Download != null)
                        {
                            getRequest4Download.abort();
                            getRequest4Download = null;
                            // client.getConnectionManager().shutdown();
                        }
                        handleErrorEvent("receive data length not equals specify length",
                                YCErrorCode.YC_NET_GENER_ERROR);
                        mTask.cancel(true);
                        return;
                        /*
                         * client = HttpClientHelper.getHttpClient(); try { response = client.execute(getRequest); }
                         * catch (Exception ex) { client = HttpClientHelper.getHttpClient(); try{ response =
                         * client.execute(getRequest); } catch(Exception ex1){ handleErrorEvent
                         * ("receive data length not equals specify length", DkErrorCode.DK_NET_GENER_ERROR);
                         * mTask.cancel(true); return; } }
                         */

                    }
                }
                else
                {

                    postRequest = HttpClientHelper.getHttpPostRequest(mUrl);
                    if(mRequestTag==Constants.NET_TAG_INITPARAMS){
//                    	postRequest.setHeader("Host","124.250.36.223:20080");
                    	postRequest.setHeader("User-Agent","Charles/4.0.2");
                    	postRequest.setHeader("Content-Type","application/json");
//                    	postRequest.setHeader("Content-Length","58");
                    }

                    setIsDownLoad(false);
                    StringEntity reqEntity = new StringEntity(mRequestData, "UTF-8");
                    postRequest.setEntity(reqEntity);
                    if (this.mStop)
                    {
                        mTask.cancel(true);
                        handleCancelEvent("cancel before write data to pipe");
                        return;
                    }
                    try
                    {
                        mLogger.d("发起请求");
                        response = client.execute(postRequest);
                        mLogger.d("得到响应");
                    } catch (Exception e)
                    {
                        client = HttpClientHelper.getHttpClient();
                        try
                        {
                            response = client.execute(postRequest);
                        } catch (Exception ex)
                        {
                            client = HttpClientHelper.getHttpClient();
                            try
                            {
                                response = client.execute(postRequest);
                            } catch (Exception ex1)
                            {
                                // handleErrorEvent("receive data length not equals specify length",
                                // DkErrorCode.DK_NET_GENER_ERROR);
                                handleErrorEvent("网络异常，请检查网络状态后重试！", YCErrorCode.YC_NET_GENER_ERROR);
                                mTask.cancel(true);
                                return;
                            }
                        }
                    }
                }
                outputio = this.initOutPutIO();

                int responseCode = response.getStatusLine().getStatusCode();
                if (responseCode == HttpStatus.SC_OK || responseCode == HttpStatus.SC_PARTIAL_CONTENT)
                {
                    int currentReadbyteCount = 0;
                    long responseDataLen = response.getEntity().getContentLength();
                    long havedownDataSize = 0;
                    inio = response.getEntity().getContent();
                    byte[] tempBytes = new byte[BUFFER_SIZE];

                    if (this.mIsDownLoad && this.mIsSupportBt)
                    {
                        havedownDataSize = FileHelper.getFileSize(mDownloadDstFilePath + ".temp");
                        responseDataLen += havedownDataSize;
                    }
                    while ((currentReadbyteCount = inio.read(tempBytes)) != -1)
                    {
                        if (this.mStop)
                        {
                            mTask.cancel(true);
                            break;
                        }
                        havedownDataSize += currentReadbyteCount;
                        // if is download request
                        // then need create the progress event and
                        // handle the event to UI layer
                        if (this.mIsDownLoad)
                        {
                            handleDownLoadingEvent(responseDataLen, havedownDataSize);
                        }
                        outputio.write(tempBytes, 0, currentReadbyteCount);
                    }
                    if (this.mStop)
                    {
                        mTask.cancel(true);
                        handleCancelEvent("cancel after read data from pipe");
                        break;
                    }

                     bs = ((ByteArrayOutputStream) outputio).toByteArray();

                     handleSuccessEvent(bs);

                }
                else if (responseCode == HttpStatus.SC_MOVED_PERMANENTLY
                        || responseCode == HttpStatus.SC_MOVED_TEMPORARILY)
                {
                    String redirectUrl = response.getFirstHeader("location").getValue();
                    if (redirectUrl != null && redirectUrl.length() > 0)
                    {
                        setUrl(redirectUrl);
                        handleNetRequest();
                    }
                }
                else
                {
                    if (responseCode == HttpStatus.SC_GATEWAY_TIMEOUT)
                    {
                        handleErrorEvent("connect time out", YCErrorCode.YC_NET_TIME_OUT);
                    }
                    else if (responseCode == -1 && mRepeatCount == 0)
                    {
                        mRepeatCount = 1;
                        handleNetRequest();
                    }
                    else
                    {
                        String codestr = String.format("Net Error Code: %d", responseCode);
                        String msgstr = String.format("Net Error Msg: %s", response.getStatusLine().getReasonPhrase());

                        MyLogger.getLogger(this.getClass().getName()).v(codestr);
                        MyLogger.getLogger(this.getClass().getName()).v(msgstr);

                        // handleErrorEvent("net error",
                        // DkErrorCode.DK_NET_GENER_ERROR);
                        handleErrorEvent("网络异常，请检查网络状态后重试！", YCErrorCode.YC_NET_GENER_ERROR);
                    }
                }

            } while (false);

            // close the io pipe
            if (inio != null)
            {
                inio.close();
                inio = null;
            }

            if (outputio != null)
            {
                outputio.close();
                outputio = null;
            }

        } catch (Exception e)
        {
            e.printStackTrace();
            MyLogger.getLogger(this.getClass().getName()).v(e.toString());
            handleErrorEvent("exception happen", YCErrorCode.YC_NET_GENER_ERROR);
        } finally
        {

            if (inio != null)
            {
                inio = null;
            }

            if (outputio != null)
            {
                outputio = null;
            }
        }
    }

    private void handleDownLoadingEvent(long totalSize, long currentSize)
    {
        // create custom message instance and
        // set parameter
        NetMessage msg = new NetMessage();
        msg.setMessageType(NetMessage.NetMessageType.NetDownloadling);
        msg.setTotalSize(totalSize);
        msg.setCurentSize(currentSize);
        msg.setRequestId(this.hashCode());

        // create system message instance
        Message sysmsg = new Message();
        sysmsg.obj = msg;

        sendCbkMessage(sysmsg);
    }

    private void handleErrorEvent(String netError, int errorCode)
    {

        NetMessage msg = new NetMessage();
        if (this.mIsDownLoad)
        {
            msg.setMessageType(NetMessage.NetMessageType.NetDownloadFailure);
            /*
             * File file = new File(mDownloadDstFilePath + ".temp"); file.delete();
             */
        }
        else
        {
            msg.setMessageType(NetMessageType.NetFailure);
            msg.setErrorCode(errorCode);
        }
        msg.setErrorString(netError);
        msg.setRequestId(this.hashCode());

        // create system message instance
        Message sysmsg = new Message();
        sysmsg.obj = msg;

        sendCbkMessage(sysmsg);
    }

    private void handleSuccessEvent(byte[] responseStr)
    {
        NetMessage msg = new NetMessage();
        if (this.mIsDownLoad)
        {
            msg.setMessageType(NetMessage.NetMessageType.NetDownloadSuccess);

        }
        else
        {
            msg.setMessageType(NetMessageType.NetSuccess);

            String resString = "";
            try
            {
//                AES myaes = new AES();
                resString =new String(responseStr, "UTF-8");
                mLogger.v("response data is" + resString);
                BaseResult res = JSONHelper.parserWithTag(this.mRequestTag, resString);
                msg.setResponseData(res);

            } catch (JSONException e)
            {
                // 捕获JSON异常
                e.printStackTrace();
                msg.setErrorCode(YCErrorCode.YC_JSON_PARSER_ERROR);
                msg.setErrorString("parse json error");
            } catch (Exception ex)
            {
                // net request failed
                msg.setErrorCode(YCErrorCode.YC_NET_DATA_ERROR);
                msg.setErrorString("receive data error");
            } finally
            {

            }
        }
        msg.setRequestId(this.hashCode());

        // create system message instance
        Message sysmsg = new Message();
        sysmsg.obj = msg;

        sendCbkMessage(sysmsg);
    }

    /*
     * @Transfer request cancel event to UI layer
     * 
     * @Param cancelStr specify the cancel reason and not use yet
     */
    private void handleCancelEvent(String cancelStr)
    {

        MyLogger.getLogger(this.getClass().getName()).v(cancelStr);

        NetMessage msg = new NetMessage();
        msg.setMessageType(NetMessageType.NetCancel);
        msg.setRequestId(this.hashCode());

        // create system message instance
        Message sysmsg = new Message();
        sysmsg.obj = msg;

        sendCbkMessage(sysmsg);
    }

    @Override
    public void run()
    {
    	if(getmRequestType()==GET){
    		handleNetGetRequest();
    	}else{
    		handleNetRequest();
    	}
    }
}
