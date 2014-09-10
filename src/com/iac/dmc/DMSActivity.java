package com.iac.dmc;

import com.iac.dmc.engine.DLNAServerContainer;
import com.iac.dmc.engine.DLNAServerContainer.DeviceChangeListener;
import com.iac.dmc.service.DLNASearchServerService;
import com.iac.dmc.util.LogUtil;

import org.cybergarage.upnp.Device;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class DMSActivity extends BaseActivity{

    /** Called when the activity is first created. */
    protected static final String TAG = "DMSActivity";
    private Button mBtnSearch;

    private ListView mDevListView;

    private DMSAdapter mDevAdapter;
    private List<Device> mDevices;

    private static DMSActivity mDMSActivity;
    private String mDevFriendName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dms_layout);
        mDMSActivity = this;
        startDLNASearchServerService();
        initView();
        initData();
    }

    public static DMSActivity getInstance() {
        return mDMSActivity;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d("Chen Raymond", "DMSActivity onDestroy");
        super.onDestroy();
    }

    private void initView(){
        mBtnSearch = (Button) findViewById(R.id.btn_search);
        mBtnSearch.setOnClickListener(new SearchButtonClickListener());
        mDevListView = (ListView) findViewById(R.id.device_list);
        mDevListView.setOnItemClickListener(new DeviceClickListener());
        mDevices = DLNAServerContainer.getInstance().getDevices();
    }

    private void initData(){
        //mAllShareProxy = AllShareProxy.getInstance(this);
        mDevAdapter = new DMSAdapter(this, mDevices);
        mDevListView.setAdapter(mDevAdapter);
        DLNAServerContainer.getInstance().setDeviceChangeListener(
                new DeviceChangeListener() {

                    @Override
                    public void onDeviceChange(Device device) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refresh();
                            }
                        });
                    }
                });
    }

    public class SearchButtonClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch(v.getId()){
                case R.id.btn_search:
                    Log.d("Chen Raymond","btn_search");
                    startDLNASearchServerService();
                    mDevices = DLNAServerContainer.getInstance().getDevices();
                    LogUtil.d(TAG, "mDevices size:" + mDevices.size());
                    refresh();
                    break;
            }
        }
    }

    public class DeviceClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            Device device = (Device) parent.getItemAtPosition(position);
            DLNAServerContainer.getInstance().setSelectedDevice(device);
            mDevFriendName = device.getFriendlyName();
            goContentActivity();
        }

    }

    private void refresh() {
        if (mDevAdapter != null) {
            mDevAdapter.refreshData(mDevices);
        }
    }

    private void goContentActivity(){
        Intent intent = new Intent(this, ContentActivity.class);
        startActivity(intent);
    }

    public void closeAndSetResult() {
        Bundle bundle = new Bundle();
        bundle.putString("getFriendlyName", mDevFriendName);
        Intent intentSS = new Intent();
        intentSS.putExtras(bundle);
        setResult(RESULT_OK, intentSS);
        finish();
    }

    private void startDLNASearchServerService() {
        Intent intent = new Intent(getApplicationContext(), DLNASearchServerService.class);
        startService(intent);
    }
}