



# flutter集成推送功能-小米推送集成

创建一个名字为flutter_mipush_demo的flutter工程，Android的applicationId和packageName都为：com.example.fluttermipushdemo

## Android

### 应用注册配置

- 注意这里的application是继承自FlutterApplication

```java
public class MyApplication extends FlutterApplication {
    // 使用自己APP的ID（官网注册的）
    private static final String APP_ID = "00000000000000";
    // 使用自己APP的Key（官网注册的）
    private static final String APP_KEY = "000000000000";


    //为了提高推送服务的注册率，我建议在Application的onCreate中初始化推送服务
    //你也可以根据需要，在其他地方初始化推送服务
    @Override
    public void onCreate() {
        super.onCreate();


        if (shouldInit()) {
            //注册推送服务
            //注册成功后会向DemoMessageReceiver发送广播
            // 可以从DemoMessageReceiver的onCommandResult方法中MiPushCommandMessage对象参数中获取注册信息
            MiPushClient.registerPush(this, APP_ID, APP_KEY);
        }
    }

    //通过判断手机里的所有进程是否有这个App的进程
    //从而判断该App是否有打开
    private boolean shouldInit() {

        //通过ActivityManager我们可以获得系统里正在运行的activities
        //包括进程(Process)等、应用程序/包、服务(Service)、任务(Task)信息。
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();

        //获取本App的唯一标识
        int myPid = android.os.Process.myPid();
        //利用一个增强for循环取出手机里的所有进程
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            //通过比较进程的唯一标识和包名判断进程里是否存在该App
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
```

- ```
  android:name="com.example.fluttermipushdemo.MyApplication"
  ```

​       替换AndroidManifest.xml文件中的application如上；之前为：io.flutter.app.FlutterApplication

### 消息处理

```
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
        
         try {
            ComponentName com = new ComponentName(context.getPackageName(), "com.example.fluttermipushdemo.MainActivity");
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
```

 这部分和Android中一样

### Manifest文件的其他相关配置

- 权限

  ```
      <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
      <uses-permission android:name="android.permission.INTERNET" />
      <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
      <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
      <uses-permission android:name="android.permission.READ_PHONE_STATE" />
      <uses-permission android:name="android.permission.GET_TASKS" />
      <uses-permission android:name="android.permission.VIBRATE" />

      <!--//注意这里.permission.MIPUSH_RECEIVE是自身app的包名-->
      <permission android:name="com.example.fluttermipushdemo.permission.MIPUSH_RECEIVE" android:protectionLevel="signature" />

     <!-- //注意这里.permission.MIPUSH_RECEIVE是自身app的包名-->
      <uses-permission android:name="com.example.fluttermipushdemo.permission.MIPUSH_RECEIVE" />
  ```

- service 和receiver.

```
//4个后台服务-->
        <service
            android:enabled="true"
            android:process=":pushservice"
            android:name="com.xiaomi.push.service.XMPushService"/>

       <!-- //此service必须在3.0.1版本以后（包括3.0.1版本）加入-->
        <service
            android:name="com.xiaomi.push.service.XMJobService"
            android:enabled="true"
            android:exported="false"
            android:permission="android.permission.BIND_JOB_SERVICE"
            android:process=":pushservice" />

        <!--//此service必须在2.2.5版本以后（包括2.2.5版本）加入-->
        <service
            android:enabled="true"
            android:exported="true"
            android:name="com.xiaomi.mipush.sdk.PushMessageHandler" />

        <service android:enabled="true"
            android:name="com.xiaomi.mipush.sdk.MessageHandleService" />


       <!-- //3个广播-->
        <receiver
            android:exported="true"
            android:name="com.xiaomi.push.service.receivers.NetworkStatusReceiver" >
            <intent-filter>
                <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
                <category android:name="android.intent.category.DEFAULT" />
            </intent-filter>
        </receiver>

        <receiver
            android:exported="false"
            android:process=":pushservice"
            android:name="com.xiaomi.push.service.receivers.PingReceiver" >
            <intent-filter>
                <action android:name="com.xiaomi.push.PING_TIMER" />
            </intent-filter>
        </receiver>

        <!--//继承了PushMessageReceiver的DemoMessageReceiver的广播注册-->
        <receiver
            android:name=".Mipush_Broadcast"
            android:exported="true">
            <intent-filter>
                <action android:name="com.xiaomi.mipush.RECEIVE_MESSAGE" />
            </intent-filter>
            <intent-filter>
                <action android:name="com.xiaomi.mipush.MESSAGE_ARRIVED" />
            </intent-filter>
            <intent-filter>
                <action android:name="com.xiaomi.mipush.ERROR" />
            </intent-filter>
        </receiver>
```

参考：

- https://dev.mi.com/mipush/downpage/ 小米推送SDK下载地址
- https://www.jianshu.com/p/b1134bebc2d4  Android消息推送：手把手教你集成小米推送

### 消息传递

以上为Android部分，和之前Android集成小米推送的不同点

- flutter工程中application必须继承FlutterApplication
- 需要将推送获取的消息发送到flutter平台

分为两步：

#### 在Android平台发送

主要通过messageChannel

- messageChannel初始化
- 延迟一定的时间发送消息

```
 @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {

        //打印消息方便测试
        System.out.println("用户点击了通知消息");
        System.out.println("通知消息是" + message.toString());
        System.out.println("点击后,会进入应用" );
        
         try {
            ComponentName com = new ComponentName(context.getPackageName(), "com.example.fluttermipushdemo.MainActivity");
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
```

在MainActivity中添加如下方法，在onCreate中调用：

```
  private BasicMessageChannel<String> messageChannel;
   
 @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    setIntent(intent);
// 延迟发送消息，等待flutter环境初始化。
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
     // 初始化messageChannel
        messageChannel = new BasicMessageChannel<>(getFlutterView(), PUSH_CHANNEL, StringCodec.INSTANCE);
        Bundle b = getIntent().getExtras();
        if (b != null) {
          sendMessage(b);
        }
      }
    }, 1500l);
  }

  public void sendMessage(Bundle b) {
      if (b != null) {
          String json = b.getString(ResponseMessage.JSON_CONTENT);
          // 发送
          messageChannel.send(json);
      }
    }
```

- 发送消息之前需要延迟一定的时间，等待初始化完成。

#### 在flutter中接受消息

在flutter第一个页面中添加如下方法，用于接受Android平台发送的消息

```dart
 static const String PUSH_CHANNEL = "fluttermipushdemo.example.com/push";
  static const BasicMessageChannel<String> platform = const BasicMessageChannel<String>(PUSH_CHANNEL, const StringCodec());

  void initState() {
    super.initState();
    platform.setMessageHandler(_handlePlatformMessage);
  }

  Future<String> _handlePlatformMessage(String message) async {
    Map pushData =  json.decode(message);
    return null;
  }
```

- PUSH_CHANNEL字符串和Android平台对应的要一样
- 在initState方法中注册消息的处理方法

参考

- https://github.com/flutter/flutter/tree/master/examples/flutter_view  FlutterMessageChannel 主要用法

## iOS 

参考

- [ios推送配置](https://dev.mi.com/doc/p=2995/index.html) 
- [创建推送证书](https://www.jianshu.com/p/2777e3cf6bf8 )
- [flutter接受消息部分同样参考上面的工程](https://github.com/flutter/flutter/tree/master/examples/flutter_view   )



