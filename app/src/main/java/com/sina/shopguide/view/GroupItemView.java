package com.sina.shopguide.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.shopguide.R;
import com.sina.shopguide.dialog.MemberInfoDlg;
import com.sina.shopguide.dto.Group;
import com.sina.shopguide.util.ImageLoaderUtils;

import org.apache.commons.lang3.StringUtils;


/**
 * Created by tiger on 18/5/7.
 */

public class GroupItemView extends RelativeLayout implements IUpdate<Group>{

    private CircleImageView civAvatar;
    private TextView tvName;
    private TextView tvCurrCount;
    private TextView tvLastCount;
    private boolean hieFrom = false;

    private Group item;
    public GroupItemView(Context context, AttributeSet attrs,
                         int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public GroupItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public GroupItemView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.vw_listitem_group, this);

        civAvatar =(CircleImageView)findViewById(R.id.id_lavatar);
        tvName=(TextView)findViewById(R.id.id_lname);
        tvCurrCount =(TextView)findViewById(R.id.id_lcurrcount);
        tvLastCount=(TextView)findViewById(R.id.id_llastcount);
        civAvatar.setOnClickListener(clickListener);
    }

    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            MemberInfoDlg dlg = new MemberInfoDlg(getContext(),item);
            dlg.show();
        }
    };

    public void update(Group it){
        if(it == null){
            return;
        }
        item = it;
        tvName.setText(item.getNick_name());
        tvCurrCount.setText("¥"+item.getThismonth_contribute());
        tvLastCount.setText("¥"+item.getLastmonth_contribute());

        if(StringUtils.isNotEmpty(item.getAvatar_url())) {
            ImageLoader.getInstance().displayImage(item.getAvatar_url(), civAvatar,
                    ImageLoaderUtils.getDefaultoptions());
        }
    }
}
