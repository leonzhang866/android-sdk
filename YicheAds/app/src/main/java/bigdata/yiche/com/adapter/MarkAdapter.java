package bigdata.yiche.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import java.util.zip.Inflater;

import bigdata.yiche.com.R;
import bigdata.yiche.com.bean.TagBean;

/**
 * Created by yiche on 16/10/10.
 */
public class MarkAdapter extends BaseAdapter{
    private Context mContext;
    private List<TagBean> mList;
    private LayoutInflater mInflater;

    public MarkAdapter(Context context, List<TagBean> list) {
        this.mList=list;
        this.mContext=context;
        mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_mark, null);
            holder = new ViewHolder();
            holder.nameTextView = (TextView) convertView.findViewById(R.id.tv_mark);
            holder.valueTextView = (TextView) convertView.findViewById(R.id.tv_value);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nameTextView.setText(mList.get(i).getName());
        holder.valueTextView.setText(mList.get(i).getValue()+"");
        return convertView;
    }

    static class ViewHolder {
        public TextView nameTextView;
        public TextView valueTextView;
    }
}
