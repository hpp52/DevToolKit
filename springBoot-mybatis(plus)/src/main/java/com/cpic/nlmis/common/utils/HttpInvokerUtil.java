package com.cpic.nlmis.common.utils;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

/**
 * @Author: hk
 * @Description:
 * @Date: 2019/8/15 11:16
 */
public class HttpInvokerUtil {
    private final static Logger logger = LoggerFactory.getLogger(HttpInvokerUtil.class);

    /**
     * HttpURLConnection
     * @param sUrl: 请求地址
     * @param sMethod: 请求方式GET、POST
     * @param sOutput: 请求参数
     * @param proxyHost: 代理地址
     * @param proxyPort: 代理IP
     * @param contentType: 请求Content-type
     * @return
     */
    public static JSONObject exec(String sUrl, String sMethod, String sOutput,
                                  String proxyHost, String proxyPort, String contentType) {
        JSONObject json = null;
        StringBuffer buffer = new StringBuffer();

        HttpURLConnection con = null;
        try {
            URL url = new URL(sUrl);

            if(!StringUtils.isEmpty(proxyHost) && !StringUtils.isEmpty(proxyPort)) {
                // 如果是本机自己测试,不需要代理请求,但发到服务器上的时候需要代理请求
                logger.info("开始代理访问接口----------");
                // 对http开启全局代理
                System.setProperty("http.proxyHost", proxyHost);
                System.setProperty("http.proxyPort", proxyPort);
                // 对https开启全局代理
                System.setProperty("https.proxyHost", proxyHost);
                System.setProperty("https.proxyPort", proxyPort);

                // 代理访问http请求
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, Integer.parseInt(proxyPort)));
                con = (HttpURLConnection) url.openConnection(proxy);
                logger.info("结束代理访问接口----------");
            } else {
                // 原生访问http请求，未代理请求
                con = (HttpURLConnection) url.openConnection();
            }

            // json方式调用
            if(!StringUtils.isEmpty(contentType) && "JSON".equals(contentType)) {
//				con.setRequestProperty("Content-type", "application/json");
                con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

            }

            con.setDoOutput(true); // 是否可以输出
            con.setDoInput(true); // 是否可以输入
            con.setUseCaches(false); // 是否可以使用缓存
            con.setRequestMethod(sMethod);
            // con.setConnectTimeout(60000); // 最高超时时间

            con.setReadTimeout(60000); // 最高读取时间
            con.setConnectTimeout(60000); // 最高连接时间

            if (sOutput != null) {
                OutputStream os = con.getOutputStream();
                try {
                    os.write(sOutput.getBytes("UTF-8"));
                } catch (Exception e) {
                    logger.info("HttpInvoker exec error: {}", e);
                } finally {
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e) {
                            logger.info("HttpInvoker exec error: {}", e);
                        }
                    }
                }
            }

            InputStream is = null;
            InputStreamReader inputReader = null;
            BufferedReader reader = null;
            try {
                is = con.getInputStream();
                inputReader = new InputStreamReader(is, "UTF-8");
                reader = new BufferedReader(inputReader);
                String temp;
                while ((temp = reader.readLine()) != null) {
                    buffer.append(temp);
                }
                logger.info("==>HttpInvoker exec buffer: {}"+ buffer==null||buffer.length()<=0?null:buffer.toString());
            } catch (Exception e) {
                logger.info("HttpInvoker exec error: {}", e);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        logger.info("HttpInvoker exec error: {}", e);
                    }
                }
                if (inputReader != null) {
                    try {
                        inputReader.close();
                    } catch (IOException e) {
                        logger.info("HttpInvoker exec error: {}", e);
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        logger.info("HttpInvoker exec error: {}", e);
                    }
                }
            }

            // con.disconnect();
            json = JSONObject.parseObject(buffer.toString());
            if (json != null) {
                logger.info("OK, http连接Url: {}, 返回数据,json: {}"+sUrl+ json);
            } else {
                logger.info("return json is null, http连接Url: {}, 返回数据,json: {}"+sUrl+ json);
            }
        } catch (IOException e) {
            logger.info("HttpInvoker exec error: {}", e);
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

        return json;
    }




    public static void main(String[] args) {
        String proxyHost = "10.63.46.31";
        String proxyPort = "8002";
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?"
                +"appid="+"wx54cd4420615e1d98"
                +"&secret="+"5b1000840ad23d290c54ed5f64a4a0dd"
                +"&code="+"081DecKz13Nfmc0liAMz1TIrKz1DecK4"
                +"grant_type=authorization_code";
        String sMethod = "POST";
        String sOutput ="";
        JSONObject json= HttpInvokerUtil.exec(url, sMethod, sOutput, proxyHost, proxyPort, "");
        System.out.println(json);

    }
}
