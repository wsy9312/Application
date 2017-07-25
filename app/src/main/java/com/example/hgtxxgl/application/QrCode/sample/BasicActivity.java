package com.example.hgtxxgl.application.QrCode.sample;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtils;

public class BasicActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
//        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        StatusBarUtils.setWindowStatusBarColor(this,R.color.mainColor_blue);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initTotal();
    }

    private void initTotal() {
        toolbar.inflateMenu(R.menu.base_toolbar_menu);//设置右上角的填充菜单
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.action_item1) {
                    Toast.makeText(BasicActivity.this , "1" , Toast.LENGTH_SHORT).show();

                } else if (menuItemId == R.id.action_item2) {
                    Toast.makeText(BasicActivity.this , "2" , Toast.LENGTH_SHORT).show();

                }
                return true;
            }
        });

    }
}
