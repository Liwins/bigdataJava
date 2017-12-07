package cn.riversky.udf;

/**
 * {"movie":"223","rate":"233","timeStamp":"978301763","uid":"2"}
 * Created by admin on 2017/12/2.
 */
public class Rate {
    private String movie;
    private String rate;
    private String timeStamp;
    private String uid;

    public Rate() {
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return movie + ","+ rate + ","+ timeStamp + "," + uid ;
    }
}
