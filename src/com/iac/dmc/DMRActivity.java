package com.iac.dmc;

import com.iac.dmc.engine.DLNARenderContainer;
import com.iac.dmc.engine.DLNARenderContainer.DeviceChangeListener;
import com.iac.dmc.service.DLNASearchRenderService;
import com.iac.dmc.util.LogUtil;

import org.cybergarage.upnp.Device;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class DMRActivity extends BaseActivity{
    protected static final String TAG = "DMRActivity";
    private Button btn_main;
    private ListView lv_main;
    private View emptyView;
    private List<Device> mDevices;
    private DeviceAdapter mDeviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startDLNASearchRenderService();
        findView();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopDLNASearchRenderService();
    }

    private void findView() {
        btn_main = (Button) findViewById(R.id.btn_main);
        lv_main = (ListView) findViewById(R.id.lv_main);
    }

    private void initView() {
        btn_main.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startDLNASearchRenderService();
                mDevices = DLNARenderContainer.getInstance().getDevices();
                LogUtil.d(TAG, "mDevices size:" + mDevices.size());
                refresh();
            }
        });

        emptyView = View.inflate(this, R.layout.empty_view, null);
        addContentView(emptyView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        lv_main.setEmptyView(emptyView);

        mDeviceAdapter = new DeviceAdapter();
        mDevices = DLNARenderContainer.getInstance().getDevices();
        lv_main.setAdapter(mDeviceAdapter);
        lv_main.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DLNARenderContainer.getInstance().setSelectedDevice(mDevices.get(position));
                //mDevices.get(position).getFriendlyName();
                Bundle bundle = new Bundle();
                bundle.putString("getFriendlyName", mDevices.get(position).getFriendlyName());
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                //startControlActivity();
            }
        });

        DLNARenderContainer.getInstance().setDeviceChangeListener(
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

    private void refresh() {
        if (mDeviceAdapter != null) {
            mDeviceAdapter.notifyDataSetChanged();
        }
    }

    private void startDLNASearchRenderService() {
        Intent intent = new Intent(getApplicationContext(), DLNASearchRenderService.class);
        startService(intent);
    }

    private void stopDLNASearchRenderService() {
        Intent intent = new Intent(getApplicationContext(), DLNASearchRenderService.class);
        stopService(intent);
    }

    private class DeviceAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mDevices == null) {
                return 0;
            } else {
                return mDevices.size();
            }
        }

        @Override
        public Object getItem(int position) {
            if (mDevices != null) {
                return mDevices.get(position);
            }

            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(),
                        R.layout.item_lv_main, null);
                holder = new ViewHolder();
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_name_item = (TextView) convertView
                    .findViewById(R.id.tv_name_item);
            holder.tv_name_item.setText(mDevices.get(position)
                    .getFriendlyName());
            return convertView;
        }
    }

    static class ViewHolder {
        private TextView tv_name_item;
    }

}
