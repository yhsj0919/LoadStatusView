package xyz.yhsj.loadstatusviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import xyz.yhsj.loadstatusview.LoadStatusView;
import xyz.yhsj.loadstatusview.listener.OnStatusPageClickListener;
public class MainActivity extends AppCompatActivity {
    private LoadStatusView loadStatusView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadStatusView = (LoadStatusView) findViewById(R.id.loadview);
        loadStatusView.setViewState(LoadStatusView.VIEW_STATE_ERROR);

        loadStatusView.setOnStatusPageClickListener(new OnStatusPageClickListener() {
            @Override
            public void onError() {
                loadStatusView.setViewState(LoadStatusView.VIEW_STATE_EMPTY);
            }

            @Override
            public void onEmpty() {
                loadStatusView.setViewState(LoadStatusView.VIEW_STATE_ERROR);
            }

        });
    }
}
