package com.example.fluttermipushdemo;

/**
 * Created by Administrator on 2018/6/11.
 */

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.util.List;

/**
 * Created by Carson_Ho on 16/10/26.
 */

public class Mipush_Broadcast extends PushMessageReceiver {

    private String mRegId;

    //透传消息到达客户端时调用
    //作用：可通过参数message从而获得透传消息，具体请看官方SDK文档
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {

        //打印消息方便测试
        System.out.println("透传消息到达了");
        System.out.println("透传消息是"+message.toString());

    }


//通知消息到达客户端时调用
    //注：应用在前台时不弹出通知的通知消息到达客户端时也会回调函数
    //作用：通过参数message从而获得通知消息，具体请看官方SDK文档

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        //打印消息方便测试
        System.out.println("通知消息到达了");
        System.out.println("通知消息是"+message.toString());
    }

    //用户手动点击通知栏消息时调用
    //注：应用在前台时不弹出通知的通知消息到达客户端时也会回调函数
    //作用：1. 通过参数message从而获得通知消息，具体请看官方SDK文档
    //2. 设置用户点击消息后打开应用 or 网页 or 其他页面

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {

        //打印消息方便测试
        System.out.println("用户点击了通知消息");
        System.out.println("通知消息是" + message.toString());
        System.out.println("点击后,会进入应用" );
        // 启动应用

        try {
            ComponentName com = new ComponentName(context.getPackageName(), "com.xiaolaohr.xlzhaopin.MyMainActivity");
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(com);
            if (message != null) {
                intent.putExtras(message.toString()); // 把Bundle塞入Intent里面
            }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //用来接收客户端向服务器发送命令后的响应结果。
    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {

        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                Log.d("xmg", "注册成功-====================@@@@@@@@@@=--"+mRegId);
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
            }
        }
    }

    //用于接收客户端向服务器发送注册命令后的响应结果。
    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {

        String command = message.getCommand();
        System.out.println(command );

        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {

                //打印日志：注册成功
                System.out.println("注册成功");
            } else {
                //打印日志：注册失败
                System.out.println("注册失败");
            }
        } else {
            System.out.println("其他情况"+message.getReason());
        }
    }

}
