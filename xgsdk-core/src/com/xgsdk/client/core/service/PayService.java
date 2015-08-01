
package com.xgsdk.client.core.service;

import com.xgsdk.client.core.XGInfo;
import com.xgsdk.client.core.SystemInfo;
import com.xgsdk.client.core.http.HttpUtils;
import com.xgsdk.client.core.utils.MD5Util;
import com.xgsdk.client.core.utils.SHA1Util;
import com.xgsdk.client.core.utils.XGLog;
//import com.xgsdk.client.util.ProductConfig;


import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Build;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class PayService {

    // 创建订单URI
    public static String PAY_NEW_ORDER_URI = "/pay/create-order";
    // 更新订单URI
    public static String PAY_UPDATE_ORDER_URI = "/xgsdk/apiXgsdkPay/updateOrder";
    // 取消订单URI
    public static String PAY_CANCEL_ORDER_URI = "/xgsdk/apiXgsdkPay/cancelOrder";
    // 刷新余额接口
    public static String PAY_REFRESHBALANCE_URI = "/xgsdk/apiXgsdkPay/refreshBalance";

    public static String PAY_VERIFY_ORDER_URI = "/xgsdk/apiXgsdkPay/verifyOrder";

    private static final int THREAD_JOIN_TIME_OUT = 30000;

    private static final String INTERFACE_TYPE_CREATE_ORDER = "create-order";
    private static final String INTERFACE_TYPE_UPDATE_ORDER = "update-order";

    private static final String TIME_PATTERN = "yyyyMMddHHmmss";
    private static final SimpleDateFormat sTsFormat = new SimpleDateFormat(
            TIME_PATTERN, Locale.getDefault());

    // 单独线程运行方式
    private static String createOrderInThread(final Activity activity,
            final String appId, final String appKey, final String channelId,
            final String uId, final String productId, final String productName,
            final String productDec, final String amount,
            final String totalPrice, final String serverId,
            final String zoneId, final String roleId, final String roleName,
            final String currencyName, final String payExt,
            final String gameOrderId, final String notifyUrl) throws Exception {

        Callable<String> callable = new Callable<String>() {
            public String call() throws Exception {
                return PayService.createOrder(activity, appId, appKey,
                        channelId, uId, productId, productName, productDec,
                        amount, totalPrice, serverId, zoneId, roleId, roleName,
                        currencyName, payExt, gameOrderId, notifyUrl);
            }
        };
        FutureTask<String> future = new FutureTask<String>(callable);
        Thread thread = new Thread(future);
        thread.start();
        thread.join(THREAD_JOIN_TIME_OUT);
        return future.get();
    }

    public static String createOrderInThread(final Activity activity,
            final String uId, final String productId, final String productName,
            final String productDec, final String amount,
            final String totalPrice, final String serverId,
            final String zoneId, final String roleId, final String roleName,
            final String currencyName, final String payExt,
            final String gameOrderId, final String notifyUrl) throws Exception {
        return createOrderInThread(activity, XGInfo.getXGAppId(activity),
                XGInfo.getXGAppKey(activity), XGInfo.getChannelId(), uId,
                productId, productName, productDec, amount, totalPrice,
                serverId, zoneId, roleId, roleName, currencyName, payExt,
                gameOrderId, notifyUrl);
    }

    // 单独线程运行方式
    private static String createOrderInThreadForOriginal(
            final Activity activity, final String appId, final String appKey,
            final String channelId, final String uId, final String productId,
            final String productName, final String productDec,
            final String amount, final String totalPrice,
            final String serverId, final String zoneId, final String roleId,
            final String roleName, final String currencyName,
            final String payExt, final String gameOrderId,
            final String notifyUrl) throws Exception {
        Callable<String> callable = new Callable<String>() {
            public String call() throws Exception {
                return PayService.createOrderForOriginal(activity, appId,
                        appKey, channelId, uId, productId, productName,
                        productDec, amount, totalPrice, serverId, zoneId,
                        roleId, roleName, currencyName, payExt, gameOrderId,
                        notifyUrl);
            }
        };
        FutureTask<String> future = new FutureTask<String>(callable);
        Thread thread = new Thread(future);
        thread.start();
        thread.join(THREAD_JOIN_TIME_OUT);
        orderId = future.get();
        return orderId;
    }

    public static String createOrderInThreadForOriginal(
            final Activity activity, final String uId, final String productId,
            final String productName, final String productDec,
            final String amount, final String totalPrice,
            final String serverId, final String zoneId, final String roleId,
            final String roleName, final String currencyName,
            final String payExt, final String gameOrderId,
            final String notifyUrl) throws Exception {
        return createOrderInThreadForOriginal(activity,
                XGInfo.getXGAppId(activity), XGInfo.getXGAppKey(activity),
                XGInfo.getChannelId(), uId, productId, productName, productDec,
                amount, totalPrice, serverId, zoneId, roleId, roleName,
                currencyName, payExt, gameOrderId, notifyUrl);
    }

    public static String orderId = "";

    // 单独线程运行方式
    private static void updateOrderInThread(final Activity activity,
            final String appId, final String appKey, final String channelId,
            final String orderId, final String uId, final String productId,
            final String productName, final String productDec,
            final String amount, final String totalPrice,
            final String serverId, final String zoneId, final String roleId,
            final String roleName, final String currencyName,
            final String payExt, final String gameOrderId,
            final String notifyUrl) throws Exception {
        PayService.orderId = orderId;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PayService.updateOrder(activity, appId, appKey, channelId,
                            orderId, uId, productId, productName, productDec,
                            amount, totalPrice, serverId, zoneId, roleId,
                            roleName, currencyName, payExt, gameOrderId,
                            notifyUrl);
                } catch (Exception ex) {
                    XGLog.e(ex.getMessage(), ex);
                }
            }
        });
        thread.start();
        thread.join(THREAD_JOIN_TIME_OUT);
    }

    public static void updateOrderInThread(final Activity activity,
            final String orderId, final String uId, final String productId,
            final String productName, final String productDec,
            final String amount, final String totalPrice,
            final String serverId, final String zoneId, final String roleId,
            final String roleName, final String currencyName,
            final String payExt, final String gameOrderId,
            final String notifyUrl) throws Exception {
        updateOrderInThread(activity, XGInfo.getXGAppId(activity),
                XGInfo.getXGAppKey(activity), XGInfo.getChannelId(), orderId,
                uId, productId, productName, productDec, amount, totalPrice,
                serverId, zoneId, roleId, roleName, currencyName, payExt,
                gameOrderId, notifyUrl);
    }

    // 单独线程运行方式
    private static void cancelOrderInThread(final Activity activity,
            final String appId, final String appKey, final String orderId)
            throws Exception {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PayService.cancelOrder(activity, appId, appKey, orderId);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    XGLog.e(ex.getMessage(), ex);
                }
            }
        });
        thread.start();
        thread.join(THREAD_JOIN_TIME_OUT);
    }

    public static void cancelOrderInThread(final Activity activity,
            String orderId) throws Exception {
        cancelOrderInThread(activity, XGInfo.getXGAppId(activity),
                XGInfo.getXGAppKey(activity), orderId);
    }

    private static String createOrder(final Activity activity,
            final String appId, final String appKey, final String channelId,
            final String uId, final String productId, final String productName,
            final String productDec, final String amount,
            final String totalPrice, final String serverId,
            final String zoneId, final String roleId, final String roleName,
            final String currencyName, final String payExt,
            final String gameOrderId, final String notifyUrl) throws Exception {
        // 发送请求
        String result = createOrderForOriginal(activity, appId, appKey,
                channelId, uId, productId, productName, productDec, amount,
                totalPrice, serverId, zoneId, roleId, roleName, currencyName,
                payExt, gameOrderId, notifyUrl);
        JSONObject jsonResult = new JSONObject(result);
        if (!"1".equals(jsonResult.getString("code"))) {
            throw new Exception("response exception:"
                    + jsonResult.getString("msg"));
        }
        JSONObject jsonData = jsonResult.getJSONObject("data");
        if (null == jsonData) {
            throw new Exception("response exception:"
                    + jsonResult.getString("msg"));
        }
        PayService.orderId = jsonData.getString("orderId");
        return jsonData.getString("orderId");
    }

    public static String createOrder(final Activity activity, final String uId,
            final String productId, final String productName,
            final String productDec, final String amount,
            final String totalPrice, final String serverId,
            final String zoneId, final String roleId, final String roleName,
            final String currencyName, final String payExt,
            final String gameOrderId, final String notifyUrl) throws Exception {
        return createOrder(activity, XGInfo.getXGAppId(activity),
                XGInfo.getXGAppKey(activity), XGInfo.getChannelId(), uId,
                productId, productName, productDec, amount, totalPrice,
                serverId, zoneId, roleId, roleName, currencyName, payExt,
                gameOrderId, notifyUrl);
    }

    private static String createOrderForOriginal(final Activity activity,
            final String appId, final String appKey, final String channelId,
            final String uId, final String productId, final String productName,
            final String productDec, final String amount,
            final String totalPrice, final String serverId,
            final String zoneId, final String roleId, final String roleName,
            final String currencyName, final String payExt,
            final String gameOrderId, final String notifyUrl) throws Exception {
        // 排序签名
        StringBuilder getUrl = generateRequestUrl(activity, PAY_NEW_ORDER_URI,
                INTERFACE_TYPE_CREATE_ORDER, appId, appKey, channelId, null,
                uId, productId, productName, productDec, amount, totalPrice,
                serverId, zoneId, roleId, roleName, currencyName, payExt,
                gameOrderId, notifyUrl);
        // 发送请求
        String result = HttpUtils.executeHttpGet(getUrl.toString());
        // 返回结果为空
        if (TextUtils.isEmpty(result)) {
            // 生成订单失败
            throw new Exception("request:" + getUrl.toString()
                    + ",response is null.");
        }
        return result;
    }

    public static String createOrderForOriginal(final Activity activity,
            final String uId, final String productId, final String productName,
            final String productDec, final String amount,
            final String totalPrice, final String serverId,
            final String zoneId, final String roleId, final String roleName,
            final String currencyName, final String payExt,
            final String gameOrderId, final String notifyUrl) throws Exception {
        return createOrderForOriginal(activity, XGInfo.getXGAppId(activity),
                XGInfo.getXGAppKey(activity), XGInfo.getChannelId(), uId,
                productId, productName, productDec, amount, totalPrice,
                serverId, zoneId, roleId, roleName, currencyName, payExt,
                gameOrderId, notifyUrl);
    }

    private static void updateOrder(final Activity activity,
            final String appId, final String appKey, final String channelId,
            final String orderId, final String uId, final String productId,
            final String productName, final String productDec,
            final String amount, final String totalPrice,
            final String serverId, final String zoneId, final String roleId,
            final String roleName, final String currencyName,
            final String payExt, final String gameOrderId,
            final String notifyUrl) throws Exception {
        PayService.orderId = orderId;
        StringBuilder getUrl = generateRequestUrl(activity,
                PAY_UPDATE_ORDER_URI, INTERFACE_TYPE_UPDATE_ORDER, appId,
                appKey, channelId, orderId, uId, productId, productName,
                productDec, amount, totalPrice, serverId, zoneId, roleId,
                roleName, currencyName, payExt, gameOrderId, notifyUrl);
        // 发送请求
        String result = HttpUtils.executeHttpGet(getUrl.toString());
        // 返回结果为空
        if (TextUtils.isEmpty(result)) {
            // 生成订单失败
            throw new Exception("request:" + getUrl.toString()
                    + ",response is null.");
        }
        JSONObject jsonResult = new JSONObject(result);
        if ("1".equals(jsonResult.getString("code"))) {
            return;
        } else {
            throw new Exception("response exception:"
                    + jsonResult.getString("msg"));
        }
    }

    private static StringBuilder generateRequestUrl(final Activity activity,
            final String uri, final String interfacetype, final String appId,
            final String appKey, final String channelId, final String orderId,
            final String uId, final String productId, final String productName,
            final String productDec, final String amount,
            final String totalPrice, final String serverId,
            final String zoneId, final String roleId, final String roleName,
            final String currencyName, final String payExt,
            final String gameOrderId, final String notifyUrl) throws Exception {
        List<NameValuePair> requestParams = new ArrayList<NameValuePair>();
        if (!TextUtils.isEmpty(orderId)) {
            requestParams.add(new BasicNameValuePair("orderId", orderId));
        }
        if (!TextUtils.isEmpty(appId)) {
            requestParams.add(new BasicNameValuePair("sdkAppid", appId));
        }
        if (!TextUtils.isEmpty(channelId)) {
            requestParams.add(new BasicNameValuePair("channelId", channelId));
        }
        if (!TextUtils.isEmpty(uId)) {
            requestParams.add(new BasicNameValuePair("sdkUid", uId));
        }
        if (!TextUtils.isEmpty(totalPrice)) {
            requestParams.add(new BasicNameValuePair("totalPrice", totalPrice));
            requestParams.add(new BasicNameValuePair("originalPrice",
                    totalPrice));
        }
        if (!TextUtils.isEmpty(amount)) {
            requestParams.add(new BasicNameValuePair("appGoodsAmount", amount));
        }
        if (!TextUtils.isEmpty(productId)) {
            requestParams.add(new BasicNameValuePair("appGoodsId", productId));
        }
        if (!TextUtils.isEmpty(productName)) {
            requestParams.add(new BasicNameValuePair("appGoodsName",
                    productName));
        }
        if (!TextUtils.isEmpty(productDec)) {
            requestParams
                    .add(new BasicNameValuePair("appGoodsDesc", productDec));
        }
        if (!TextUtils.isEmpty(serverId)) {
            requestParams.add(new BasicNameValuePair("serverId", serverId));
        }
        if (!TextUtils.isEmpty(zoneId)) {
            requestParams.add(new BasicNameValuePair("zoneId", zoneId));
        }
        if (!TextUtils.isEmpty(roleId)) {
            requestParams.add(new BasicNameValuePair("roleId", roleId));
        }

        if (!TextUtils.isEmpty(roleName)) {
            requestParams.add(new BasicNameValuePair("roleName", roleName));
        }
        if (!TextUtils.isEmpty(currencyName)) {
            requestParams.add(new BasicNameValuePair("currencyName",
                    currencyName));
        }
        if (!TextUtils.isEmpty(payExt)) {
            requestParams.add(new BasicNameValuePair("custom", payExt));
        }
        if (!TextUtils.isEmpty(XGInfo.getXGSdkVersion())) {
            requestParams.add(new BasicNameValuePair("sdkVersion", XGInfo
                    .getXGSdkVersion()));
        }
        String ts = sTsFormat.format(new Date(System.currentTimeMillis()));
        if (!TextUtils.isEmpty(ts)) {
            requestParams.add(new BasicNameValuePair("ts", ts));
        }
        String planId = XGInfo.getXGPlanId(activity);
        if (!TextUtils.isEmpty(planId)) {
            requestParams.add(new BasicNameValuePair("planId", planId));
        }
        String buildNumber = XGInfo.getXGBuildNumber(activity);
        if (!TextUtils.isEmpty(buildNumber)) {
            requestParams
                    .add(new BasicNameValuePair("buildNumber", buildNumber));
        }
        String deviceId = XGInfo.getXGDeviceId(activity);
        if (!TextUtils.isEmpty(deviceId)) {
            requestParams.add(new BasicNameValuePair("deviceId", deviceId));
        }
        String deviceBrand = SystemInfo.getDeviceBrand();
        if (!TextUtils.isEmpty(deviceBrand)) {
            requestParams
                    .add(new BasicNameValuePair("deviceBrand", deviceBrand));
        }
        String deviceModel = SystemInfo.getDeviceModel();
        if (!TextUtils.isEmpty(deviceModel)) {
            requestParams
                    .add(new BasicNameValuePair("deviceModel", deviceModel));
        }
        if (!TextUtils.isEmpty(gameOrderId)) {
            requestParams
                    .add(new BasicNameValuePair("gameTradeNo", gameOrderId));
        }
        if (!TextUtils.isEmpty(notifyUrl)) {
            requestParams.add(new BasicNameValuePair("gameCallbackUrl",
                    notifyUrl));
        }
        if (!TextUtils.isEmpty(interfacetype)) {
            requestParams.add(new BasicNameValuePair("type", interfacetype));
        }
        Collections.sort(requestParams, new Comparator<NameValuePair>() {
            @Override
            public int compare(NameValuePair lhs, NameValuePair rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });
        // 生成MD5签名
        StringBuilder strSign = new StringBuilder();
        for (int i = 0; i < requestParams.size(); i++) {
            NameValuePair nvPair = requestParams.get(i);
            strSign.append(nvPair.getName()).append("=")
                    .append(nvPair.getValue());
            if (i < requestParams.size() - 1) {
                strSign.append("&");
            }
        }
        String requestContent = URLEncodedUtils.format(requestParams,
                HTTP.UTF_8);
        String sign = SHA1Util.HmacSHA1EncryptByte(strSign.toString() + appId
                + appKey, appKey);
        XGLog.d("before sign:" + strSign.toString());
        XGLog.d("after sign:" + sign);
        // 生成请求
        StringBuilder getUrl = new StringBuilder();
        getUrl.append(XGInfo.getXGRechargeUrl(activity)).append(uri)
                .append("/").append(channelId).append("/").append(appId)
                .append("?");
        getUrl.append(requestContent);
        getUrl.append("&sign=").append(sign);
        return getUrl;

    }

    public static void updateOrder(final Activity activity,
            final String orderId, final String uId, final String productId,
            final String productName, final String productDec,
            final String amount, final String totalPrice,
            final String serverId, final String zoneId, final String roleId,
            final String roleName, final String currencyName,
            final String payExt, final String gameOrderId,
            final String notifyUrl) throws Exception {
        updateOrder(activity, XGInfo.getXGAppId(activity),
                XGInfo.getXGAppKey(activity), XGInfo.getChannelId(), orderId,
                uId, productId, productName, productDec, amount, totalPrice,
                serverId, zoneId, roleId, roleName, currencyName, payExt,
                gameOrderId, notifyUrl);
    }

    /**
     * 通知服务端取消订单
     * 
     * @param orderId
     * @return
     */
    private static void cancelOrder(Activity activity, final String appId,
            final String appKey, final String orderId) throws Exception {
        String strSign = "orderId=" + orderId;
        String sign = MD5Util.md5(strSign + appId + appKey);
        XGLog.d("before sign:" + strSign);
        XGLog.d("after sign:" + sign);
        // 生成请求
        StringBuilder getUrl = new StringBuilder();
        getUrl.append(XGInfo.getXGRechargeUrl(activity))
                .append(PAY_CANCEL_ORDER_URI).append("/")
                .append(XGInfo.getChannelId()).append("/").append(appId)
                .append("?");
        getUrl.append(strSign);
        getUrl.append("&sign=").append(sign);
        // 发送请求
        String result = HttpUtils.executeHttpGet(getUrl.toString());
        // 返回结果为空
        if (TextUtils.isEmpty(result)) {
            // 生成订单失败
            throw new Exception("request:" + getUrl.toString()
                    + ",response is null.");
        }
        JSONObject jsonResult = new JSONObject(result);
        if ("1".equals(jsonResult.getString("code"))) {
            return;
        } else {
            throw new Exception(jsonResult.getString("msg"));
        }
    }

    public static void cancelOrder(Activity activity, final String orderId)
            throws Exception {
        cancelOrder(activity, XGInfo.getXGAppId(activity),
                XGInfo.getXGAppKey(activity), orderId);
    }

    public static void refreshBalanceInThread(final Activity activity,
            final String appId, final String appKey, final String openid,
            final String openkey, final String pay_token, final String appid,
            final String pf, final String pfkey, final String serverId,
            final String sdkAppid, final String channelId) throws Exception {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PayService.refreshBalance(activity, appId, appKey, openid,
                            openkey, pay_token, appid, pf, pfkey, serverId,
                            sdkAppid, channelId);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    XGLog.e(ex.getMessage(), ex);
                }
            }
        });
        thread.start();
        thread.join(THREAD_JOIN_TIME_OUT);
    }

    private static void refreshBalance(final Activity activity,
            final String appId, final String appKey, String openid,
            String openkey, String pay_token, String appid, String pf,
            String pfkey, String serverId, String sdkAppid, String channelId)
            throws Exception {
        // 排序签名
        List<NameValuePair> requestParams = new ArrayList<NameValuePair>();
        requestParams.add(new BasicNameValuePair("openid", openid));
        requestParams.add(new BasicNameValuePair("openkey", openkey));
        if (!TextUtils.isEmpty(pay_token)) {
            requestParams.add(new BasicNameValuePair("pay_token", pay_token));
        }
        requestParams.add(new BasicNameValuePair("appid", appid));
        requestParams.add(new BasicNameValuePair("pf", pf));
        requestParams.add(new BasicNameValuePair("pfkey", pfkey));
        if (!TextUtils.isEmpty(serverId)) {
            requestParams.add(new BasicNameValuePair("zoneid", serverId));
        }
        requestParams.add(new BasicNameValuePair("sdkAppid", sdkAppid));
        requestParams.add(new BasicNameValuePair("channelId", channelId));
        Collections.sort(requestParams, new Comparator<NameValuePair>() {
            @Override
            public int compare(NameValuePair lhs, NameValuePair rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });
        // 生成MD5签名
        StringBuilder strSign = new StringBuilder();
        for (int i = 0; i < requestParams.size(); i++) {
            NameValuePair nvPair = requestParams.get(i);
            strSign.append(nvPair.getName()).append("=")
                    .append(nvPair.getValue());
            if (i < requestParams.size() - 1) {
                strSign.append("&");
            }
        }
        String requestContent = URLEncodedUtils.format(requestParams,
                HTTP.UTF_8);
        String sign = MD5Util.md5(strSign.toString() + appId + appKey);
        XGLog.d("before sign:" + strSign.toString());
        XGLog.d("after sign:" + sign);
        // 生成请求
        StringBuilder getUrl = new StringBuilder();
        getUrl.append(XGInfo.getXGRechargeUrl(activity))
                .append(PAY_REFRESHBALANCE_URI).append("/").append(channelId)
                .append("/").append(appId).append("?");
        getUrl.append(requestContent);
        getUrl.append("&sign=").append(sign);
        // 发送请求
        String result = HttpUtils.executeHttpGet(getUrl.toString());
        // 返回结果为空
        if (TextUtils.isEmpty(result)) {
            // 失败
            throw new Exception("request:" + getUrl.toString()
                    + ",response is null.");
        }
        if (!("0".equals(result))) {
            throw new Exception("refresh balance failed,request:"
                    + getUrl.toString());
        }
    }

    public static void refreshBalance(final Activity activity, String openid,
            String openkey, String pay_token, String appid, String pf,
            String pfkey, String serverId, String sdkAppid, String channelId)
            throws Exception {
        refreshBalance(activity, XGInfo.getXGAppId(activity),
                XGInfo.getXGAppKey(activity), openid, openkey, pay_token,
                appid, pf, pfkey, serverId, sdkAppid, channelId);
    }

    public static String verifyPay(Activity activity, String orderId) {

        if (TextUtils.isEmpty(orderId)) {
            orderId = PayService.orderId;
        }
        StringBuilder getUrl = new StringBuilder();
        // http://onsite.recharge.xgsdk.com:8180/xgsdk/apiXgsdkPay/verifyOrder/{channelId}/{sdkAppid}
        getUrl.append(XGInfo.getXGRechargeUrl(activity))
                .append(PAY_VERIFY_ORDER_URI).append("/")
                .append(XGInfo.getChannelId()).append("/")
                .append(XGInfo.getXGAppId(activity)).append("?orderId=")
                .append(orderId).append("&sign=");
        String ret = "";
        try {
            ret = HttpUtils.doGetInThread(getUrl.toString());
            XGLog.i(ret);
        } catch (Exception e) {
            XGLog.e("verify pay error:", e);
        }
        return ret;
    }

    // 单独线程运行方式
    public static String getResponseInThread(final String url) throws Exception {

        Callable<String> callable = new Callable<String>() {
            public String call() throws Exception {
                return PayService.getResponse(url);
            }
        };
        FutureTask<String> future = new FutureTask<String>(callable);
        Thread thread = new Thread(future);
        thread.start();
        thread.join(THREAD_JOIN_TIME_OUT);
        return future.get();
    }

    public static String getResponse(String url) throws Exception {

        // 发送请求
        String result = HttpUtils.executeHttpGet(url);
        // 返回结果为空
        if (TextUtils.isEmpty(result)) {
            // 生成订单失败
            throw new Exception("request:" + url + ",response is null.");
        }

        return result;
    }

}
