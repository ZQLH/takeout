package ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lala.heimawaimaizhunbei.R;
import com.j256.ormlite.stmt.query.In;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.InjectView;
import model.net.bean.GoodsInfo;
import model.net.bean.GoodsTypeInfo;
import presenter.base.MyApplication;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import ui.ShoppingCartManager;
import ui.fragment.GoodsFragment;
import utils.AnimationUtils;
import utils.UiUtils;

/**
 * Created by lala on 2017/7/24.
 */

public class StickyAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    List<GoodsTypeInfo> data_above = new ArrayList<>();
    public List<GoodsInfo> data = new ArrayList<>();
    TextView tvCount;
    ImageButton ibMinus;
    ImageButton ibAdd;
    private LayoutInflater inflater;
    Context mContext;
    GoodsFragment fragment;
    public List<GoodsInfo> list = new ArrayList<>();
    private FrameLayout container;
    private TextView count;


    public StickyAdapter(GoodsFragment fragment,Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        this.fragment=fragment;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.header, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.head_text);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        holder.text.setText(data_above.get(data.get(position).headIndex).name);
        holder.text.setBackgroundColor(mContext.getResources().getColor(R.color.colorItemBg));
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return data.get(position).headId;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position).name;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            //初始化部分
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_goods, parent, false);
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_detail = (TextView) convertView.findViewById(R.id.tv_zucheng);
            holder.tv_seller_month = (TextView) convertView.findViewById(R.id.tv_yueshaoshounum);
            holder.tv_newprice = (TextView) convertView.findViewById(R.id.tv_newprice);
            holder.tv_oldprice = (TextView) convertView.findViewById(R.id.tv_oldprice);
            holder.iv_min = (ImageView) convertView.findViewById(R.id.ib_minus);
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_money);
            holder.iv_ade = (ImageView) convertView.findViewById(R.id.ib_add);
            holder.tvCount = (TextView) convertView.findViewById(R.id.tv_money);
            holder.headId = data.get(position).headId;
            //右边集合显示的条目组所属于左边集合中的哪一个
            holder.headIndex = data.get(position).headIndex;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //赋值操作部分
        GoodsInfo goodsInfo = data.get(position);
        int count = goodsInfo.count;
        if (count <= 0) {
            holder.iv_min.setVisibility(View.GONE);
            holder.tvCount.setVisibility(View.GONE);
            holder.tvCount.setText("0");
        } else {
            holder.iv_min.setVisibility(View.VISIBLE);
            holder.tvCount.setVisibility(View.VISIBLE);
            holder.tvCount.setText(count + "");
        }


        ibMinus = (ImageButton) holder.iv_min;
        holder.iv_ade.setTag(position);
        holder.iv_min.setTag(position);

        //两个点击事件
        holder.iv_ade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();

                addHandler(v, holder, data.get(position));
            }
        });
        holder.iv_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                minusHander(holder, data.get(position));
            }
        });


        Picasso.with(mContext).load(goodsInfo.icon).into(holder.iv_icon);
        holder.tv_name.setText(goodsInfo.name);
        holder.tv_detail.setText(goodsInfo.form);
        holder.tv_seller_month.setText(String.valueOf(goodsInfo.monthSaleNum));
        holder.tv_newprice.setText("$" + (int) goodsInfo.newPrice);
        holder.tv_oldprice.setText(String.valueOf(goodsInfo.oldPrice));
        return convertView;
    }

    public void setData(ArrayList<GoodsTypeInfo> data) {
        this.data_above = data;
        getGoodsInfo(data_above);
    }

    public List<GoodsInfo> getGoodsInfo(List<GoodsTypeInfo> data_above) {
        for (int i = 0; i < data_above.size(); i++) {
            list = data_above.get(i).list;
            for (int j = 0; j < list.size(); j++) {
                GoodsInfo goodsInfo = list.get(j);
                goodsInfo.headId = data_above.get(i).id;
                goodsInfo.headIndex = i;
                if (j == 0) {
                    //显示标题对应的下标，即在集合1中存储了集合2的小标题的下标信息，方便联动
                    data_above.get(i).groupFirstIndex = data.size();
                }
            }
            data.addAll(list);
        }
        notifyDataSetChanged();
        return data;
    }

    private void addHandler(View view, ViewHolder holder, GoodsInfo goodsInfo) {
        Integer num = ShoppingCartManager.getInstance().addGoods(goodsInfo);
        goodsInfo.count = num;
        if (num == 1) {
            holder.iv_min.setVisibility(View.VISIBLE);
            holder.tvCount.setVisibility(View.VISIBLE);
            AnimationSet showMinusAnimation = AnimationUtils.getShowMinusAnimation();
            holder.iv_min.startAnimation(showMinusAnimation);
            holder.tvCount.startAnimation(showMinusAnimation);
            holder.tvCount.setText(num + "");
        } else {
            holder.tvCount.setText(num + "");
        }
        flyToShoppingCart(view);
        count = null;
        if (count == null) {
            count = (TextView) container.findViewById(R.id.fragment_goods_tv_count);
        }
        if (num > 0) {
            count.setVisibility(View.VISIBLE);
        }
        Integer totalNum = ShoppingCartManager.getInstance().getTotalNum();
        count.setText(totalNum.toString());
    }


    private void flyToShoppingCart(View view) {
        int[] location=new int[2];
        view.getLocationOnScreen(location);
        //获取目标位置
        int[] targetLocation=new int[2];
        //将Activity加载控件容器获取到
        if (container ==null){
            container = (FrameLayout) UiUtils.getContainder(view,R.id.seller_detail_container);
        }
        container.findViewById(R.id.cart).getLocationOnScreen(targetLocation);
//        location[1]-=UiUtils.STATUE_BAR_HEIGHT;
//        targetLocation[1]-=UiUtils.STATUE_BAR_HEIGHT;
        final ImageView iv=getImageView(location,view);
        container.addView(iv);
        Animation animation= utils.AnimationUtils.getAddAnimation(targetLocation,location);
        iv.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                    container.removeView(iv);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
    private ImageView getImageView(int[] location, View view) {
        ImageView imageView = new ImageButton(mContext);
        imageView.setX(location[0]);
        imageView.setY(location[1]-70);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(70,70));
        imageView.setBackgroundResource(R.mipmap.food_button_add);
        return imageView;
    }

    private void minusHander(ViewHolder viewHolder, GoodsInfo goodsInfo) {
        int num;
        if (Integer.parseInt(viewHolder.tvCount.getText().toString())>1){
            num=Integer.parseInt(viewHolder.tvCount.getText().toString());
            num--;
            goodsInfo.count = num;
            viewHolder.tvCount.setText(num+"");
            Integer totalNum = ShoppingCartManager.getInstance().getTotalNum();
            count.setText(totalNum.toString());

        }else{
            num=0;
            goodsInfo.count = num;
            viewHolder.tvCount.setText(num+"");
            Integer totalNum = ShoppingCartManager.getInstance().getTotalNum();
            if (totalNum>0)
            {
                count.setText(totalNum.toString());
            }else{
                count.setVisibility(View.INVISIBLE);
            }
            viewHolder.iv_min.setVisibility(View.INVISIBLE);
            viewHolder.tvCount.setVisibility(View.INVISIBLE);
        }

    }

    class HeaderViewHolder {
        TextView text;
    }

    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_detail;
        TextView tv_seller_month;
        TextView tv_newprice;
        TextView tv_oldprice;
        ImageView iv_min;
        TextView tv_num;
        ImageView iv_ade;
        int headId;
        int headIndex;
        TextView tvCount;
    }
}