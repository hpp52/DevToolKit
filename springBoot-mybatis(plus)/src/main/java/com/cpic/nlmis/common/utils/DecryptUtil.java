package com.cpic.nlmis.common.utils;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import static com.cpic.nlmis.common.utils.SymbolUtil.Separator;

/**
 * @Author: hk
 * @Description:
 * @Date: 2019/8/15 10:17
 */
public class DecryptUtil {
    /**
     * 加盐过程：原字段+"|"+预约时间
     * 加密过程：base64加密
     * @param key
     * @return
     * @throws Exception
     */
    public  static String decryptBASE64AndRemoveSalt(String key) throws Exception{
        byte[] decoded = Base64.getDecoder().decode(key);
        String decodeStr = new String(decoded);
        if (!decodeStr.contains(Separator)){
            throw new Exception("解密错误,没有盐 | 符号");
        }
        String removeSaltStr=decodeStr.substring(0,decodeStr.indexOf("|"));
        return removeSaltStr;
    }

    public static void main(String[] args) {
        String decodeStr="ahuihajh|123565";
        String removeSaltStr=decodeStr.substring(0,decodeStr.indexOf("|"));
        System.out.println(removeSaltStr);
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(dateFormat.format(new Date()));
    }
}
