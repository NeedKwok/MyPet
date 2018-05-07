package com.example.mypet.ui;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import com.example.mypet.service.PetWindowService;
import com.example.mypet.service.mmwechatListenerService;
import com.example.mypet.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    //Accessibility权限标志
    private boolean accessibilityIsOpen = false;
    private final int REQUEST_ACCESSIBILITY = 1;
    private final int REQUEST_FLOAT_WINDOW = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawer_main);
        NavigationView navigationView = findViewById(R.id.navigation_main);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }

        //navigationView.setCheckedItem(R.id.user_info);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.user_info:
                        Intent it = new Intent(MainActivity.this, UserInfoActivity.class);
                        startActivity(it);
                        break;
                    case R.id.pet_switch:
                        if (Build.VERSION.SDK_INT >= 23) {
                            if (!Settings.canDrawOverlays(getApplicationContext())) {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivityForResult(intent, REQUEST_FLOAT_WINDOW);
                            } else {
                                Intent it2 = new Intent(MainActivity.this, PetWindowService.class);
                                startService(it2);
                            }
                        }
                        break;
                    case R.id.pet_mmListen:
                        //检查Accessibility权限
                        if (checkAccessibilityPermission()) {
                            //新线程开启服务
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    mmwechatListenerService.isRunning = true;
                                    startService(new Intent(MainActivity.this, mmwechatListenerService.class));
                                }
                            }).start();
                        } else {
                            //申请Accessibility权限
                            requestAccessibilityPermission();
                        }
                        break;
                    case R.id.pet_blt:
                        Intent blt = new Intent(MainActivity.this, BLTinfoActivity.class);
                        startActivity(blt);
                        break;
                    default:
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private long firstTime = 0;

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            //不存在连按两次退出
            if (System.currentTimeMillis() - firstTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.item1:
                Intent it = new Intent(MainActivity.this, NewAlarmClockActivity.class);
                startActivity(it);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    //Accessibility权限检查
    private boolean checkAccessibilityPermission() {
        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> accessibilityServices = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        //遍历所有的AccessibilityService 如果没有本服务，则表示没开启
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId().equals("com.example.mypet/.service.mmwechatListenerService")) {
                accessibilityIsOpen = true;
                return true;
            }
        }
        return false;
    }

    //Accessibility权限申请
    private void requestAccessibilityPermission() {
        if (!accessibilityIsOpen) {
            Toast.makeText(this, "请打开本应用的权限", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivityForResult(intent, REQUEST_ACCESSIBILITY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ACCESSIBILITY:
                AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
                List<AccessibilityServiceInfo> accessibilityServices = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
                for (AccessibilityServiceInfo info : accessibilityServices) {
                    if (info.getId().equals("com.example.mypet/.service.mmwechatListenerService")) {
                        accessibilityIsOpen = true;
                        mmwechatListenerService.isRunning = true;
                        startService(new Intent(this, mmwechatListenerService.class));
                        Toast.makeText(this, "微信消息提醒已开启", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Toast.makeText(this, "微信消息提醒未开启", Toast.LENGTH_SHORT).show();
                break;
            case REQUEST_FLOAT_WINDOW:
                if (Build.VERSION.SDK_INT >= 23) {
                    if (!Settings.canDrawOverlays(getApplicationContext())) {
                        Toast.makeText(this, "未开启悬浮窗权限", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent it = new Intent(MainActivity.this, PetWindowService.class);
                        startService(it);
                    }
                }
                break;
        }

    }

}
