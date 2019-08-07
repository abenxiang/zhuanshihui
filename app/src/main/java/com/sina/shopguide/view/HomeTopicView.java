package com.sina.shopguide.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sina.shopguide.R;
import com.sina.shopguide.dto.zhuanti;

import java.util.List;

public class HomeTopicView extends RelativeLayout implements IUpdate<List<zhuanti>> {
	private HomeTopicItemVView[] ncivItemArray;
	private HomeTopicItemHView[] civItemArray;
	private List<zhuanti> listChoice;
	
	public HomeTopicView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public HomeTopicView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public HomeTopicView(Context context) {
		super(context);
		init();
	}

	@Override
	public void update(List<zhuanti> list) {
		listChoice = list;
		int size = listChoice.size();
		if(size<5){
			return;
		}
		for(int i=0;i<2;i++){
			ncivItemArray[i].update(listChoice.get(i));
		}
		
		for(int i=2;i<size;i++){
			civItemArray[i-2].update(listChoice.get(i), i);
		}
	}
	
	private void init() {
		ncivItemArray = new HomeTopicItemVView[2];
		civItemArray = new HomeTopicItemHView[4];
		inflate(getContext(), R.layout.vw_home_topic, this);
		this.setBackgroundColor(getResources().getColor(R.color.colorWhite));
		civItemArray[0]= (HomeTopicItemHView) findViewById(R.id.chioce_item1);
		civItemArray[1]= (HomeTopicItemHView) findViewById(R.id.chioce_item2);
		civItemArray[2]= (HomeTopicItemHView) findViewById(R.id.chioce_item3);
		civItemArray[3]= (HomeTopicItemHView) findViewById(R.id.chioce_item4);

		ncivItemArray[0]= (HomeTopicItemVView) findViewById(R.id.chioce_v_item1);
		ncivItemArray[1]= (HomeTopicItemVView) findViewById(R.id.chioce_v_item2);
	}
}
