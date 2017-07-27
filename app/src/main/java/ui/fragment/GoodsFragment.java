package ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

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
import ui.activity.MyAdapter;
import ui.adapter.StickyAdapter;

import static com.example.lala.heimawaimaizhunbei.R.mipmap.food_button_add;

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
    private MyAdapter adapter;
    private StickyAdapter stickyAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerGoodsFragmentComponent.builder().goodsFragmentPresenterModule(new GoodsFragmentPresenterModule(this)).build().in(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods, null);
        View viewById = view.findViewById(R.id.seller_detail_container);
        ImageView dy_iv=new ImageView(getActivity());
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new MyAdapter(getActivity());
        stickyAdapter = new StickyAdapter(this,getActivity());
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

    public MyAdapter getAdapter()
    {
        return adapter;
    }

    public StickyAdapter getAdapter2()
    {
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
        GoodsTypeInfo head=adapter.data.get(position);
        shl.setSelection(head.groupFirstIndex);
        shl.smoothScrollToPosition(head.groupFirstIndex);

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(stickyAdapter.data.size() > 0){
            GoodsInfo data=stickyAdapter.data.get(firstVisibleItem);
            adapter.setSelectedPosition(data.headIndex);
            listView.smoothScrollToPosition(data.headIndex);
        }
    }
}

