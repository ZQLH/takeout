package ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lala.heimawaimaizhunbei.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import model.MyApplication;
import model.net.bean.Order;
import ui.adapter.OrderRecyclerViewAdapter;
import ui.view.IView;

/**
 * Created by lala on 2017/7/21.
 */

public class OrderFragment extends BaseFragment implements IView, SwipeRefreshLayout.OnRefreshListener {
    @InjectView(R.id.rv_order_list)
    RecyclerView rvOrderList;
    @InjectView(R.id.srl_order)
    SwipeRefreshLayout srlOrder;

    private OrderRecyclerViewAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new OrderRecyclerViewAdapter();
        rvOrderList.setAdapter(adapter);
        rvOrderList.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL,false));

        srlOrder.setOnRefreshListener(this);

        srlOrder.setRefreshing(true);
        orderPresenter.getOrderInfo();
        orderPresenter.getData();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
    public void closeRefresh(){
        if(srlOrder.isRefreshing()){
            srlOrder.setRefreshing(false);
        }
    }

    public OrderRecyclerViewAdapter getAdapter()
    {
        return adapter;
    }

    @Override
    public void success(Object o) {
        if (o instanceof List){
            List<Order>  orders = (List<Order>) o;
            Order order = orders.get(0);
            if(!TextUtils.isEmpty(MyApplication.type)){
                order.setType(MyApplication.type);
            }

            // 更新界面：adapter
            adapter.setOrderList(orders);
            adapter.notifyDataSetChanged();
            // 隐藏滚动条
            closeRefresh();
        }
    }

    @Override
    public void failed(String msg) {

    }

    @Override
    public void onRefresh() {
        orderPresenter.getOrderInfo();
    }

}
