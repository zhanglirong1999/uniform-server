package com.school.uniform.common;

public class CONST {
    public static final int SUCCESS_CODE = 200;
    public static final int FAIL_CODE = -1;
    public static final String SUCCESS_MESSAGE_DEFAULT = "SUCCESS";
    public static final String FAIL_MESSAGE_DEFAULT = "FAIL";

    /**
     * 同意1，拒绝0
     */
//    public static final int FRIEND_ACTION_Y = 1;
//    public static final int FRIEND_ACTION_N = 0;

    // wx 参数
    public static final String appId = "wx46e0f3ded2190ecf";
    public static final String appSecret = "a5ba3736dbf6a66715b43fc05ab3f8db";
    public static final String mch_id = "1603201061";
    public static final String notify_url="http://www.alumnisystem.cn/wxpay/callback";
//public static final String notify_url="http://localhost:18080/wxpay/callback";

    public static final String key="";
    public static final String SIGNTYPE="MD5";
    //微信统一下单接口地址
    public static final String pay_url = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    //  "a5ba3736dbf6a66715b43fc05ab3f8db"
    // ACL Key
    public static final String ACL_ACCOUNTID = "accountId";
    public static final String SCHOOL_ID ="schoolId";
    public static final String SOLICIT_ID="sid";

    public static final String ManAvatar ="https://uniform-nj-1301042386.cos.ap-nanjing.myqcloud.com/38114208764928.jpg";
    public static final String WomanAvatar ="https://uniform-nj-1301042386.cos.ap-nanjing.myqcloud.com/38114152631296.jpg";


    public static final String fail() {
        return "<xml>\n" +
                "  <return_code><![CDATA[FAIL]]></return_code>\n" +
                "  <return_msg><![CDATA[]]></return_msg>\n" +
                "</xml>";
    }

    public static final String success() {
        return "<xml>\n" +
                "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                "</xml>";
    }

}
