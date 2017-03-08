package bigdata.yiche.com.netresponse;

/**
 * Created by yiche on 16/10/10.
 */
public class BaseResult {
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;
    private int status;

}
