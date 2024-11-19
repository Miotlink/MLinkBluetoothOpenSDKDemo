妙联蓝牙配网Android SDK使用说明文档
(Build 2024-11-19)










	





















 编 写 人：ml_qiaozhuang
                             编写时间：2024-11-19
                             审 核 人：ml_szf
    审核时间：2024-11-19
修订页
编号	章节名称	修订内容简述	修订日期	修订后
版本号	修订人
1	全部	编写首次版本	2021-03	V1.0.0	
2			2021-03	V1.1.1	qiao
					
					
					
					
					
					
					
					
					
					
					
					


 













1.说明
 1.1 本文档用于说明妙联蓝牙WIFI配网SDK Android版本接口之间的关系以及接口调用顺序，对蓝牙WIFI 配网SDK Android版本各接口都有详细的说明。
2.名词解释
kindId	设备品类ID目前使用于妙联APP、或者接入妙联平台APP
modelId	设备型号ID目前使用于妙联APP、或者接入妙联平台APP
deviceName	蓝牙设备名称
macAddress	蓝牙设备MAC地址
mVersion	蓝牙模组版本号
mCode	蓝牙模组配网方式，目前使用于妙联APP
rssi	蓝牙信号强度

3.功能介绍
蓝牙WIFI搜索	用于搜索带有妙联标志的蓝牙设备
蓝牙WIFI配网	手机端将蓝牙设备连接上路由器并连接上平台
蓝牙串口通讯	可实现手机端和蓝牙设备的数据通讯

4.开发指南
4.1工程配置
  环境准备
支持 Android Studio 3.0 以上 支持 JDK 7.0 以上版本

支持 Android 手机系统 4.4 以上版本
4.2流程图


4.3获得SDK
Demo下载地址
https://github.com/Miotlink/MLinkBluetoothOpenSDKDemo.git

Demo 安装二维码


Gradle依赖地址  
dependencies {
   implementation 'com.github.Miotlink:MLinkBluetoothOpenSDK:v1.0.7'
}

配置build.gradle
allprojects {
    repositories {
        maven {
            url 'https://jitpack.io'
        }
     }
}
dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}



4.4配置权限
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.BLUETOOTH_ADVERTISE" />
<uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
<uses-permission android:name="android.permission.BLUETOOTH_SCAN" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>

4.5SDK初始化
一般建议在application中初始化

MLinkSmartBluetoothSDK.getInstance().setDebug(false);//true日志打印//false 不打印

MLinkSmartBluetoothSDK.getInstance().init(this, "", (code, message) -> {

});

4.6调用方法
4.6.1蓝牙设备搜索
调用方法
MLinkSmartBluetoothSDK.getInstance().startScan(scanCallBack);
MLinkSmartBluetoothSDK.getInstance().startScan(10000，scanCallBack);
回调函数
BleDeviceScanListener bleDeviceScanListener=new BleDeviceScanListener() {
    @Override
    public void onDiscoverDevices(BleModelDevice bleModelDevice) throws Exception {
未绑定的设备
        
    }

    @Override
    public void onHasBindDiscoverDevices(BleModelDevice bleModelDevice) throws Exception {
//  已绑定的设备
    }
}


4.6.2停止搜索
调用方法
MLinkSmartBluetoothSDK.getInstance().onStopScan();

4.6.3蓝牙连接
调用方法
MLinkSmartBluetoothSDK.getInstance().connect(bleModelDevice.getMacAddress(), notifyDeviceConnectListener);
连接回调

SmartNotifyDeviceConnectListener notifyDeviceConnectListener=new SmartNotifyDeviceConnectListener() {
    @Override
    public void onSmartNotifyConnectListener(int errorCode, String errorMessage, String macCode) {
        
    }
};


errorCode  0 断开连接 1连接中 2连接成功

4.6.4获取蓝牙的连接状态

调用方法	
MiotSmartBluetoothSDK.getInstance().isConnect(macCode);

macCode	蓝牙设备MAC地址
	
监听回调


4.6.5绑定设备、蓝牙通信

调用方法	
MiotSmartBluetoothSDK.getInstance().bindPu(macCode);

macCode	蓝牙设备MAC地址
	
监听回调
SmartNotifyBindPuListener smartNotifyBindPuListener=new SmartNotifyBindPuListener() {
    @Override
    public void notifyBindPuListener(String macCode, int errorCode, String errorMessage) throws Exception {
        
    }
}


4.6.6断开连接
MiotSmartBluetoothSDK.getInstance().onDisConnect(String macCode);




4.6.7监听数据通信

MiotSmartBluetoothSDK.getInstance().send(macCode,byte[],smartNotifyListener)

SmartNotifyUartDataListener smartNotifyUartDataListener=new SmartNotifyUartDataListener() {
    @Override
    public void onSmartNotifyUartDataListener(String macCode, String command) throws Exception {
        
    }

    @Override
    public void onSmartNotifyBindListener(String macCode, int errorCode, String errorMessage) {

    }

    @Override
    public void onSmartNotifyDeviceVersionListener(String macCode, int version) {

    }

    @Override
    public void onNotifyUartData(String macCode, int errorCode, String errorMessage) {

    }
}


4.6.8销毁
App应用销毁调用该方法

MiotSmartBluetoothSDK.getInstance().onDestory();


4.7错误码
15	绑定设备成功
2	连接设备成功
1	连接中
0	设备断开连接
100	发送数据失败，蓝牙断开连接

4.8混淆代码
   -keep class com.miotlink.** { *;}
4.9注意事项
4.9.1Android操作系统8.0以上需打开开启位置权限
4.9.2Android操作系统8.1以上需打开位置服务
4.9.3Android  API>31 以上需打开位置服务和发现设备权限
4.9.4仅支持Android 4.4以上的操作系统
5.联系我们
电话：0571-86797400
邮箱：qiaozhuang@miotlink.com
地址：浙江省杭州市滨江区伟业路1号高新软件园5号楼
网址：www.miotlink.com
