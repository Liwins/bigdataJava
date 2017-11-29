package cn.riversky.rjoin;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by admin on 207/11/27.
 */
public class DetailOne implements Writable{
    private String oid;
    private String pid;
    private String pname;

    private String flag;
    public DetailOne() {
    }

    public void setAll(String oid,  String pid, String pname,String flag) {
        this.oid = oid;
        this.pid=pid;
        this.pname = pname;
        this.flag = flag;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
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
        dataOutput.writeUTF(pid);
        dataOutput.writeUTF(pname);
        dataOutput.writeUTF(flag);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.oid = dataInput.readUTF();
        this.pid = dataInput.readUTF();
        this.pname =dataInput.readUTF();
        this.flag=dataInput.readUTF();
    }


    @Override
    public String toString() {
        return "Detail{" +
                "oid='" + oid + '\'' +
                ", pid='" + pid + '\'' +
                ", pname='" + pname + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}
