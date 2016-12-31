package com.example.shana.listview;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "ArrayAdapter");
        menu.add(0, 2, 0, "SimpleAdapter");
        menu.add(0, 3, 0, "SimpleCursorAdapter");
        menu.add(0, 4, 0, "BaseAdapter");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                ArrayAdapter();
                break;
            case 2:
                SimpleAdapter();
                break;
            case 3:
                SimpleCursorAdapter();
                break;
            case 4:
                BaseAdapter();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //    简单适配器
    private void SimpleAdapter() {
        setTitle("SimpleAdapter");
        String[] arr_str = {"pic", "title", "content"};
        int[] img_arr = {R.id.pic, R.id.title, R.id.content};
//        上下文，数据源，布局文件，键，值
        SimpleAdapter sa = new SimpleAdapter(this, getData(), R.layout.item, arr_str, img_arr);
        listView.setAdapter(sa);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("介绍")
                        .setMessage("This is " + getData().get(position).get("title").toString())
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
            }
        });

    }

    // 设置数据源
    public List<Map<String, Object>> getData() {
        final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ;
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("pic", R.drawable.kotori);
        map.put("title", "kotori");
        map.put("content", "my love!");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("pic", R.drawable.reimu13);
        map.put("title", "reimu");
        map.put("content", "my love!");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("pic", R.drawable.shana4);
        map.put("title", "shana");
        map.put("content", "my love!");
        list.add(map);

        return list;
    }


    //游标适配器
    private void SimpleCursorAdapter() {
        setTitle("SimpleCursorAdapter");
        final Cursor mcursor=getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        startManagingCursor(mcursor);
        SimpleCursorAdapter madapter=new SimpleCursorAdapter(this,
                android.R.layout.simple_expandable_list_item_1,
                mcursor,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME},
                new int[]{android.R.id.text1});
        listView.setAdapter(madapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,mcursor.getString(mcursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),Toast.LENGTH_LONG).show();
            }
        });
    }
    //基本适配器
    private void BaseAdapter() {
        setTitle("BaseAdapter");
        class myAdapter extends BaseAdapter{
            private List<Map<String, Object>> list;
            private LayoutInflater mInflater;
            private Context context;

            myAdapter(Context context, List<Map<String, Object>> mlist) {
                this.context = context;
                this.list = mlist;
                this.mInflater = LayoutInflater.from(context);
            }

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return list.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder viewholder;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item, null);
                    viewholder=new ViewHolder();//最重要的一点= =
                    viewholder.pic = (ImageView) convertView.findViewById(R.id.pic);
                    viewholder.title = (TextView) convertView.findViewById(R.id.title);
                    viewholder.content = (TextView) convertView.findViewById(R.id.content);
                    viewholder.layout = (LinearLayout) convertView.findViewById(R.id.LL);
                    convertView.setTag(viewholder);
                } else {
                    viewholder = (ViewHolder) convertView.getTag();
                }
                viewholder.pic.setImageResource(Integer.parseInt(list.get(position).get("pic").toString()));
                viewholder.title.setText(list.get(position).get("title").toString());
                viewholder.content.setText(list.get(position).get("content").toString());
                if(position%2==0){
                    viewholder.layout.setBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));
                }else{
                    viewholder.layout.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
                }
                return convertView;

            }

            class ViewHolder {
                ImageView pic;
                TextView title;
                TextView content;
                LinearLayout layout;
            }

        }
        myAdapter ma=new myAdapter(MainActivity.this,getData());
        listView.setAdapter(ma);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("介绍")
                        .setMessage("This is " + getData().get(position).get("title").toString())
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
            }
        });
    }

    //数组适配器
    public void ArrayAdapter(){
        setTitle("ArrayAdapter");
        final String []str={"周日","周一","周二","周三","周四","周五","周六"};
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,str);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,str[position],Toast.LENGTH_SHORT).show();
            }
        });
    }
}

