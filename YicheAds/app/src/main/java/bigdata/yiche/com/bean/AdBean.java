package bigdata.yiche.com.bean;

/**
 * Created by wanglirong on 16/10/11.
 */

public class AdBean {

    private String ad_dvid;
    private int ad_pid ;
    private int ad_creativeId;
    private int ad_type;
    private String[] ad_picUrls;
    private String ad_title;
    private String ad_url;

    public String getAd_resourceId() {
        return ad_resourceId;
    }

    public void setAd_resourceId(String ad_resourceId) {
        this.ad_resourceId = ad_resourceId;
    }

    private String ad_resourceId;


    public String getAd_dvid() {
        return ad_dvid;
    }

    public void setAd_dvid(String ad_dvid) {
        this.ad_dvid = ad_dvid;
    }

    public int getAd_pid() {
        return ad_pid;
    }

    public void setAd_pid(int ad_pid) {
        this.ad_pid = ad_pid;
    }

    public int getAd_creativeId() {
        return ad_creativeId;
    }

    public void setAd_creativeId(int ad_creativeId) {
        this.ad_creativeId = ad_creativeId;
    }

    public int getAd_type() {
        return ad_type;
    }

    public void setAd_type(int ad_type) {
        this.ad_type = ad_type;
    }

    public String[] getAd_picUrls() {
        return ad_picUrls;
    }

    public void setAd_picUrls(String[] ad_picUrls) {
        this.ad_picUrls = ad_picUrls;
    }

    public String getAd_title() {
        return ad_title;
    }

    public void setAd_title(String ad_title) {
        this.ad_title = ad_title;
    }

    public String getAd_url() {
        return ad_url;
    }

    public void setAd_url(String ad_url) {
        this.ad_url = ad_url;
    }
}
