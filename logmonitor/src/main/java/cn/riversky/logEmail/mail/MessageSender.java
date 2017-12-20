package cn.riversky.logEmail.mail;


import cn.riversky.logEmail.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/15.
 */
public class MessageSender {
    private static final Logger logger = Logger.getLogger(MessageSender.class);

    /**
     * 发送邮件，邮件内容为文本格式
     *
     * @param mailInfo
     * @return
     */
    public static boolean sendMail(MailInfo mailInfo) {
        try {

            //生成基本邮件发送信息，主要包含，发送者，发送目的，抄送者，主题，时间
            Message mailMessage = generateBaseInfo(mailInfo);
            mailMessage.setText(mailInfo.getMailContent());
            //发送邮件（传输）
            Transport.send(mailMessage);
            logger.info("[txt格式邮件发送完毕,发送时间: " + DateUtils.getDateTime() + "]");
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean sendHtmlMail(MailInfo mailInfo) {
        try {
            //设置邮件的发送人，接收者，抄送者，发送时间，发送主体
            Message mailMessage=generateBaseInfo(mailInfo);
            //由于发送的带有html解析，甚至是附件，所以构建容器
            Multipart mainPart=new MimeMultipart();
            //邮件体的部分
            BodyPart html=new MimeBodyPart();
            //指定文本类型和文本数据
            html.setContent(mailInfo.getMailContent(),"text/html;charset=utf-8");
            mainPart.addBodyPart(html);
            mailMessage.setContent(mainPart);
            Transport.send(mailMessage);
            logger.info("[html 邮件发送完毕，发送时间:"+DateUtils.getDateTime()+"]");
            return true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 构造基本发送信息
     *
     * @param mailInfo
     * @return
     */
    public static Message generateBaseInfo(MailInfo mailInfo) throws UnsupportedEncodingException, MessagingException {
        /**
         * 判断是否需要身份认证
         */
        MailAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties();
        /**
         * 如果需要身份认证，创建密码验证器
         */
        if (mailInfo.isAuthValidate()) {
            authenticator = new MailAuthenticator(mailInfo.getUserName(), mailInfo.getUserPassword());
        }
        /**
         * 根据邮件会话属性和密码验证器构造一个发送邮件的session
         */
        Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
        Message mailMessage = null;
        //根据session创建一个邮件消息
        mailMessage = new MimeMessage(sendMailSession);
        //创建邮件发送地
        Address from = new InternetAddress(mailInfo.getFromAddress(), mailInfo.getFromUserName());
        //设置邮件发送者
        mailMessage.setFrom(from);
        if (mailInfo.getToAddress() != null && mailInfo.getToAddress().contains(",")) {
            mailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailInfo.getToAddress()));
        } else {
            Address to = new InternetAddress(mailInfo.getToAddress());
            mailMessage.setRecipient(Message.RecipientType.TO, to);
        }
        //设置抄送邮件地址
        if (StringUtils.isNotBlank(mailInfo.getCcAddress())) {
            if (mailInfo.getCcAddress().contains(",")) {
                mailMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(mailInfo.getCcAddress()));
            } else {
                Address to = new InternetAddress(mailInfo.getCcAddress());
                mailMessage.setRecipient(Message.RecipientType.CC, to);
            }
        }
        //设置发送主体和发送时间
        mailMessage.setSubject(mailInfo.getMailSubject());
        mailMessage.setSentDate(new Date());
        return mailMessage;
    }

    public static void main(String[] args) {
        List<String> receivers=new ArrayList<String>();
        receivers.add("riversky@126.com");
        receivers.add("472242652@qq.com");
        List<String> cc=new ArrayList<String>();
        String context="First name:<br>\n" +
                "<input type=\"text\" name=\"firstname\" value=\"hello word\">\n" +
                "<br>\n" +
                "Last name:<br>\n" +
                "<input type=\"text\" name=\"lastname\">\n" +
                "<p style=\"font-family:verdana;color:red\">请注意表单本身是不可见的。</p>";
        MailInfo mailInfo=new MailInfo("测试",context,receivers,cc);
        sendHtmlMail(mailInfo);
    }
}
