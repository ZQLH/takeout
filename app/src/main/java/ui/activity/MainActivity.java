package ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import ui.fragment.HomeFragment;
import ui.fragment.InvestFragment;
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
        getWindow().getDecorView().setSystemUiVisibility(View.GONE);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
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
        fragments.add(new InvestFragment());
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
}
