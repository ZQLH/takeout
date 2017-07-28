package ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lala.heimawaimaizhunbei.R;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dragger.component.fragment.DaggerGoodsFragmentComponent;
import dragger.module.fragment.GoodsFragmentPresenterModule;
import model.net.bean.GoodsInfo;
import model.net.bean.GoodsTypeInfo;
import presenter.fragment.GoodsFragmentPresenter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import ui.ShoppingCartManager;
import ui.activity.CartActivity;
import ui.adapter.MyAdapter;
import ui.adapter.StickyAdapter;
import utils.UiUtils;

/**
 * Created by lala on 2017/7/24.
 */

public class GoodsFragment extends BaseFragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {
    @Inject
    public GoodsFragmentPresenter presenter;
    @InjectView(R.id.shl)
    StickyListHeadersListView shl;
    @InjectView(R.id.lv)
    ListView listView;
    @InjectView(R.id.iv_cart)
    ImageView ivCart;
    private MyAdapter adapter;
    private StickyAdapter stickyAdapter;
    public static Context  context;
    ShoppingCartManager instance=ShoppingCartManager.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerGoodsFragmentComponent.builder().goodsFragmentPresenterModule(new GoodsFragmentPresenterModule(this)).build().in(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods, null);
        context=getActivity();
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FrameLayout container_all = (FrameLayout) UiUtils.getContainder(view,R.id.seller_detail_container);
        TextView count = (TextView) container_all.findViewById(R.id.fragment_goods_tv_count);
        int cart_num=0;
        cart_num=instance.getTotalNum();
        if (cart_num>0) {
            count.setVisibility(View.VISIBLE);
            count.setText(cart_num+"");
        }
        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,CartActivity.class);
                getActivity().startActivity(intent);
            }
        });
        adapter = new MyAdapter(getActivity());
        stickyAdapter = new StickyAdapter(this, getActivity());
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.getData();
        listView.setAdapter(adapter);
        shl.setAdapter(stickyAdapter);
        listView.setOnItemClickListener(this);
        shl.setOnScrollListener(this);
    }

    public MyAdapter getAdapter() {
        return adapter;
    }

    public StickyAdapter getAdapter2() {
        return stickyAdapter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void failed(String msg) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.setSelectedPosition(position);
        GoodsTypeInfo head = adapter.data.get(position);
        shl.setSelection(head.groupFirstIndex);
        shl.smoothScrollToPosition(head.groupFirstIndex);

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (stickyAdapter.data.size() > 0) {
            GoodsInfo data = stickyAdapter.data.get(firstVisibleItem);
            adapter.setSelectedPosition(data.headIndex);
            listView.smoothScrollToPosition(data.headIndex);
        }
    }

}

