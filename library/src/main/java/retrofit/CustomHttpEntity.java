package retrofit;

import base.BaseEntity;

/**
 * author： admin
 * date： 2018/4/24
 * describe：
 */
public class CustomHttpEntity  extends BaseEntity{

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public String getRequesttime() {
        return requesttime;
    }

    public void setRequesttime(String requesttime) {
        this.requesttime = requesttime;
    }

    private String url;
    private String method;
    private String response;
    private boolean isOk;
    private String requesttime;
    private String header ="";

    public long getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(long saveTime) {
        this.saveTime = saveTime;
    }

    private long saveTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    private String startTime =  "0";

    private String endTime =  "0";

    private String size = "0";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type = "";

    public String getUserDataHeader() {
        return userDataHeader;
    }

    public void setUserDataHeader(String userDataHeader) {
        this.userDataHeader = userDataHeader;
    }

    public String getApiAuthHeader() {
        return apiAuthHeader;
    }

    public void setApiAuthHeader(String apiAuthHeader) {
        this.apiAuthHeader = apiAuthHeader;
    }

    private String userDataHeader = "";
    private String apiAuthHeader = "";
}
