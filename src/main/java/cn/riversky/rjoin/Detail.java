package cn.riversky.rjoin;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by admin on 207/11/27.
 */
public class Detail implements Writable{
    private String oid;
    private String date;
    private String onum;
    private String pid;
    private String pname;
    private String ku;
    private String price;
    private String flag;
    public Detail() {
    }

    public void setAll(String oid, String date, String onum, String pid, String pname, String ku, String price,String flag) {
        this.oid = oid;
        this.date = date;
        this.onum = onum;
        this.pid = pid;
        this.pname = pname;
        this.ku = ku;
        this.price = price;
        this.flag = flag;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getOnum() {
        return onum;
    }

    public void setOnum(String onum) {
        this.onum = onum;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getKu() {
        return ku;
    }

    public void setKu(String ku) {
        this.ku = ku;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    /**
     *      this.oid = oid;
     this.date = date;
     this.opid = opid;
     this.onum = onum;
     this.pid = pid;
     this.pname = pname;
     this.ku = ku;
     this.price = price;
     flag
     * @param dataOutput
     * @throws IOException
     */
    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(oid);
        dataOutput.writeUTF(date);
        dataOutput.writeUTF(onum);
        dataOutput.writeUTF(pid);
        dataOutput.writeUTF(pname);
        dataOutput.writeUTF(ku);
        dataOutput.writeUTF(price);
        dataOutput.writeUTF(flag);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.oid = dataInput.readUTF();
        this.date =  dataInput.readUTF();
        this.onum =dataInput.readUTF();
        this.pid = dataInput.readUTF();
        this.pname =dataInput.readUTF();
        this.ku = dataInput.readUTF();
        this.price =dataInput.readUTF();
        this.flag=dataInput.readUTF();
    }


    @Override
    public String toString() {
        return "Detail{" +
                "oid='" + oid + '\'' +
                ", date='" + date + '\'' +
                ", onum='" + onum + '\'' +
                ", pid='" + pid + '\'' +
                ", pname='" + pname + '\'' +
                ", ku='" + ku + '\'' +
                ", price='" + price + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}
