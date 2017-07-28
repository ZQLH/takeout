package ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.AndroidCharacter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lala.heimawaimaizhunbei.R;

import java.util.ArrayList;
import java.util.List;

import model.net.bean.GoodsTypeInfo;

import static com.example.lala.heimawaimaizhunbei.R.color.colorTextDefault;

/**
 * Created by lala on 2017/7/24.
 */

public class MyAdapter extends BaseAdapter {
    public List<GoodsTypeInfo> data;
    Context mContext;
    private int selectedHeadIndex;

    public MyAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        if (data!=null){
            return data.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(mContext,R.layout.item_left,null);
            holder.itemView= (TextView) convertView.findViewById(R.id.item_left);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.itemView.setText(data.get(position).name);
        if (position==selectedHeadIndex){  holder.itemView.setBackgroundColor(Color.WHITE);
        }else {
            holder.itemView.setBackgroundColor(android.graphics.Color.parseColor("#b1ded5d5"));
        }

        return convertView;
    }

    public void setData(ArrayList<GoodsTypeInfo> data)
    {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setSelectedPosition(int position) {
        selectedHeadIndex=position;
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView itemView;


    }

}
