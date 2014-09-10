package com.iac.dmc;

import com.iac.dmc.service.DLNASearchRenderService;
import com.iac.dmc.service.DLNASearchServerService;
import com.iac.dmc.util.FileHelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StartPageActivity extends BaseActivity implements OnClickListener{
    protected static final String TAG = "StartPageActivity";
    private Button btn_set_renderer;
    private Button btn_set_server;
    private Button btn_play_music;
    private TextView text_renderer_content;
    private ImageView image_renderer_content;
    private TextView text_server_content;
    private ImageView image_server_content;
    private static Context mContext;
    public static final int SET_RENDERER = 0;
    public static final int SET_SERVER = 1;
    public static final int PLAY_MUSIC = 2;
    public static final int GO_BRAVO = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_start_page);
        mContext = this;
        findView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopDLNAService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings :
                Intent goBRAVO = new Intent();
                goBRAVO.setClassName("com.savitech.bravow", "com.savitech.bravow.MainActivity");
                try {
                    startActivity(goBRAVO);
                }
                catch(Exception e) {
                    Log.w("Raymond", e.toString());
                    FileHelper.updatePackage(this, R.raw.bravo);
                    //Toast.makeText(this, "Can't find Bravow setting activity.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }

    private void findView() {
        btn_set_renderer = (Button) findViewById(R.id.btn_set_renderer);
        btn_set_server = (Button) findViewById(R.id.btn_set_server);
        btn_play_music = (Button) findViewById(R.id.btn_play_music);
        text_renderer_content = (TextView) findViewById(R.id.renderer_content);
        image_renderer_content = (ImageView) findViewById(R.id.renderer_content_image);
        text_server_content = (TextView) findViewById(R.id.server_content);
        image_server_content = (ImageView) findViewById(R.id.server_content_image);
        if(btn_set_renderer != null) {
            btn_set_renderer.setOnClickListener(this);
        }
        if(btn_set_server != null) {
            btn_set_server.setOnClickListener(this);
        }
        if(btn_play_music != null) {
            btn_play_music.setOnClickListener(this);
        }
    }

    private void startSearchRendererActivity() {
        Log.d("Chen Raymond", "startSearchRendererActivity");
        Intent intent = new Intent(getApplicationContext(), DMRActivity.class);
        startActivityForResult(intent,SET_RENDERER);
    }

    private void startSearchServerActivity() {
        Log.d("Chen Raymond", "startSearchServerActivity");
        Intent intent = new Intent(getApplicationContext(), DMSActivity.class);
        startActivityForResult(intent, SET_SERVER);
    }

    private void startControlActivity() {
        Log.d("Chen Raymond", "startControlActivity");
        Intent intent = new Intent(getApplicationContext(), ControlActivity.class);
        startActivity(intent);
    }

    private void stopDLNAService() {
        Intent intent = new Intent(getApplicationContext(), DLNASearchRenderService.class);
        stopService(intent);
        Intent intent2 = new Intent(getApplicationContext(), DLNASearchServerService.class);
        stopService(intent2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            switch(requestCode) {
                case SET_RENDERER :
                    Bundle extras = data.getExtras();
                    if (extras != null){
                        setRendererText(extras.getString("getFriendlyName"));
                    }
                    break;
                case SET_SERVER :
                    Log.d("Chen Raymond", "SET_SERVER activity result");
                    Bundle extras1 = data.getExtras();
                    if (extras1 != null){
                        setServerText(extras1.getString("getFriendlyName"));
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        if(view == btn_set_renderer) {
            startSearchRendererActivity();
        }
        if(view == btn_set_server) {
            startSearchServerActivity();
        }
        if(view == btn_play_music) {
            if(TextUtils.isEmpty(text_renderer_content.getText()) ||
                    TextUtils.isEmpty(text_server_content.getText())) {
                //btn_play_music.setEnabled(false);
                Toast.makeText(this, R.string.set_render_server, Toast.LENGTH_SHORT).show();
            }
            else {
                startControlActivity();
            }
        }
    }

    public Boolean setRendererText(String device) {
        if(text_renderer_content != null && image_renderer_content != null) {
            text_renderer_content.setText(device);
            image_renderer_content.setImageResource(R.drawable.tab_icon_music);
            return true;
        }
        else {
            return false;
        }
    }

    public Boolean setServerText(String device) {
        if(text_server_content != null && image_server_content != null) {
            text_server_content.setText(device);
            image_server_content.setImageResource(R.drawable.tab_icon_music);
            return true;
        }
        else {
            return false;
        }
    }
}
