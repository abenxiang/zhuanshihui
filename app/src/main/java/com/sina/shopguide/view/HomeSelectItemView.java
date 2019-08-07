package com.sina.shopguide.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.shopguide.R;
import com.sina.shopguide.activity.ZhuantiDetailActivity;
import com.sina.shopguide.dto.zhuanti;
import com.sina.shopguide.util.ImageLoaderUtils;

public class HomeSelectItemView extends RelativeLayout  implements
							IUpdate<zhuanti>{
	private ImageView ivImgHot;
	private TextView tvTitle;

	private zhuanti topic;
	
	public HomeSelectItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public HomeSelectItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public HomeSelectItemView(Context context) {
		super(context);
		init();
	}

	private void init() {
		inflate(getContext(), R.layout.vw_home_select_item, this);
		this.setBackgroundColor(getResources().getColor(R.color.colorWhite));
		ivImgHot= (ImageView) findViewById(R.id.img_hot);
		tvTitle= (TextView) findViewById(R.id.tv_title);
		setOnClickListener(mClick);
	}
	
	@Override
	public void update(zhuanti data) {
		if (data == null || topic == data) {
			return;
		}
		topic = data;
		update();
	}
	
	private void update(){
		ImageLoader.getInstance().displayImage(topic.getPicUrl(), ivImgHot,
				ImageLoaderUtils.getDefaultoptions());
		tvTitle.setText(topic.getTitle());
	}
	
	private OnClickListener mClick = new OnClickListener() {
		public void onClick(View v) {
			Intent in = new Intent(getContext(), ZhuantiDetailActivity.class);
			in.putExtra(ZhuantiDetailActivity.PID, topic.getId());
			getContext().startActivity(in);
		}
	};
	
}
