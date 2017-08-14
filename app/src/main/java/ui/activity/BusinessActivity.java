package ui.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.example.lala.heimawaimaizhunbei.R;

import java.util.ArrayList;
import java.util.List;

import ui.adapter.MyPagerAdapter;
import ui.fragment.CommentFragment;
import ui.fragment.GoodsFragment;
import ui.fragment.SellersFragment;
import utils.UiUtils;

/**
 * Created by lala on 2017/7/23.
 */

public class BusinessActivity extends AppCompatActivity {

    private String sellerName;
    public static int sellerId;
    private Bundle extras;
    private List<Fragment> fragments;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_detail);
        extras = this.getIntent().getExtras();
        int a = extras.getInt("sellerId");
        initToolBar();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        ViewPager viewPager = (ViewPager) findViewById(R.id.vp);
        fragments = new ArrayList<>();
        fragments.add(setTitle(new GoodsFragment(), "商品"));
        fragments.add(setTitle(new CommentFragment(), "评价"));
        fragments.add(setTitle(new SellersFragment(), "商家"));
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Rect outRect =new Rect();
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        int statusBarHeight=outRect.top;
        UiUtils.STATUE_BAR_HEIGHT=statusBarHeight;
    }

    private Fragment setTitle(Fragment fragment, String title) {
        Bundle args=new Bundle();
        args.putString("title",title);
        fragment.setArguments(args);
        return fragment;
    }

    private void initToolBar() {
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        sellerId = extras.getInt("sellerId");
        sellerName = extras.getString("sellerName");
        toolbar.setTitle(sellerName);
        //replace actionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
