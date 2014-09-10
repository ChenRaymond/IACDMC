package com.iac.dmc;

import com.iac.dmc.engine.BrowseDMSProxy;
import com.iac.dmc.engine.BrowseDMSProxy.BrowseRequestCallback;
import com.iac.dmc.engine.ContentManager;
import com.iac.dmc.engine.DLNAServerContainer;
import com.iac.dmc.engine.MediaItem;
import com.iac.dmc.engine.MediaManager;
import com.iac.dmc.inter.IDeviceChangeListener;
import com.iac.dmc.util.CommonUtil;
import com.iac.dmc.util.DLNAUtil;

import org.cybergarage.upnp.Device;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ContentActivity extends BaseActivity implements OnItemClickListener, IDeviceChangeListener,
BrowseRequestCallback, OnClickListener{

    private TextView mTVSelDeV;
    private ListView mContentListView;
    private Button mBtnBack;


    private ContentAdapter mContentAdapter;
    private ContentManager mContentManager;

    private List<MediaItem> mCurItems;

    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_layout);

        initView();
        initData();
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
        mContentManager.clear();
        super.onDestroy();
    }




    private void initView()
    {
        mTVSelDeV = (TextView) findViewById(R.id.tv_selDev);
        mContentListView = (ListView) findViewById(R.id.content_list);
        mContentListView.setOnItemClickListener(this);
        mBtnBack = (Button) findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading...");
    }

    private void initData()
    {
        mContentManager = ContentManager.getInstance();

        mCurItems = new ArrayList<MediaItem>();
        mContentAdapter = new ContentAdapter(this, mCurItems);
        mContentListView.setAdapter(mContentAdapter);

        updateSelDev();

        mHandler = new Handler();
        mHandler.postDelayed(new RequestDirectoryRunnable(), 100);
    }



    private void requestDirectory()
    {
        Device selDevice = DLNAServerContainer.getInstance().getSelectedDevice();
        if (selDevice == null){
            CommonUtil.showToask(this, "can't select any devices...");
            finish();
            return ;
        }

        BrowseDMSProxy.syncGetDirectory(this, this);
        showProgress(true);
    }

    class RequestDirectoryRunnable implements Runnable{

        @Override
        public void run() {
            // TODO Auto-generated method stub
            requestDirectory();
        }

    }



    private void setContentlist(List<MediaItem> list)
    {
        mCurItems = list;
        if (list == null){
            mContentAdapter.clear();
        }else{
            mContentAdapter.refreshData(list);
        }
    }


    private ProgressDialog mProgressDialog;
    private void showProgress(boolean bShow)
    {
        mProgressDialog.dismiss();
        if (bShow){
            mProgressDialog.show();
        }

    }

    private void backToMainPage(int index, MediaItem item){

        MediaManager.getInstance().setMusicList(mCurItems);
        MediaManager.getInstance().setMusicPlayIndex(index);
        DMSActivity.getInstance().closeAndSetResult();
        finish();

        /*Intent intent = new Intent();
        intent.setClass(this, MusicPlayerActivity.class);
        intent.putExtra(MusicPlayerActivity.PLAY_INDEX, index);
        MediaItemFactory.putItemToIntent(item, intent);
        ContentActivity.this.startActivity(intent);*/
    }

    /*private void goVideoPlayerActivity(int position, MediaItem item){

        MediaManager.getInstance().setVideoList(mCurItems);

        Intent intent = new Intent();
        intent.setClass(this, VideoPlayerActivity.class);
        intent.putExtra(VideoPlayerActivity.PLAY_INDEX, position);
        MediaItemFactory.putItemToIntent(item, intent);
        ContentActivity.this.startActivity(intent);
    }


    private void goPicturePlayerActivity(int position, MediaItem item){

        MediaManager.getInstance().setPictureList(mCurItems);

        Intent intent = new Intent();
        intent.setClass(this, PicturePlayerActivity.class);
        intent.putExtra(PicturePlayerActivity.PLAY_INDEX, position);
        MediaItemFactory.putItemToIntent(item, intent);
        ContentActivity.this.startActivity(intent);
    }*/


    private void back(){
        mContentManager.popListItem();
        List<MediaItem> list = mContentManager.peekListItem();
        if (list == null){
            super.onBackPressed();
        }else{
            setContentlist(list);
        }

    }

    @Override
    public void onBackPressed() {
        back();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {

        MediaItem item = (MediaItem) parent.getItemAtPosition(position);
        log.e("item = \n" + item.getShowString());

        if (DLNAUtil.isAudioItem(item)) {
            backToMainPage(position, item);
        }
        else{
            BrowseDMSProxy.syncGetItems(ContentActivity.this, item.getStringid(), ContentActivity.this);
            showProgress(true);
        }
    }

    @Override
    public void onDeviceChange(boolean isSelDeviceChange) {
        // TODO Auto-generated method stub
        if (isSelDeviceChange){
            CommonUtil.showToask(this, "current device has been drop...");
            finish();
        }
    }

    @Override
    public void onGetItems(final List<MediaItem> list) {
        // TODO Auto-generated method stub
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                showProgress(false);
                if (list == null){
                    CommonUtil.showToask(ContentActivity.this, "can't get folder...");
                    return ;
                }
                mContentManager.pushListItem(list);
                setContentlist(list);

            }
        });
    }


    private void updateSelDev()
    {
        setSelDevUI(DLNAServerContainer.getInstance().getSelectedDevice());
    }


    private void setSelDevUI(Device device)
    {
        if (device == null)
        {
            mTVSelDeV.setText("no select device");
        }else{
            mTVSelDeV.setText(device.getFriendlyName());
        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId()){
            case R.id.btn_back:
                back();
                break;
        }
    }

    private static class log {

        public static void e(String s) {
            Log.e("ContentActivity", s);
        }
    }
}
