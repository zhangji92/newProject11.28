package com.example.lenovo.amuse.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.lenovo.amuse.mode.LovePlayMode;
import com.example.lenovo.amuse.mode.WineMode;
import com.google.gson.Gson;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2016/9/26.
 * 网络数据
 */

public class HttpTools {

    static FinalHttp finalHttp;
    static HttpTools httpTools = null;

    //初始化网路连接
    private HttpTools() {
        finalHttp = new FinalHttp();
    }

    /**
     * 单例模式
     *
     * @return
     */
    public static HttpTools getInstance() {
        if (httpTools == null) {
            httpTools = new HttpTools();
        }
        return httpTools;
    }

    /**
     * 首页数据
     * lat N string 维度
     * lng N string 经度
     * searcename N string 检索
     * <p>
     * 同城爱玩
     * pageNumber 是 string 页数
     * pageSize   是 string 每页条数
     * city       是 string 城市
     * lat        是 string 维度
     * lng        是 string 精度
     * flags 标识符
     * <p>
     * 手机验证码
     * mobile 是 string 手机号
     * 快拍列表接口
     * * pageNumber 是 string 页数
     * pageSize   是 string 每页条数
     */
    public void getDate(final Handler handler, String lat, String lng, String mobile, String searcename, String pageNumber, String pageSize, String city, final int flags) {
        String url = "";
        if (flags == 1) {
            //首页
            url = BaseUri.HOME + "&lat=" + lat + "&lng=" + lng;//"&searcename="+searcename
        } else if (flags == 2) {
            //同城爱玩
            url = BaseUri.LOVE_PLAY + "&lat=" + lat + "&lng=" + lng;//+"&pageNumber="+pageNumber+"&pageSize="+pageSize+"&city="+city
        } else if (flags == 3) {
            //获取验证码
            url = BaseUri.REGISTER_CODE + "&mobile=" + mobile;
        } else if (flags == 4) {
            //快拍列表
            url = BaseUri.SNAPSHORT + "&pageNumber=" + pageNumber + "&pageSize=" + pageSize;
        }
        finalHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);

                Message message = new Message();
                if (flags == 1) {
                    message.what = BaseUri.FIRSTCODE;
                    message.obj = JsonParserTools.parserMode(s, 1);
                } else if (flags == 2) {
                    message.what = BaseUri.LOVE_PLAY_CODE;
                    message.obj = JsonParserTools.parserMode(s, 2);
                } else if (flags == 3) {
                    message.what = BaseUri.PHONE;
                    message.obj = JsonParserTools.parserMode(s, 4);
                } else if (flags == 4) {//快拍列表
                    message.what = BaseUri.SNAP;
                    message.obj = JsonParserTools.parserMode(s, 7);
                }
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    /**
     * 优惠专区
     * pageNumber 是 string 页号
     * pageSize 是 string 条数
     * lat 是 string 维度
     * lng 是 string 精度
     * <p>
     */
    public void postData(final Handler handler, String lat, String lng, String pageNumber, String pageSize) {

        String url = BaseUri.PREFERENTIAL + "&lat=" + lat + "&lng=" + lng;//+"&pageNumber="+pageNumber+"&pageSize="+pageSize;

        finalHttp.post(url, new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Message message = new Message();
                message.obj = JsonParserTools.parserMode(s, 3);
                message.what = BaseUri.PRE;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    /**
     * 注册
     * username 是 string 用户名
     * passwd 是 string 密码
     * code 是 string 验证码
     * <p>
     * 登陆
     * username 是 string 用户名
     * passwd 是 string 密码
     */
    public void postLogin(final Handler handler, String username, String passwd, String code, final int flags) {
        String url = "";
        final Map<String, String> map = new HashMap<>();
        if (flags == 1) {//注册
            url = BaseUri.REGISTER;
            map.put("username", username);
            map.put("passwd", passwd);
            map.put("code", code);
        } else if (flags == 2) {//登陆
            url = BaseUri.LOGIN;
            map.put("username", username);
            map.put("passwd", passwd);
        }
        finalHttp.post(url, new AjaxParams(map), new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Message message = new Message();
                if (flags == 1) {
                    message.obj = JsonParserTools.parserMode(s, 5);
                    message.what = BaseUri.REGISTER_SUCCESS;
                } else if (flags == 2) {
                    message.obj = s;
                    message.what = BaseUri.LOGIN_SUCCESS;
                }
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }
    /**
     * token 是string 用户token
     nickname 是 string 昵称
     gender 否 string 性别 1男 2女
     age 否 string 年龄
     address 否 string 地址
     pics 否 string 头像地址
     */
    /**
     * pageNumber 是 string 页码
     * pageSize 是 string 条数
     */
    public void postMessage(final Handler mHandler, String token, String nickname, String gender, String age, String address, String pics) {

        String uri = BaseUri.USER;
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("token", token);
        ajaxParams.put("nickname", nickname);
        ajaxParams.put("gender", gender);
        ajaxParams.put("age", age);
        ajaxParams.put("address", address);
        ajaxParams.put("pics", pics);

        finalHttp.post(uri, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Message message = new Message();
                message.obj = JsonParserTools.parserMode(s, 4);
                message.what = BaseUri.MESSAGE_CODE;
                mHandler.sendMessage(message);
                Log.i("getFirstDate", "----BaseUri.PLACE-------: " + s);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }


    /**
     * pageNumber 是 string 页码
     * pageSize 是 string 条数
     * <p>
     * 图片
     * pics 是 string 页码
     */
    public void postWine(final Handler mHandler, String pageNumber, String pageSize, File pics, final int flags) {
        String uri = "";
        AjaxParams ajaxParams = new AjaxParams();
        if (flags == 1) {
            uri = BaseUri.WINE;
            ajaxParams.put("pageNumber", pageNumber);
            ajaxParams.put("pageSize", pageSize);
        } else if (flags == 2) {
            uri = BaseUri.PIC;
            try {
                ajaxParams.put("pics", pics);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        finalHttp.post(uri, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Message message = new Message();
                if (flags == 1) {
                    message.obj = JsonParserTools.parserMode(s, 8);
                    message.what = BaseUri.WINE_CODE;
                } else if (flags == 2) {
                    message.obj = JsonParserTools.parserMode(s, 4);
                    message.what = BaseUri.PIC_CODE;
                }
                mHandler.sendMessage(message);

            }
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }


    /**
     *
     * pageNumber 是 string 页数
     * pageSize   是 string 每页条数
     * city       是 string 城市
     * lat        是 string 维度
     * lng        是 string 精度
     * flags 标识符
     * <p>
     * `酒水代理商家信息
     * pageNumber 是 string 页码
     * pageSize 是 string 条数
     * id 否 string 代理商id
     * <p>
     * `酒水代理商家详情信息
     * pageNumber 是 string 页码
     * pageSize 是 string 条数
     * id 否 string 代理商品id
     */
    public void getPlace(final Handler handler, String pageNumber, String lat, String lng, final String flag, String pageSize, String city, String id, final int flags) {
        String url = "";
        if (flags == 1) {
            //场所
            url = BaseUri.PLACE + "&lat=" + lat + "&lng=" + lng;//+"&pageNumber="+pageNumber+"&pageSize="+pageSize+"&city="+city
        } else if (flags == 2) {
            url = BaseUri.WINE_AGENT + "&pageNumber=" + pageNumber + "&pageSize=" + pageSize + "&id=" + id;
        } else if (flags == 3) {
            url = BaseUri.WINE_DETAILS + "&pageNumber=" + pageNumber + "&pageSize=" + pageSize + "&id=" + id;
        } else if (flags == 4) {
            url = BaseUri.SHOP_DETAILS + "&id=" + id;
        }

        finalHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Message message = new Message();
                if (flags == 1) {
                    message.obj = JsonParserTools.parserMode(s,14);
                    message.what = BaseUri.PLACE_CODE;

                } else if (flags == 2) {
                    message.what = BaseUri.WINE_AGENT_CODE;
                    message.obj = JsonParserTools.parserMode(s, 9);
                } else if (flags == 3) {
                    message.what = BaseUri.WINE_AGENT_DETAILS;
                    message.obj = JsonParserTools.parserMode(s, 10);
                } else if (flags == 4) {
                    message.what = BaseUri.SHOP_DETAILS_CODE;
                    message.obj = JsonParserTools.parserMode(s, 11);
                }
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    public <T> void getServiceMessage(final Handler handler, final ServiceMessage<T> msg) {

        finalHttp.get(msg.getUrl(), new AjaxCallBack<String>() {
            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.i("getFirstDate", "----getServiceMessage-------: " + s);
                Gson gson = new Gson();
                T t = (T) gson.fromJson(s, msg.getT().getClass());
                Message message = new Message();
                message.what = msg.getWhat();
                message.obj = t;
                handler.sendMessage(message);
            }
        });
    }

    public <T> void postServiceMessage(final Handler handler, final ServiceMessage<T> msg, AjaxParams params) {
        finalHttp.post(msg.getUrl(), params, new AjaxCallBack<String>() {
            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e("", "----getLogin-------: " + s);
                Gson gson = new Gson();
                T t = (T) gson.fromJson(s, msg.getT().getClass());

                Message message = new Message();
                message.what = msg.getWhat();
                message.obj = t;
                handler.sendMessage(message);
            }
        });
    }



}
