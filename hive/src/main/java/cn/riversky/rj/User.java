package cn.riversky.rj;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by admin on 2017/11/30.
 */
public class User implements Writable {
    private int id;
    private String name;
    private String idCard;
    private String sex;
    private String born;
    private String address;
    private String withSex;
    private String currentDate;
    private String mobile;
    private String email;

    public User() {
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(id);
        dataOutput.writeUTF(name);
        dataOutput.writeUTF(idCard);
        dataOutput.writeUTF(sex);
        dataOutput.writeUTF(born);
        dataOutput.writeUTF(address);
        dataOutput.writeUTF(withSex);
        dataOutput.writeUTF(currentDate);
        dataOutput.writeUTF(mobile);
        dataOutput.writeUTF(email);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        id = dataInput.readInt();
        name = dataInput.readUTF();
        idCard = dataInput.readUTF();
        sex = dataInput.readUTF();
        born = dataInput.readUTF();
        address = dataInput.readUTF();
        withSex = dataInput.readUTF();
        currentDate = dataInput.readUTF();
        mobile = dataInput.readUTF();
        email = dataInput.readUTF();
    }

    public User(int id, String name, String idCard, String sex, String born, String address, String withSex, String currentDate, String mobile, String email) {
        this.id = id;
        this.name = name;
        this.idCard = idCard;
        this.sex = sex;
        this.born = born;
        this.address = address;
        this.withSex = withSex;
        this.currentDate = currentDate;
        this.mobile = mobile;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBorn() {
        return born;
    }

    public void setBorn(String born) {
        this.born = born;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWithSex() {
        return withSex;
    }

    public void setWithSex(String withSex) {
        this.withSex = withSex;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return id + "," + name + "," + idCard + "," +
                sex + "," + born + "," + address +
                "," + withSex + "," + currentDate +
                "," + mobile + "," + email;
    }
}
