
package com.xgsdk.client.api.cocos2dx;

import com.xgsdk.client.api.XGSDK;
import com.xgsdk.client.api.callback.ExitCallBack;
import com.xgsdk.client.api.callback.PayCallBack;
import com.xgsdk.client.api.callback.UserCallBack;
import com.xgsdk.client.api.entity.GameServerInfo;
import com.xgsdk.client.api.entity.PayInfo;
import com.xgsdk.client.api.entity.RoleInfo;
import com.xgsdk.client.api.entity.XGUser;
import com.xgsdk.client.core.XGInfo;
import com.xgsdk.client.core.XGInfo.GAME_ENGINE;
import com.xgsdk.client.core.utils.ToastUtil;
import com.xgsdk.client.core.utils.XGLog;

import org.cocos2dx.lib.Cocos2dxGLSurfaceView;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Iterator;

public class XGSDKCocos2dxWrapper {
    private static final String LOG_TAG = "XGCocos2dxWrapper";

    private XGSDK mSdk;

    private static XGSDKCocos2dxWrapper sInstance;

    private Activity mActivity;

    void init(Activity activity) {
        mActivity = activity;
        mSdk.init(activity);
        Cocos2dxPluginWrapper.init(activity);
        Cocos2dxPluginWrapper.setGLSurfaceView(Cocos2dxGLSurfaceView
                .getInstance());
    }

    private XGSDKCocos2dxWrapper() {
        XGInfo.setGameEngine(GAME_ENGINE.COCOS2DX);
        mSdk = XGSDK.getInstance();
        mSdk.setUserCallBack(new UserCallBack() {

            @Override
            public void onLogoutSuccess(final String msg) {
                Cocos2dxPluginWrapper.runOnGLThread(new Runnable() {

                    @Override
                    public void run() {
                        Cocos2dxUserCallBack.onLogoutSuccess(msg);
                    }

                });
            }

            @Override
            public void onLogoutFail(final int code, final String msg) {
                Cocos2dxPluginWrapper.runOnGLThread(new Runnable() {

                    @Override
                    public void run() {
                        Cocos2dxUserCallBack.onLogoutFail(code, msg);
                    }

                });
            }

            @Override
            public void onLoginSuccess(final String authInfo) {
                Cocos2dxPluginWrapper.runOnGLThread(new Runnable() {

                    @Override
                    public void run() {
                        Cocos2dxUserCallBack.onLoginSuccess(authInfo);
                    }

                });
            }

            @Override
            public void onLoginFail(final int code, final String msg) {
                Cocos2dxPluginWrapper.runOnGLThread(new Runnable() {

                    @Override
                    public void run() {
                        Cocos2dxUserCallBack.onLoginFail(code, msg);
                    }

                });
            }

            @Override
            public void onInitFail(final int code, final String msg) {
                Cocos2dxPluginWrapper.runOnGLThread(new Runnable() {

                    @Override
                    public void run() {
                        Cocos2dxUserCallBack.onInitFail(code, msg);
                    }

                });
            }

            @Override
            public void onLoginCancel(final String msg) {
                Cocos2dxPluginWrapper.runOnGLThread(new Runnable() {

                    @Override
                    public void run() {
                        Cocos2dxUserCallBack.onLoginCancel(msg);
                    }

                });
            }
        });
    }

    public static XGSDKCocos2dxWrapper getInstance() {
        if (sInstance == null) {
            synchronized (XGSDKCocos2dxWrapper.class) {
                if (sInstance == null) {
                    sInstance = new XGSDKCocos2dxWrapper();
                }
            }
        }

        return sInstance;
    }

    public String getChannelId() {
        return mSdk.getChannelId();
    }

    /**
     * 登录
     */
    public void login(final String customParams) {
        XGLog.i(LOG_TAG, "login");
        Cocos2dxPluginWrapper.runOnMainThread(new Runnable() {

            @Override
            public void run() {
                mSdk.login(mActivity, customParams);
            }

        });
    }

    public void pay(final String sdkUid, final String appGoodsId,
            final String appGoodsName, final String appGoodsDesc,
            final int appGoodsAmount, final String appGoodsUnit,
            final int totalPrice, final int originalPrice,
            final String currencyName, final String custom,
            final String gameTradeNo, final String gameCallbackUrl,
            final String serverId, final String serverName,
            final String zoneId, final String zoneName, final String roleId,
            final String roleName, final int level, final int vipLevel) {
        XGLog.i(LOG_TAG, "pay");
        Cocos2dxPluginWrapper.runOnMainThread(new Runnable() {

            @Override
            public void run() {
                final PayInfo payment = new PayInfo();
                payment.setSdkUid(sdkUid);
                payment.setAppGoodsId(appGoodsId);
                payment.setAppGoodsName(appGoodsName);
                payment.setAppGoodsDesc(appGoodsDesc);
                payment.setAppGoodsAmount(appGoodsAmount);
                payment.setAppGoodsUnit(appGoodsUnit);
                payment.setTotalPrice(totalPrice);
                payment.setOriginalPrice(originalPrice);
                payment.setCurrencyName(currencyName);
                payment.setCustom(custom);
                payment.setGameTradeNo(gameTradeNo);
                payment.setGameCallbackUrl(gameCallbackUrl);
                payment.setServerId(serverId);
                payment.setServerName(serverName);
                payment.setZoneId(zoneId);
                payment.setZoneName(zoneName);
                payment.setRoleId(roleId);
                payment.setRoleName(roleName);
                payment.setLevel(level);
                payment.setVipLevel(vipLevel);
                mSdk.pay(mActivity, payment, new PayCallBack() {

                    @Override
                    public void onSuccess(final String msg) {
                        Cocos2dxPluginWrapper.runOnGLThread(new Runnable() {

                            @Override
                            public void run() {
                                Cocos2dxPayCallBack.onSuccess(msg);
                            }
                        });
                    }

                    @Override
                    public void onFail(final int code, final String msg) {
                        Cocos2dxPluginWrapper.runOnGLThread(new Runnable() {

                            @Override
                            public void run() {
                                Cocos2dxPayCallBack.onFail(code, msg);
                            }
                        });
                    }

                    @Override
                    public void onCancel(final String msg) {
                        Cocos2dxPluginWrapper.runOnGLThread(new Runnable() {

                            @Override
                            public void run() {
                                Cocos2dxPayCallBack.onCancel(msg);
                            }
                        });
                    }

                    @Override
                    public void onOthers(final int code, final String msg) {
                        Cocos2dxPluginWrapper.runOnGLThread(new Runnable() {

                            @Override
                            public void run() {
                                Cocos2dxPayCallBack.onOthers(code, msg);
                            }
                        });
                    }

                });
            }

        });
    }

    public void exit(final String customParams) {
        XGLog.i(LOG_TAG, "exit");
        Cocos2dxPluginWrapper.runOnMainThread(new Runnable() {

            @Override
            public void run() {
                mSdk.exit(mActivity, new ExitCallBack() {

                    @Override
                    public void onExit() {
                        Cocos2dxPluginWrapper.runOnGLThread(new Runnable() {

                            @Override
                            public void run() {
                                // 调用渠道退出窗口后的回调
                                Cocos2dxExitCallBack.onExit();
                            }
                        });

                    }

                    @Override
                    public void onNoChannelExiter() {
                        Cocos2dxPluginWrapper.runOnGLThread(new Runnable() {

                            @Override
                            public void run() {
                                // 需要游戏自身弹出退出窗口
                                Cocos2dxExitCallBack.onNoChannelExiter();
                            }
                        });
                    }

                    @Override
                    public void onCancel() {
                        Cocos2dxPluginWrapper.runOnGLThread(new Runnable() {

                            @Override
                            public void run() {
                                Cocos2dxExitCallBack.onCancel();
                            }
                        });
                    }

                }, customParams);
            }
        });
    }

    public void logout(final String customParams) {
        XGLog.i(LOG_TAG, "logout");
        Cocos2dxPluginWrapper.runOnMainThread(new Runnable() {

            @Override
            public void run() {
                mSdk.logout(mActivity, customParams);
            }

        });
    }

    public void switchAccount(final String customParams) {
        XGLog.i(LOG_TAG, "switchAccount");
        Cocos2dxPluginWrapper.runOnMainThread(new Runnable() {

            @Override
            public void run() {
                mSdk.switchAccount(mActivity, customParams);
            }

        });
    }

    /**
     * 进入游戏后向渠道传递用户信息
     */
    public void onEnterGame(String uid, String username, String roleId,
            String roleName, String gender, int level, int vipLevel,
            String balance, String partyName, String serverId, String serverName) {
        XGLog.i(LOG_TAG, "login success,tell userinfo to sdk");
        XGUser userInfo = new XGUser();
        userInfo.setUserName(username);
        userInfo.setUid(uid);
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setRoleId(roleId);
        roleInfo.setRoleName(roleName);
        roleInfo.setGender(gender);
        roleInfo.setLevel(level);
        roleInfo.setVipLevel(vipLevel);
        roleInfo.setBalance(balance);
        roleInfo.setPartyName(partyName);
        GameServerInfo gameInfo = new GameServerInfo();
        gameInfo.setServerId(serverId);
        gameInfo.setServerName(serverName);
        mSdk.onEnterGame(mActivity, userInfo, roleInfo, gameInfo);
    }

    public void onCreateRole(String roleId, String roleName, String gender,
            int level, int vipLevel, String balance, String partyName) {
        XGLog.i(LOG_TAG, "onCreateRole");
        RoleInfo info = new RoleInfo();
        info.setBalance(balance);
        info.setGender(gender);
        info.setLevel(level);
        info.setPartyName(partyName);
        info.setRoleId(roleId);
        info.setRoleName(roleName);
        info.setVipLevel(vipLevel);
        mSdk.onCreateRole(mActivity, info);
    }

    public void onRoleLevelup(String uid, String username, String roleId,
            String roleName, String gender, int level, int vipLevel,
            String balance, String partyName, String serverId, String serverName) {
        XGLog.i(LOG_TAG, "onRoleLevelup");
        XGUser userInfo = new XGUser();
        userInfo.setUserName(username);
        userInfo.setUid(uid);
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setRoleId(roleId);
        roleInfo.setRoleName(roleName);
        roleInfo.setGender(gender);
        roleInfo.setLevel(level);
        roleInfo.setVipLevel(vipLevel);
        roleInfo.setBalance(balance);
        roleInfo.setPartyName(partyName);
        GameServerInfo gameInfo = new GameServerInfo();
        gameInfo.setServerId(serverId);
        gameInfo.setServerName(serverName);
        mSdk.onRoleLevelup(mActivity, userInfo, roleInfo, gameInfo);
    }

    /**
     * 访问用户中心
     */
    public void openUserCenter(final String customParams) {
        XGLog.i(LOG_TAG, "openUserCenter");
        Cocos2dxPluginWrapper.runOnMainThread(new Runnable() {

            @Override
            public void run() {
                mSdk.openUserCenter(mActivity, customParams);
            }
        });
    }

    /**
     * @Title: onEvent
     * @Description: 自定义事件
     * @param: @param uid
     * @param: @param username
     * @param: @param roleId
     * @param: @param roleName
     * @param: @param gender
     * @param: @param level
     * @param: @param vipLevel
     * @param: @param balance
     * @param: @param partyName
     * @param: @param serverId
     * @param: @param serverName
     * @param: @param eventId 事件id
     * @param: @param eventDesc 事件描述
     * @param: @param eventVal 事件值
     * @param: @param eventBody 事件内容 必须是json格式
     * @param: @param customParams 扩展字段 必须是json格式
     * @return: void
     * @throws
     */
    public void onEvent(String uid, String username, String roleId,
            String roleName, String gender, int level, int vipLevel,
            String balance, String partyName, String serverId,
            String serverName, String eventId, String eventDesc, int eventVal,
            String eventBody, String customParams) {
        XGUser userInfo = new XGUser();
        userInfo.setUserName(username);
        userInfo.setUid(uid);
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setRoleId(roleId);
        roleInfo.setRoleName(roleName);
        roleInfo.setGender(gender);
        roleInfo.setLevel(level);
        roleInfo.setVipLevel(vipLevel);
        roleInfo.setBalance(balance);
        roleInfo.setPartyName(partyName);
        GameServerInfo gameInfo = new GameServerInfo();
        gameInfo.setServerId(serverId);
        gameInfo.setServerName(serverName);
        HashMap<String, Object> bodyMap = new HashMap<String, Object>();
        if (!TextUtils.isEmpty(eventBody)) {
            try {
                JSONObject body = new JSONObject(eventBody);
                Iterator<String> keys = body.keys();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    bodyMap.put(key, body.opt(key));
                }
            } catch (JSONException e) {
                XGLog.e("eventBody is invalid." + eventBody, e);
            }
        }
        mSdk.onEvent(mActivity, userInfo, roleInfo, gameInfo, eventId,
                eventDesc, eventVal, bodyMap, customParams);

    }

    /**
     * @Title: onMissionBegin
     * @Description: 任务开始
     * @param: @param uid
     * @param: @param username
     * @param: @param roleId
     * @param: @param roleName
     * @param: @param gender
     * @param: @param level
     * @param: @param vipLevel
     * @param: @param balance
     * @param: @param partyName
     * @param: @param serverId
     * @param: @param serverName
     * @param: @param activity
     * @param: @param missionName 任务名称
     * @param: @param customParams 扩展参数，必须是json
     * @return: void
     * @throws
     */
    public void onMissionBegin(String uid, String username, String roleId,
            String roleName, String gender, int level, int vipLevel,
            String balance, String partyName, String serverId,
            String serverName, String missionName, String customParams) {
        XGUser userInfo = new XGUser();
        userInfo.setUserName(username);
        userInfo.setUid(uid);
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setRoleId(roleId);
        roleInfo.setRoleName(roleName);
        roleInfo.setGender(gender);
        roleInfo.setLevel(level);
        roleInfo.setVipLevel(vipLevel);
        roleInfo.setBalance(balance);
        roleInfo.setPartyName(partyName);
        GameServerInfo serverInfo = new GameServerInfo();
        serverInfo.setServerId(serverId);
        serverInfo.setServerName(serverName);
        mSdk.onMissionBegin(mActivity, userInfo, roleInfo, serverInfo,
                missionName, customParams);

    }

    /**
     * @Title: onMissionSuccess
     * @Description: 任务成功
     * @param: @param uid
     * @param: @param username
     * @param: @param roleId
     * @param: @param roleName
     * @param: @param gender
     * @param: @param level
     * @param: @param vipLevel
     * @param: @param balance
     * @param: @param partyName
     * @param: @param serverId
     * @param: @param serverName
     * @param: @param activity
     * @param: @param missionName 任务名称
     * @param: @param customParams 扩展参数，必须是json
     * @return: void
     * @throws
     */
    public void onMissionSuccess(String uid, String username, String roleId,
            String roleName, String gender, int level, int vipLevel,
            String balance, String partyName, String serverId,
            String serverName, String missionName, String customParams) {
        XGUser userInfo = new XGUser();
        userInfo.setUserName(username);
        userInfo.setUid(uid);
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setRoleId(roleId);
        roleInfo.setRoleName(roleName);
        roleInfo.setGender(gender);
        roleInfo.setLevel(level);
        roleInfo.setVipLevel(vipLevel);
        roleInfo.setBalance(balance);
        roleInfo.setPartyName(partyName);
        GameServerInfo serverInfo = new GameServerInfo();
        serverInfo.setServerId(serverId);
        serverInfo.setServerName(serverName);
        mSdk.onMissionSuccess(mActivity, userInfo, roleInfo, serverInfo,
                missionName, customParams);

    }

    /**
     * @Title: onMissionFail
     * @Description: 任务失败
     * @param: @param uid
     * @param: @param username
     * @param: @param roleId
     * @param: @param roleName
     * @param: @param gender
     * @param: @param level
     * @param: @param vipLevel
     * @param: @param balance
     * @param: @param partyName
     * @param: @param serverId
     * @param: @param serverName
     * @param: @param activity
     * @param: @param missionName 任务名称
     * @param: @param customParams 扩展参数，必须是json
     * @return: void
     * @throws
     */
    public void onMissionFail(String uid, String username, String roleId,
            String roleName, String gender, int level, int vipLevel,
            String balance, String partyName, String serverId,
            String serverName, String missionName, String customParams) {
        XGUser userInfo = new XGUser();
        userInfo.setUserName(username);
        userInfo.setUid(uid);
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setRoleId(roleId);
        roleInfo.setRoleName(roleName);
        roleInfo.setGender(gender);
        roleInfo.setLevel(level);
        roleInfo.setVipLevel(vipLevel);
        roleInfo.setBalance(balance);
        roleInfo.setPartyName(partyName);
        GameServerInfo serverInfo = new GameServerInfo();
        serverInfo.setServerId(serverId);
        serverInfo.setServerName(serverName);
        mSdk.onMissionFail(mActivity, userInfo, roleInfo, serverInfo,
                missionName, customParams);

    }

    /**
     * 当前渠道是否提供该接口
     * 
     * @param methodName
     * @return
     */
    public boolean isMethodSupport(String methodName) {
        return mSdk.isMethodSupport(methodName);
    }

    public void showAndroidToast(final String msg) {
        Cocos2dxPluginWrapper.runOnMainThread(new Runnable() {

            @Override
            public void run() {
                ToastUtil.showToast(mActivity, msg);
            }
        });
    }
}
