package com.sina.shopguide.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.sina.shopguide.R;
import com.sina.shopguide.dto.zhuanti;

import java.util.List;

public class HomeSelectView extends RelativeLayout implements IUpdate<List<zhuanti>>  {
	private HomeSelectItemView[] civItemArray;
	private List<zhuanti> listChoice;
	
	public HomeSelectView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public HomeSelectView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public HomeSelectView(Context context) {
		super(context);
		init();
	}

	@Override
	public void update(List<zhuanti> list) {
		listChoice = list;
		for(int i=0;i<listChoice.size();i++){
			civItemArray[i].update(listChoice.get(i));
		}
	}
	
	private void init() {
		inflate(getContext(), R.layout.vw_home_select, this);
		civItemArray = new HomeSelectItemView[4];
		civItemArray[0]= (HomeSelectItemView) findViewById(R.id.chioce_item1);
		civItemArray[1]= (HomeSelectItemView) findViewById(R.id.chioce_item2);
		civItemArray[2]= (HomeSelectItemView) findViewById(R.id.chioce_item3);
		civItemArray[3]= (HomeSelectItemView) findViewById(R.id.chioce_item4);
		this.setBackgroundColor(getResources().getColor(R.color.colorWhite));
	}
	
}
