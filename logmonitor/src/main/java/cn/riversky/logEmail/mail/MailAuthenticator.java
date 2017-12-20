package cn.riversky.logEmail.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/15.
 */
public class MailAuthenticator extends Authenticator{
    String userName;
    String userPassword;

    public MailAuthenticator() {
    }

    public MailAuthenticator(String userName, String userPassword) {
        super();
        this.userName = userName;
        this.userPassword = userPassword;
    }
    @Override
    public PasswordAuthentication getPasswordAuthentication(){
        return new PasswordAuthentication(userName,userPassword);
    }
}
