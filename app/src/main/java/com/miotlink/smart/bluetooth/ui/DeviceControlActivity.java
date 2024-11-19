package com.miotlink.smart.bluetooth.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.miotlink.MLinkSmartBluetoothSDK;
import com.miotlink.bluetooth.listener.SmartNotifyBindPuListener;
import com.miotlink.bluetooth.listener.SmartNotifyDeviceConnectListener;
import com.miotlink.bluetooth.listener.SmartNotifyUartDataListener;
import com.miotlink.bluetooth.model.BleModelDevice;
import com.miotlink.bluetooth.utils.HexUtil;
import com.miotlink.smart.bluetooth.R;
import com.miotlink.smart.bluetooth.base.BaseActivity;
import com.miotlink.smart.bluetooth.view.LoadingViewDialog;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DeviceControlActivity extends BaseActivity implements View.OnClickListener, SmartNotifyDeviceConnectListener, SmartNotifyUartDataListener {


    private TextView ble_state_tv;

    private Button send_hex_btn, get_wifiinfo_btn, ota_btn, product_test_btn;

    private EditText send_ble_hex_et;

    private TextView receiver_ble_hex, clean_data;

    private BleModelDevice bleModelDevice = null;

    private boolean isconnect = false;

    private String uartdata = "";

    private LoadingViewDialog loadingViewDialog = null;

    private String macCode = "";

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");


    public static void startIntent(Context mContext, Bundle bundle) {
        Intent intent = new Intent(mContext, DeviceControlActivity.class);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    @Override
    public void initView() throws Exception {
        loadingViewDialog = new LoadingViewDialog(mContext);
        ble_state_tv = findViewById(R.id.ble_state_tv);
        clean_data = findViewById(R.id.clean_data);
        send_hex_btn = findViewById(R.id.send_hex_btn);
        get_wifiinfo_btn = findViewById(R.id.get_wifiinfo_btn);
        ota_btn = findViewById(R.id.ota_btn);
        send_ble_hex_et = findViewById(R.id.send_ble_hex_et);
        receiver_ble_hex = findViewById(R.id.receiver_ble_hex);
        product_test_btn = findViewById(R.id.product_test_btn);
        product_test_btn.setOnClickListener(this);
        send_hex_btn.setOnClickListener(this);
        clean_data.setOnClickListener(this);
        get_wifiinfo_btn.setOnClickListener(this);
        ota_btn.setOnClickListener(this);
        ble_state_tv.setOnClickListener(this);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_ble_control;
    }

    @Override
    protected void onDestroy() {
        MLinkSmartBluetoothSDK.getInstance().disConnect(bleModelDevice.getMacAddress());
        super.onDestroy();
    }

    @Override
    public void initData() throws Exception {
        macCode = getIntent().getStringExtra("macCode");
        bleModelDevice = MLinkSmartBluetoothSDK.getInstance().getScanDevice(macCode);
        if (bleModelDevice != null) {
            MLinkSmartBluetoothSDK.getInstance().connect(bleModelDevice.getMacAddress(), this);
            MLinkSmartBluetoothSDK.getInstance().setSmartNotifyUartDataListener(this);
        }

    }


    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("device", bleModelDevice);
        bundle.putString("macCode", bleModelDevice.getMacAddress());
        switch (v.getId()) {
            case R.id.send_hex_btn:
                if (TextUtils.isEmpty(send_ble_hex_et.getText())) {
                    Toast.makeText(mContext, "请输入串口数据", Toast.LENGTH_SHORT).show();
                    return;
                }
                MLinkSmartBluetoothSDK.getInstance().setSmartNotifyUartDataListener(this);
                String s = send_ble_hex_et.getText().toString();
                MLinkSmartBluetoothSDK.getInstance().send(bleModelDevice.getMacAddress(), s.getBytes(), this);
                break;
            case R.id.get_wifiinfo_btn:

                MLinkSmartBluetoothSDK.getInstance().bindPu(bleModelDevice.getMacAddress(), (macCode, errorCode, errorMessage) -> {
                    if (errorCode == 0x0f) {
                        runOnUiThread(() -> Toast.makeText(mContext, "设备已绑定", Toast.LENGTH_SHORT).show());
                    }
                });
                break;
            case R.id.product_test_btn:
                MLinkSmartBluetoothSDK.getInstance().getVersion(bleModelDevice.getMacAddress());
                break;

            case R.id.clean_data:
                uartdata = "";
                receiver_ble_hex.setText(uartdata);
                break;
            case R.id.ble_state_tv:
                if (MLinkSmartBluetoothSDK.getInstance().isConnect(macCode)) {
                    MLinkSmartBluetoothSDK.getInstance().disConnect(macCode);
                } else {
                    MLinkSmartBluetoothSDK.getInstance().connect(macCode, this);
                }
                break;
        }
    }


    @Override
    public void onSmartNotifyConnectListener(int errorCode, String errorMessage, String macCode) {
        runOnUiThread(() -> {

            ble_state_tv.setText(errorCode == 2 ? "断开" : "连接");
        });
    }


    @Override
    public void onSmartNotifyDeviceVersionListener(String macCode, int version) {

    }

    @Override
    public void onNotifyUartDataListener(String macCode, int errorCode, String errorMessage, String command) {
        uartdata += simpleDateFormat.format(new Date()) + ":" + command + "\n";
        runOnUiThread(() -> receiver_ble_hex.setText(uartdata));
    }


}
