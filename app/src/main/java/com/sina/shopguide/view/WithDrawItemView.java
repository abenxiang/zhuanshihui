package com.sina.shopguide.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sina.shopguide.R;
import com.sina.shopguide.dto.WithDrawItem;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by tiger on 18/5/7.
 */

public class WithDrawItemView extends RelativeLayout implements IUpdate<WithDrawItem>{

    private TextView tvTime;
    private TextView tvCount;
    private TextView tvFrom;
    private boolean hieFrom = false;

    private WithDrawItem item;
    public WithDrawItemView(Context context, AttributeSet attrs,
                            int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public WithDrawItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public WithDrawItemView(Context context) {
        super(context);
        init(null);
    }

    public WithDrawItemView(Context context,boolean hidefrom) {
        super(context);
        hieFrom = hidefrom;
        init(null);
    }

    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.vw_listitem_withdraw, this);

        tvTime =(TextView)findViewById(R.id.id_ltime);
        tvCount =(TextView)findViewById(R.id.id_lcount);
        tvFrom=(TextView)findViewById(R.id.id_lfrom);
        if(hieFrom){
            tvFrom.setVisibility(View.GONE);
        }
    }

    public void update(WithDrawItem it){
        if(it == null){
            return;
        }
        item = it;
        tvTime.setText(item.getCtime());
        if(Float.parseFloat(item.getAmount())>0.00){
            tvCount.setText("+"+item.getAmount());
        }else {
            tvCount.setText(item.getAmount());
        }
        if(StringUtils.isNotEmpty(item.getFrom()) && tvFrom.getVisibility() == View.VISIBLE){
            tvFrom.setText(item.getFrom());
        }
    }
}
