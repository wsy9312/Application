package com.example.myapplication;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {
    private Button button;
    private ImageView pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        pic = (ImageView) findViewById(R.id.pic);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO 自动生成的方法存根
                System.out.println("onClick");
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO 自动生成的方法存根
        System.out.println(requestCode+"");
        if(requestCode==1)
        {
            //获得图片的uri
            Uri uri = data.getData();
            //外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
            ContentResolver cr = this.getContentResolver();
            Bitmap bitmap;
            //Bitmap bm; //这是一种方式去读取图片
            try
            {
                //bm = MediaStore.Images.Media.getBitmap(cr, uri);
                //pic.setImageBitmap(bm);
                bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                pic.setImageBitmap(bitmap);
                System.out.println("GOOD");
                //第一种方式去读取路径
                //String[] proj = {MediaStore.Images.Media.DATA};
                /*
                 //好像是android多媒体数据库的封装接口，具体的看Android文档
                Cursor cursor = managedQuery(uri, proj, null, null, null);
                //按我个人理解 这个是获得用户选择的图片的索引值
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
              //将光标移至开头 ，这个很重要，不小心很容易引起越界
                cursor.moveToFirst();
                //最后根据索引值获取图片路径
                String path = cursor.getString(column_index);
                System.out.println(path);
                   */
                //第二种方式去读取路径
                Cursor cursor =this.getContentResolver().query(uri, null, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(column_index);
                System.out.println(path);
            }
            catch (Exception e)
            {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
                System.out.println("BAD");
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
