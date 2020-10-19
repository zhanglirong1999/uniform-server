package com.school.uniform.util;

import com.school.uniform.common.IdGenerator;
import lombok.AllArgsConstructor;
import org.apache.http.entity.ContentType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

@Component
public class ConstantUtil {
    static IdGenerator idGenerator;

    @AllArgsConstructor
    public enum BizExceptionCause{
        MISMATCH(1,"不匹配"),
        TRY_EXCEED(2,"超过最大尝试次数"),
        TIME_EXCEED(3,"超时"),
        NOT_FOUND(4,"无效token"),
        LOW_AUTHORITY(5,"权限不够"),
        NOT_LOGINED(6,"未登录"),
        NOT_OPENID(7,"获取openid失败"),
        NOT_USER(8,"找不到此用户"),
        NOT_TAG(9,"请填写标签名称"),
        LOSS_NAME(10,"请填写姓名"),
        LOSS_GENDER(11,"请填写性别"),
        LOSS_ADMINPHONE(12,"管理员电话为空"),
        LOSS_ADMINNAME(13,"管理员姓名名为空"),
        LOSS_VERIFY(14,"缺少验证码"),
        ERROR_VERIFY(15,"管理员验证码格式非法"),
        ERROR_PHONE(16,"当前的需要发出的手机号不为 1 个"),
        PAST_CODE(17,"验证码已失效, 请重新获取!"),
        ERROR_CODE(18,"验证码不正确, 请重新获取!"),
        LOSS_DETAIL(19,"商品信息请填写完整"),
        LOSS_SCHOOLNAME(20,"请填写学校"),
        ERROR_GENDER(21,"无法识别性别"),
        LOSS_SEX(22,"请选择男女"),
        LOSS_SIZE(23,"请选择大小"),
        LOSS_COUNT(24,"缺少数量"),
        ERROR_INFOSHOP(25,"购物车信息错误，查找不到对应商品"),
        LOSS_POSITION(26,"请填写地址"),
        ERROR_STATE(27,"没有此状态"),
        HAVE_SEND(28,"此货物已经发货"),
        IS_SHOPPING(29,"订单错误，尚未购买"),
        LOSS_PHONE(30,"缺少电话"),
        ERROR_NUM(31,"商品数和最少数不匹配"),
        ERROR_SIZEANDPRICE(32,"大小和价格数目不匹配"),
        NO_SOLICIT(33,"没有此征订订单"),
        NO_BUYTWO(34,"不能重复购买此订单"),
        ERROR_PAY(35,"微信支付统一下单失败"),
        ERROR_ADMIN(36,"用户名或手机号错误"),
        LOSS_STUDENT(37,"没有学生信息")

        ;
        public final Integer code;
        public final String reason;//解释

    }

    static {
//        idGenerator = new SnowflakeIdGenerator(0, 0);
        idGenerator = new IdGenerator();
    }

    /**
     * bigint生成器
     * @return
     */
    public static long generateId() {
        return idGenerator.nextId();
    }


    public static MultipartFile fileToMultipartFile(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        return new MockMultipartFile(file.getName(), file.getName(),
                ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
    }

    public static Date addEightHours(Date date) {
        return new Date(date.getTime() + 8 * 60 * 60 * 1000);
    }

    public static Boolean deleteFileUnderProjectDir(String fileName) {
        // 然后应该删除项目目录下的本地文件
        File targetFile = new File(System.getProperty("user.dir") + File.separator + fileName);
        return targetFile.delete();
    }

    /**
     * @param requestUrl    请求地址
     * @param requestMethod 请求方法
     * @param outputStr     参数
     */
    public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {
        // 创建SSLContext
        StringBuffer buffer = null;
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(requestMethod);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            //往服务器端写内容
            if (null != outputStr) {
                OutputStream os = conn.getOutputStream();
                os.write(outputStr.getBytes("utf-8"));
                os.close();
            }
            // 读取服务器端返回的内容
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            buffer = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }


}
