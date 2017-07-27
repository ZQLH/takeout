package ui.fragment;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lala.heimawaimaizhunbei.R;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dragger.component.fragment.DaggerHomeFragmentComponent;
import dragger.module.fragment.HomeFragmentPresenterModule;
import presenter.base.MyApplication;
import presenter.fragment.HomeFragmentPresenter;
import ui.adapter.HomeRecyclerViewAdapter;

/**
 * 工作内容：
 * 1、布局
 * 2、头容器的处理
 * a、需要侵入到状态栏中
 * b、状态栏为透明
 * c、随着RecyclerView的滑动，头的透明度会变动
 * 3、RecyclerView数据加载
 * a、简单数据加载
 * b、复杂数据加载
 */

public class HomeFragment extends BaseFragment {
    @InjectView(R.id.rv_home)
    RecyclerView rvHome;
    @InjectView(R.id.srl_home)
    SwipeRefreshLayout srlHome;
    @InjectView(R.id.home_tv_address)
    TextView homeTvAddress;
    @InjectView(R.id.ll_title_search)
    LinearLayout llTitleSearch;
    @InjectView(R.id.ll_title_container)
    LinearLayout llTitleContainer;
    private HomeRecyclerViewAdapter adapter;
    //@Inject 使用 @inject 注解标记要注入对象的字段。
    @Inject
    public HomeFragmentPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerHomeFragmentComponent.builder().homeFragmentPresenterModule(new HomeFragmentPresenterModule(this)).build().in(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new HomeRecyclerViewAdapter(getActivity());
        rvHome.setAdapter(adapter);
        rvHome.setLayoutManager(new LinearLayoutManager(MyApplication.context,LinearLayoutManager.VERTICAL,false));
        rvHome.addOnScrollListener(listener);
    }

    private int sumY=0;
    private float duration=350.0f;
    private ArgbEvaluator evaluator=new ArgbEvaluator();
    RecyclerView.OnScrollListener listener=new  RecyclerView.OnScrollListener(){
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            sumY+=dy;
            int bgColor;
            if (sumY<0){
                bgColor=0X113190E8;
            }else if(sumY>350){
                bgColor=0XBB3190E8;
            }else{
                bgColor= (int) evaluator.evaluate(sumY/duration,0X113190E8,0XFF3190E8);
            }
            llTitleContainer.setBackgroundColor(bgColor);
        }
        // 滚动的总距离相对0-150之间有一个百分比，头部的透明度也是从初始值变动到不透明，
        // 通过距离的百分比，得到透明度对应的值
        // 如果小于0那么透明度为初始值，如果大于150为不透明状态
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getData();
    }

    public void failed(String msg) {

    }

    public HomeRecyclerViewAdapter getAdapter() {
        return adapter;
    }
}
