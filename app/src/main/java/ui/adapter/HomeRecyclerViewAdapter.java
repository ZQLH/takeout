package ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.lala.heimawaimaizhunbei.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.net.bean.Category;
import model.net.bean.GoodsInfo;
import model.net.bean.HomeInfo;
import model.net.bean.Seller;
import ui.ShoppingCartManager;
import ui.activity.BusinessActivity;

/**
 * Created by lala on 2017/7/21.
 */

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter {
    //定义三种条目类型
    private static final int TYPE_TITLE = 0;
    private static final int TYPE_DIVISION = 1;
    private static final int TYPE_SELLER = 2;
    private static HomeInfo info;
    public Context mContext;
    private List<Category> category;
    private View title;
    private LinearLayout linearLayout;
    private int sellerid;

    public HomeRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }
    public void setData(HomeInfo info) {
        this.info=info;
        notifyDataSetChanged();
    }
    /**
     *
     * View.inflate与LayoutInflater的底层都是一样的，需要注意的是，因为涉及到复用的问题，所以需要传入
     * parent的地方必须要传入空
     * */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_TITLE:
                title = View.inflate(mContext, R.layout.item_title,null);
                linearLayout = (LinearLayout) title.findViewById(R.id.catetory_container);
                TitleHolder titleHolder=new TitleHolder(title);
                return titleHolder;
            case TYPE_DIVISION:
                View division=View.inflate(mContext,R.layout.item_division,null);
                DivisionHolder divisionHolder=new DivisionHolder(division);
                return  divisionHolder;
            case  TYPE_SELLER:
                View seller=View.inflate(mContext,R.layout.item_seller,null);
                SellerHolder sellerHolder=new SellerHolder(seller);
                return  sellerHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case TYPE_TITLE:
                TitleHolder  titleHolder = (TitleHolder) holder;
                titleHolder.testData();
                break;
            case TYPE_DIVISION:
                break;
            case TYPE_SELLER:
                SellerHolder  sellerHolder = (SellerHolder) holder;
                sellerHolder.setSeller(info.body.get(position -1).seller);

                sellerid = (int) info.body.get(position -1).seller.id;
                int num=ShoppingCartManager.getInstance().getGoodsSellerIdNum(sellerid);
                if (num > 0) {
                    sellerHolder.tv_count.setText(num + "");
                    sellerHolder.tv_count.setVisibility(View.VISIBLE);
                }else{
                    sellerHolder.tv_count.setText("0");
                    sellerHolder.tv_count.setVisibility(View.GONE);
                }
                break;

        }
    }
    /**
     * 绑定 Item View 和 Holder
     */
    @Override
    public int getItemCount()
    {
       return info != null && info.body!=null ? info.body.size() + 1 : 0;
    }

    @Override
    public int getItemViewType(int position) {

        if (position==0){
            return TYPE_TITLE;
        }else if(info!=null&&info.body.get(position-1).type==0){
            return TYPE_SELLER;
        }else if(info!=null&&info.body.get(position-1).type==1){
            return  TYPE_DIVISION;
        }
        return -1;
    }


    public  class TitleHolder extends RecyclerView.ViewHolder
    {
      SliderLayout sliderShow;
        public TitleHolder(View itemView) {
            super(itemView);
            sliderShow= (SliderLayout) itemView.findViewById(R.id.slider);
        }
        private void testData() {
            HashMap<String,String> url_maps=new HashMap<>();
            for (int i = 0; i < info.head.promotionList.size(); i++) {
                url_maps.put(i+"",info.head.promotionList.get(i).pic+"");
            }
            sliderShow.removeAllSliders();
            //轮播图数据的加载
            for (String desc:url_maps.keySet()){
                TextSliderView textSliderView=new TextSliderView(mContext);
                textSliderView
                  .description(desc)
                .image(url_maps.get(desc).replace("10.0.2.2","192.168.191.1"))
                .setScaleType(BaseSliderView.ScaleType.Fit);

                //因为有复用机制，所以SliderLayout对象中的轮播图还在，即还是用原来的SliderShow,而没有作为垃圾重新创建。
                sliderShow.addSlider(textSliderView);
            }

            //轮播图下面数据的加载
            category = new ArrayList<>();
            category =info.head.categorieList;
            linearLayout.removeAllViews();
            for (int i = 0; i< category.size(); i=i+2){
                int j=i+1;
                View view=View.inflate(mContext,R.layout.item_home_head_category,null);
                ImageView imageView= (ImageView) view.findViewById(R.id.top_iv);
                TextView textView= (TextView) view.findViewById(R.id.top_tv);
                ImageView imageView1= (ImageView) view.findViewById(R.id.bottom_iv);
                TextView textView1= (TextView) view.findViewById(R.id.bottom_tv);
                Picasso.with(mContext).load(category.get(i).pic.replace("10.0.2.2","192.168.191.1")).into(imageView);
                textView.setText(category.get(i).name);
                Picasso.with(mContext).load(category.get(j).pic.replace("10.0.2.2","192.168.191.1")).into(imageView1);
                textView1.setText(category.get(j).name);
                linearLayout.addView(view);
            }


        }
    }
    class DivisionHolder extends RecyclerView.ViewHolder{
        public DivisionHolder(View itemView) {
            super(itemView);
        }
    }


    class  SellerHolder extends RecyclerView.ViewHolder{
        TextView tv;
        TextView tv_count;
        public SellerHolder(View itemView ) {
            super(itemView);
            tv= (TextView) itemView.findViewById(R.id.tv_title);
            tv_count= (TextView) itemView.findViewById(R.id.tv_count);
        }

        public void setSeller(final Seller seller){
            tv.setText(seller.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(itemView.getContext(),BusinessActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putInt("sellerId", (int) seller.getId());
                    bundle.putString("sellerName",seller.getName());
                    ShoppingCartManager.getInstance().name=seller.name;
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });

        }
    }
}
