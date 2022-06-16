package com.example.addressbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MyHelper myHelper;
    private EditText mEtName;
    private EditText mEtPhone;
    private TextView mTvShow;
    private Button mBtnAdd;
    private Button mBtnQuery;
    private Button mBtnEdit;
    private Button mBtnDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myHelper = new MyHelper(this);
        init();
    }

    public void init() {
        mTvShow = findViewById(R.id.tv_show);
        mEtName = findViewById(R.id.et_name);
        mEtPhone = findViewById(R.id.et_phone);
        mBtnAdd = findViewById(R.id.btn_add);
        mBtnQuery = findViewById(R.id.btn_query);
        mBtnEdit = findViewById(R.id.btn_update);
        mBtnDelete = findViewById(R.id.btn_delete);
        mBtnAdd.setOnClickListener(this);
        mBtnQuery.setOnClickListener(this);
        mBtnEdit.setOnClickListener(this);
        mBtnDelete.setOnClickListener(this);

    }

    public void onClick(View view) {
        String name,phone;//声明姓名和电话号码
        SQLiteDatabase sqLiteDatabase;//声明一个数据库sqLiteDatabase
        ContentValues values;//创建values对象
        switch (view.getId()) {
            case R.id.btn_add://添加数据
                name = mEtName.getText().toString();//获取字符串数据
                phone = mEtPhone.getText().toString();
                sqLiteDatabase = myHelper.getWritableDatabase();//获取可读写SQLiteDatabase对象
                values = new ContentValues();//创建ContentValues值
                values.put("name",name);//将数据添加到ContentValues对象
                values.put("phone",phone);
                sqLiteDatabase.insert("person",null,values);//将数据插入列表当中
                Toast.makeText(this,"联系人已添加",Toast.LENGTH_SHORT).show();//Toast提示信息
                sqLiteDatabase.close();//关闭数据库
                break;
            case R.id.btn_query://查询数据
                sqLiteDatabase = myHelper.getReadableDatabase();
                Cursor cursor = sqLiteDatabase.query("person",null,null,null,null,null,null);
                if (cursor.getCount()==0) {//获取数据总条数
                    mTvShow.setText("");
                    Toast.makeText(this,"没有数据",Toast.LENGTH_SHORT).show();
                } else {
                    cursor.moveToFirst();
                    mTvShow.setText("Name:"+cursor.getString(1)+";Tel:"+cursor.getString(2));
                }
                while (cursor.moveToNext()) {
                    mTvShow.append("\n"+"Name:"+cursor.getString(1)+";Tel:"+cursor.getString(2));
                }
                cursor.close();
                sqLiteDatabase.close();
                break;
            case R.id.btn_update://编辑/修改 数据
                sqLiteDatabase = myHelper.getReadableDatabase();
                values = new ContentValues();
                values.put("phone",phone=mEtPhone.getText().toString());
                sqLiteDatabase.update("person",values,"name=?",new String[]{mEtName.getText().toString()});
                Toast.makeText(this,"联系人信息已修改",Toast.LENGTH_SHORT).show();
                sqLiteDatabase.close();
                break;
            case R.id.btn_delete://删除数据
                sqLiteDatabase = myHelper.getReadableDatabase();
                sqLiteDatabase.delete("person",null,null);
                Toast.makeText(this,"联系人已删除",Toast.LENGTH_SHORT).show();
                mTvShow.setText("");
                sqLiteDatabase.close();
                break;

        }
    }

}
class MyHelper extends SQLiteOpenHelper {
    public MyHelper(Context context) {
        super(context,"addressbook.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE person(_id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(20),phone VARCHAR(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}