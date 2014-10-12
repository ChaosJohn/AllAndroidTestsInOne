package com.luda.mymodule.testumeng;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;


public class MainActivity extends Activity {

    final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mController = UMServiceFactory.getUMSocialService("com.umeng.share");

        // QQ support
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity)getActivity(), "100424468", "c7394704798a158208a74ab60104f0ba");
        qqSsoHandler.addToSocialSDK();

        // Wechat support
        String appID = "wx967daebe835fbeac";
        String appSecret = "5fa9e68ca3970e87a1f83e563c8dcbce";
        UMWXHandler wxHandler = new UMWXHandler(getActivity(),appID,appSecret);
        wxHandler.addToSocialSDK();
        UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(),appID,appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();


        findViewById(R.id.btn_sina).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mController.setShareContent("PaiXin");
//                mController.openShare(getActivity(), false);
                mController.postShare(getActivity(),SHARE_MEDIA.SINA,
                        new SocializeListeners.SnsPostListener() {
                            @Override
                            public void onStart() {
                                Toast.makeText(getActivity(), "开始分享.", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
                                if (eCode == 200) {
                                    Toast.makeText(getActivity(), "分享成功.", Toast.LENGTH_SHORT).show();
                                } else {
                                    String eMsg = "";
                                    if (eCode == -101){
                                        eMsg = "没有授权";
                                    }
                                    Toast.makeText(getActivity(), "分享失败[" + eCode + "] " +
                                            eMsg,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        findViewById(R.id.btn_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QQShareContent qqShareContent = new QQShareContent();
                qqShareContent.setShareContent("PaiXin");
                qqShareContent.setTargetUrl("http://www.eput.com/paixin");
                qqShareContent.setTitle("Welcome To Paixin");
                mController.setShareMedia(qqShareContent);
                mController.postShare(getActivity(),SHARE_MEDIA.QQ,
                        new SocializeListeners.SnsPostListener() {
                            @Override
                            public void onStart() {
                                Toast.makeText(getActivity(), "开始分享.", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
                                if (eCode == 200) {
                                    Toast.makeText(getActivity(), "分享成功.", Toast.LENGTH_SHORT).show();
                                } else {
                                    String eMsg = "";
                                    if (eCode == -101){
                                        eMsg = "没有授权";
                                    }
                                    Toast.makeText(getActivity(), "分享失败[" + eCode + "] " +
                                            eMsg,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        findViewById(R.id.btn_wechat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeiXinShareContent weixinContent = new WeiXinShareContent();
                weixinContent.setShareContent("PaiXin");
                weixinContent.setTitle("Welcome To Paixin");
                weixinContent.setTargetUrl("http://www.eput.com/paixin");
//                weixinContent.setShareImage(localImage);
                mController.setShareMedia(weixinContent);
                mController.postShare(getActivity(),SHARE_MEDIA.WEIXIN,
                        new SocializeListeners.SnsPostListener() {
                            @Override
                            public void onStart() {
                                Toast.makeText(getActivity(), "开始分享.", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
                                if (eCode == 200) {
                                    Toast.makeText(getActivity(), "分享成功.", Toast.LENGTH_SHORT).show();
                                } else {
                                    String eMsg = "";
                                    if (eCode == -101){
                                        eMsg = "没有授权";
                                    }
                                    Toast.makeText(getActivity(), "分享失败[" + eCode + "] " +
                                            eMsg,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        findViewById(R.id.btn_moments).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CircleShareContent circleContent = new CircleShareContent();
                circleContent.setShareContent("PaiXin");
                circleContent.setTitle("Welcome To Paixin");
                circleContent.setTargetUrl("http://www.eput.com/paixin");
//                circleContent.setShareImage(localImage);
                mController.setShareMedia(circleContent);
                mController.postShare(getActivity(),SHARE_MEDIA.WEIXIN,
                        new SocializeListeners.SnsPostListener() {
                            @Override
                            public void onStart() {
                                Toast.makeText(getActivity(), "开始分享.", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
                                if (eCode == 200) {
                                    Toast.makeText(getActivity(), "分享成功.", Toast.LENGTH_SHORT).show();
                                } else {
                                    String eMsg = "";
                                    if (eCode == -101){
                                        eMsg = "没有授权";
                                    }
                                    Toast.makeText(getActivity(), "分享失败[" + eCode + "] " +
                                            eMsg,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private Context getActivity() { return MainActivity.this; }

}
