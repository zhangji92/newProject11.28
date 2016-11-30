package com.example.administrator.beijingplayer.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.administrator.beijingplayer.mode.KuaipaiMessage;
import com.example.administrator.beijingplayer.mode.Player;
import com.example.administrator.beijingplayer.mode.Shop;
import com.example.administrator.beijingplayer.mode.ThreePage;
import com.example.administrator.beijingplayer.mode.User;
import com.example.administrator.beijingplayer.mode.UserMessage;
import com.example.administrator.beijingplayer.mode.Validate;
import com.google.gson.Gson;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.io.File;
import java.io.FileNotFoundException;


/**
 * 连接工具
 */
public class HttpTools {

    private static HttpTools httpTools;

    private FinalHttp finalHttp;


    private HttpTools(){
        if(finalHttp==null){
            finalHttp = new FinalHttp();
        }
    }
    public static HttpTools getHttpTools(){
        if(httpTools==null){
            httpTools = new HttpTools();
        }
        return httpTools;
    }

    /**
     * 获取首页信息
     * @param handler
     * @param lat
     * @param lng
     * @param searcename
     */
    public void get6bu6Messege(final Handler handler, String lat, String lng, String searcename){
        String url = ModeCode.HOME+"&lat="+lat+"&lng="+lng;
        httpTools.get6bu6Messege(url, new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);

            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e(ModeCode.TAG, "-------???----: " +s);
                Gson gson = new Gson();
                Shop shop = gson.fromJson(s,Shop.class);

                Message message = new Message();
                message.what = ModeCode.HOME_WHAT;
                message.obj =shop;
                handler.sendMessage(message);
            }
        });
    }

    /**
     * 获取第二页信息
     * @param pageNumber
     * @param pageSize
     * @param city
     * @param lat
     * @param lng
     */
    public void getPlayerMessage(final Handler handler,String pageNumber,String pageSize,String city,String lat,String lng){
        String url =ModeCode.PLAYER;
        httpTools.get6bu6Messege(url, new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);

            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e(ModeCode.TAG, "-------player----: " +s);
                Gson gson = new Gson();
                Player player = gson.fromJson(s,Player.class);

                Message message = new Message();
                message.what = ModeCode.PLAYER_WHAT;
                message.obj =player;
                handler.sendMessage(message);
            }
        });
    }

    /**
     * 获取第三页信息
     * @param pageNumber
     * @param pageSize
     * @param lat
     * @param lng
     */
    public void getThreeMessage(final Handler handler,String pageNumber,String pageSize,String lat,String lng){
        String url =ModeCode.THREE+"&lat="+lat+"&lng="+lng;
        finalHttp.post(url, new AjaxCallBack<String>() {
            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e(ModeCode.TAG, "-------Three----: " +s);
                Gson gson = new Gson();
                ThreePage threePage = gson.fromJson(s,ThreePage.class);

                Message message = new Message();
                message.what = ModeCode.THREE_WHAT;
                message.obj =threePage;
                handler.sendMessage(message);
            }
        });
    }

    /**
     * 获取验证码信息
     * @param handler
     * @param mobile
     */
    public void getValidateMessage(final Handler handler,String mobile){
        String url =ModeCode.YANZHENG+"&mobile="+mobile;
        finalHttp.post(url, new AjaxCallBack<String>() {
            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e(ModeCode.TAG, "-------viladate----: " +s);
                Gson gson = new Gson();
                Validate validate = gson.fromJson(s,Validate.class);

                Message message = new Message();
                message.what = ModeCode.REGISTE_VALIDATE;
                message.obj =validate;
                handler.sendMessage(message);
            }
        });
    }

    /**
     * 注册信息
     * @param handler
     * @param username
     * @param passwd
     * @param code
     */
    public void getRegister(final Handler handler,String username,String passwd,String code){
        AjaxParams params = new AjaxParams();
        params.put("username",username);
        params.put("passwd",passwd );
        params.put("code", code);

        String url =ModeCode.REGISTER;
        finalHttp.post(url,params ,new AjaxCallBack<String>() {
            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e(ModeCode.TAG, "----getRegister-------: " +s);
                Gson gson = new Gson();
                Validate validate = gson.fromJson(s,Validate.class);

                Message message = new Message();
                message.what = ModeCode.REGISTER_WHAT;
                message.obj =validate;
                handler.sendMessage(message);

            }
        });

    }
    /**
     * 登录信息
     * @param handler
     * @param username
     * @param passwd
     */
    public void getLogin(final Handler handler,String username,String passwd){
        AjaxParams params = new AjaxParams();
        params.put("username",username);
        params.put("passwd",passwd );

        String url =ModeCode.LOGIN;
        finalHttp.post(url,params ,new AjaxCallBack<String>() {
            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e(ModeCode.TAG, "----getLogin-------: " +s);
                Gson gson = new Gson();
                User validate = gson.fromJson(s,User.class);

                Message message = new Message();
                message.what = ModeCode.LOGIN_WHAT;
                message.obj =validate;
                handler.sendMessage(message);

            }
        });

    }

    /***
     * 获取服务器资源
     * @param handler
     * @param photo
     */
    public void getPhoto(final Handler handler,File photo){
        AjaxParams params = new AjaxParams();
        try {
            params.put("pics",photo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String url =ModeCode.PHOTO;
        finalHttp.post(url,params ,new AjaxCallBack<String>() {
            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e(ModeCode.TAG, "----getLogin-------: " +s);
                Gson gson = new Gson();
                Validate validate = gson.fromJson(s,Validate.class);

                Message message = new Message();
                message.what = ModeCode.PHOTO_WHAT;
                message.obj =validate;
                handler.sendMessage(message);

            }
        });

    }

    /**
     * 修改个人信息
     * @param handler
     * @param userMessage
     */
    public void getSetResult(final Handler handler,UserMessage userMessage){
        AjaxParams params = new AjaxParams();
        params.put("token",userMessage.getToken());
        params.put("nickname",userMessage.getNickname());
        params.put("gender",userMessage.getGender());
        params.put("age",userMessage.getAge());
        params.put("address",userMessage.getAddress());
        params.put("pics",userMessage.getImgUrl());



        String url =ModeCode.SET;
        finalHttp.post(url,params ,new AjaxCallBack<String>() {
            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e(ModeCode.TAG, "----getLogin-------: " +s);
                Gson gson = new Gson();
                Validate validate = gson.fromJson(s,Validate.class);

                Message message = new Message();
                message.what = ModeCode.SET_WHAT;
                message.obj =validate;
                handler.sendMessage(message);

            }
        });

    }

    /**
     * 获取快拍列表
     * @param handler
     * @param pageNumber
     * @param pageSize
     */
    public void getKuaipai(final Handler handler,String pageNumber,String pageSize){

        String url =ModeCode.KUAIPAI;
        finalHttp.get(url ,new AjaxCallBack<String>() {
            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e(ModeCode.TAG, "----getLogin-------: " +s);
                Gson gson = new Gson();
                KuaipaiMessage validate = gson.fromJson(s,KuaipaiMessage.class);

                Message message = new Message();
                message.what = ModeCode.KUAIPAI_WHAT;
                message.obj =validate;
                handler.sendMessage(message);

            }
        });

    }

    /***
     * get请求服务器数据
     * @param handler
     * @param msg
     * @param <T>
     */
    public <T> void getServiceMessage(final Handler handler,final ServiceMessage<T> msg){
        finalHttp.get(msg.getUrl() ,new AjaxCallBack<String>() {
            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e(ModeCode.TAG, "----getLogin-------: " +s);
                Gson gson = new Gson();
                T t = (T) gson.fromJson(s, msg.getT().getClass());

                Message message = new Message();
                message.what = msg.getWhat();
                message.obj =t;
                handler.sendMessage(message);
            }
        });
    }

    /***
     * post请求获取服务器数据
     * @param handler
     * @param msg
     * @param params
     * @param <T>
     */
    public <T> void postServiceMessage(final Handler handler,final ServiceMessage<T> msg,AjaxParams params){
        finalHttp.post(msg.getUrl(),params ,new AjaxCallBack<String>() {
            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e(ModeCode.TAG, "----getLogin-------: " +s);
                Gson gson = new Gson();
                T t = (T) gson.fromJson(s, msg.getT().getClass());

                Message message = new Message();
                message.what = msg.getWhat();
                message.obj =t;
                handler.sendMessage(message);

            }
        });
    }
    /**
     * 获取数据
     */
    public void get6bu6Messege(String url,AjaxCallBack<String> ajaxCallBack){
        finalHttp.get(url,ajaxCallBack);
    }

}
