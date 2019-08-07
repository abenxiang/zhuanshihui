package com.sina.shopguide.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import com.sina.shopguide.R;
import com.sina.shopguide.app.LabelIndicatorStrategy;
import com.sina.shopguide.fragment.HomeFragment;
import com.sina.shopguide.fragment.MeFragment;
import com.sina.shopguide.fragment.ZhuantiFragment;
import com.sina.shopguide.util.PermissionUtils;
import com.sina.shopguide.view.DoubleClickListener;

import java.util.HashMap;

//主界面
public class MainActivity extends BaseActivity {
    public static final String MAIN_INDEX="index";
    private TabHost tabHost;

    private TabManager tabManager;

    private View homeTabView;

    private String strTabHome = "";
    private String strTabZhuanti = "";
    private String strTabMe = "";
    private long lastClickTime = 0;
    private int currTabIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabHost = ((TabHost) findViewById(android.R.id.tabhost));
        tabHost.setup();
        tabManager = new TabManager(this, tabHost, R.id.containerid);
        strTabHome = getResources().getString(R.string.main_tab_home);
        strTabZhuanti=getResources().getString(R.string.main_tab_zhuanti);
        strTabMe = getResources().getString(R.string.main_tab_me);

        tabManager.addTab(tabHost.newTabSpec(strTabHome).setIndicator(
                        homeTabView = new LabelIndicatorStrategy(this, strTabHome, R.layout.main_tab_home)
                                .createIndicatorView(tabHost)), HomeFragment.class, null);
        addTab(strTabZhuanti, R.layout.main_tab_zhuanti, ZhuantiFragment.class, null);
        addTab(strTabMe, R.layout.main_tab_me, MeFragment.class, null);


        homeTabView.setOnTouchListener(new DoubleClickListener(MainActivity.this) {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                final HomeFragment home = (HomeFragment) getSupportFragmentManager().findFragmentByTag(strTabHome);
                if(home != null) {
                    home.scrollToHead();
                }
                return false;
            }
        });

        // 申请存储权限
        PermissionUtils.requestPermission(this, PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE,
                new PermissionUtils.PermissionGrant() {
                    @Override
                    public void onPermissionGranted(int requestCode) {
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE) {
            final HomeFragment home = (HomeFragment) getSupportFragmentManager().findFragmentByTag(strTabHome);
            home.refresh();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - lastClickTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次后退键退出程序",
                        Toast.LENGTH_SHORT).show();
                lastClickTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //AndfixProcessor.inject(this);
    }

    @Override
    public void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        currTabIndex = intent.getIntExtra(MAIN_INDEX,0);
        if(tabHost !=null){
            tabHost.setCurrentTab(currTabIndex);
        }
    }
    private void addTab(String title, int resource, Class<?> clazz,
                        Bundle params) {
        tabManager.addTab(
                tabHost.newTabSpec(title).setIndicator(
                        new LabelIndicatorStrategy(this, title, resource)
                                .createIndicatorView(tabHost)), clazz, params);
    }

    public void changeTab(String tab) {
        if ("".equals(tab)) {
            return;
        }
        tabHost.setCurrentTabByTag(tab);
    }

    class TabManager implements TabHost.OnTabChangeListener {

        private final BaseActivity activity;

        private final int containerId;

        private TabInfo lastTab;

        private final TabHost tabHost;

        private final HashMap<String, TabInfo> tabs = new HashMap<String, TabInfo>();

        public TabManager(BaseActivity activity, TabHost tabHost,
                          int containerId) {
            this.activity = activity;
            this.tabHost = tabHost;
            this.containerId = containerId;
            this.tabHost.setOnTabChangedListener(this);
        }

        public void addTab(TabSpec tabSpec, Class<?> clazz, Bundle params) {
            tabSpec.setContent(new DummyTabFactory(activity));
            String tag = tabSpec.getTag();
            TabInfo tab = new TabInfo(tag, clazz, params);
            tab.fragment = activity.getSupportFragmentManager()
                    .findFragmentByTag(tag);

            if (tab.fragment != null && !tab.fragment.isHidden()) {
                FragmentTransaction transaction = activity
                        .getSupportFragmentManager().beginTransaction();
                transaction.hide(tab.fragment);
                transaction.commit();
            }
            tabs.put(tag, tab);
            tabHost.addTab(tabSpec);
        }

        public TabInfo getLastTab() {
            return lastTab;
        }

        @Override
        public void onTabChanged(String id) {
            if ("tmp".equals(id)) {
                return;
            }

            TabInfo currentTab = tabs.get(id);

            FragmentTransaction transaction = null;
            transaction = activity.getSupportFragmentManager()
                    .beginTransaction();

            if (currentTab != null && currentTab.fragment == null) {
                currentTab.fragment = Fragment.instantiate(activity,
                        currentTab.clazz.getName(), currentTab.params);
                transaction.add(containerId, currentTab.fragment,
                        currentTab.tag);
            }

            if (lastTab != currentTab) {
                if (lastTab != null && lastTab.fragment != null) {
                    transaction.hide(lastTab.fragment);
                }

                if (currentTab != null && currentTab.fragment != null) {
                    transaction.show(currentTab.fragment);
                }
                transaction.commit();
                lastTab = currentTab;
            }
            activity.getSupportFragmentManager().executePendingTransactions();
        }
    }

    static final class TabInfo {

        private final Bundle params;

        private final Class<?> clazz;

        private Fragment fragment;

        private final String tag;

        TabInfo(String tag, Class<?> clazz, Bundle params) {
            this.tag = tag;
            this.clazz = clazz;
            this.params = params;
        }
    }

    static class DummyTabFactory implements TabHost.TabContentFactory {

        private final Context context;

        public DummyTabFactory(Context context) {
            this.context = context;
        }

        public View createTabContent(String tag) {
            View view = new View(context);
            view.setMinimumWidth(0);
            view.setMinimumHeight(0);
            return view;
        }
    }

}
