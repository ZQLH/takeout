package ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.lala.heimawaimaizhunbei.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jpush.android.api.JPushInterface;
import ui.fragment.HomeFragment;
import ui.fragment.OrderFragment;
import ui.fragment.MeFragment;
import ui.fragment.MoreFragment;


/**
 * Created by lala on 2017/7/21.
 */

public class MainActivity extends AppCompatActivity {
    @InjectView(R.id.main_fragment_container)
    FrameLayout fragmentContainer;
    @InjectView(R.id.main_bottome_switcher_container)
    LinearLayout switcherContainer;
    //1创建 fragment 集合用来保存 tab 栏对应的 fragment 对象
    List<Fragment> fragments = new ArrayList<>();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        JPushInterface.init(getApplicationContext());
        //2创建fragment保存到集合中
        init();
        setListener();
    }

    private void setListener() {
        int childCount = switcherContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            switcherContainer.getChildAt(i).setOnClickListener(swither);
        }
    }

    private void init() {
        fragments.add(new HomeFragment());
        fragments.add(new OrderFragment());
        fragments.add(new MeFragment());
        fragments.add(new MoreFragment());
         swither.onClick(switcherContainer.getChildAt(0));
    }

    private  View.OnClickListener swither=new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            int index=switcherContainer.indexOfChild(v);
            updateUi(index);
            switchFragment(index);
        }
    };

    private void switchFragment(int index) {
        Fragment fragment=fragments.get(index);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,fragment).commit();
    }

    private void updateUi(int index) {
        int childCount = switcherContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            setEnable(switcherContainer.getChildAt(i),index!=i);
        }
    }
    //true为可点击的
    private void setEnable(View view,boolean enable) {
        view.setEnabled(enable);
        if (view instanceof ViewGroup){
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                setEnable(((ViewGroup) view).getChildAt(i),enable);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragments.get(0).onActivityResult(requestCode,resultCode,data);
    }
}
