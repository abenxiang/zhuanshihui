package com.sina.shopguide.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.sina.shopguide.R;
import com.sina.shopguide.util.HistotySearchPreferences;


//搜索界面
@SuppressLint("InflateParams")
public class SearchActivity extends BaseActivity implements
        OnClickListener {
    private EditText etContent;
    private TextView tvCancel;
    private LinearLayout lyDefContent;
    private ListView lvHistorry;

    private String lastHisotys;
    private List<String> listHisotyStrings = new ArrayList<String>();
    private List<String> orgHisotyStrings = new ArrayList<String>();
    private TextView tvSearchHint;
    private List<String> targList;

    private boolean hasFooterView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_search);
        lyDefContent = (LinearLayout) findViewById(R.id.search_default_content);
        etContent = (EditText) findViewById(R.id.search_content);
        tvCancel = (TextView) findViewById(R.id.search_cancel);
        lvHistorry = (ListView) findViewById(R.id.search_lstv);
        tvSearchHint = (TextView) findViewById(R.id.search_hint);

        lvHistorry.setDivider(null);
        adapter = new HisListAdapter();
        lvHistorry.setAdapter(adapter);
        lvHistorry.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position < listHisotyStrings.size()) {
                    etContent.setText(listHisotyStrings.get(position));

                    lyDefContent.setVisibility(View.GONE);
                    etContent.setVisibility(View.VISIBLE);
                    startSearch();
                }
            }
        });


        etContent.setVisibility(View.GONE);

        lyDefContent.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        findViewById(R.id.search_default_content_fr).setOnClickListener(this);

        etContent.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    switch (event.getAction()) {
                        case KeyEvent.ACTION_UP:
                            startSearch();
                            break;
                    }
                    return true;
                }
                return false;
            }
        });

        targList = new ArrayList<String>();
        etContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String target = etContent.getText().toString();
                targList.clear();
                for (String item : orgHisotyStrings) {
                    if (StringUtils.isNotEmpty(target) && item.contains(target)) {
                        targList.add(item);
                    }
                }
                listHisotyStrings = targList;
                if (listHisotyStrings != null)
                    adapter.notifyDataSetChanged();
            }
        });
    }

    private void updateHistory() {
        lastHisotys = HistotySearchPreferences.getHistoryInfo();

        if (lastHisotys != null && lastHisotys.length() != 0) {
            String[] hisarray = lastHisotys
                    .split(HistotySearchPreferences.SPLITE);
            if (hisarray != null && hisarray.length != 0) {

                listHisotyStrings.clear();
                orgHisotyStrings.clear();
                listHisotyStrings.addAll(Arrays.asList(hisarray));
                orgHisotyStrings.addAll(listHisotyStrings);

                if (!hasFooterView) {
                    View v = LayoutInflater.from(SearchActivity.this).inflate(
                            R.layout.vw_tv_footer, null);
                    v.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub
                            listHisotyStrings.clear();
                            orgHisotyStrings.clear();
                            adapter.notifyDataSetChanged();
                            HistotySearchPreferences.setHistoryInfo("");
                            lvHistorry.removeFooterView(arg0);
                            hasFooterView = false;
                        }
                    });
                    lvHistorry.addFooterView(v);
                    hasFooterView = true;
                }

                adapter.notifyDataSetChanged();
            }
        }
    }

    public void onResume() {
        super.onResume();
        updateHistory();
    }

    public void startSearch() {
        String scontent = etContent.getText().toString();
        if (scontent.length() == 0) {
            Toast.makeText(SearchActivity.this, "请输入查询内容", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent in = null;
        in = new Intent(SearchActivity.this, SearchResultActivity.class);

        in.putExtra(SearchResultActivity.SEARCH_CONTENT, scontent);
        in.putExtra(SearchResultActivity.SEARCH_FROM, -1);
        etContent.setText("");
        listHisotyStrings.clear();
        listHisotyStrings.addAll(orgHisotyStrings);
        startActivity(in);
        String newHis;
        if (orgHisotyStrings.size() == 0) {
            newHis = scontent;
        } else {
            //去重
            if (orgHisotyStrings.contains(scontent)) {
                newHis = lastHisotys;
            } else {
                newHis = scontent + HistotySearchPreferences.SPLITE + lastHisotys;
            }
        }
        // listHisotyStrings.add(etContent.getText().toString());
        HistotySearchPreferences.setHistoryInfo(newHis);
    }

    public class HisHolder {
        public TextView tv;
    }

    private HisListAdapter adapter;

    private class HisListAdapter extends BaseAdapter {
        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            String ss = listHisotyStrings.get(position);
            View v = convertView;
            HisHolder holder;
            if (convertView == null) {
                holder = new HisHolder();
                v = LayoutInflater.from(SearchActivity.this).inflate(
                        R.layout.vw_tv_item, null);
                holder.tv = (TextView) v.findViewById(R.id.his_text);
                v.setTag(holder);
            } else {
                holder = (HisHolder) v.getTag();
            }

            holder.tv.setText(ss);

            return v;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return listHisotyStrings.size();// + 1;
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_default_content:
            case R.id.search_default_content_fr:
                lyDefContent.setVisibility(View.GONE);
                etContent.setVisibility(View.VISIBLE);
                etContent.postDelayed(new Runnable() {
                    public void run() {
                        etContent.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(etContent, 0);
                    }
                }, 100);
                break;
            case R.id.search_cancel:
                finish();
                break;

        }
    }

}
