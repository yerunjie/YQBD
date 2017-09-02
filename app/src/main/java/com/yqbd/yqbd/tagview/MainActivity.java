package com.yqbd.yqbd.tagview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.yqbd.yqbd.R;
import com.yqbd.yqbd.tagview.widget.Tag;
import com.yqbd.yqbd.tagview.widget.TagListView;
import com.yqbd.yqbd.tagview.widget.TagView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements OnClickListener {

    private TagListView mTagListView;
    private final List<Tag> mTags = new ArrayList<Tag>();
    private final String[] titles = {"开车", "做饭", "开网店", "摄影",
            "服装搭配", "手工制作", "输入法", "减肥", "最美应用", "按摩", "炒股"};
    int dark = 0xffffff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_tag_activity);

        mTagListView = (TagListView) findViewById(R.id.tagview);
        setUpData();
        mTagListView.setTags(mTags);
        final boolean re = false;
        mTagListView.setOnTagClickListener(new TagListView.OnTagClickListener() {

            @Override
            public void onTagClick(TagView tagView, Tag tag) {
                if (tag.getOr()) {
                    tag.setOr(false);
                    tagView.setBackgroundResource(R.drawable.tag_checked_normal);
                    Toast.makeText(getApplicationContext(), "您取消了" + tagView.getText().toString(), 2000).show();
                } else {
                    tag.setOr(true);
                    Toast.makeText(getApplicationContext(), tagView.getText().toString() + "id" + tag.getId(), 2000).show();
                    tagView.setBackgroundResource(R.drawable.tag_checked_pressed);
                    tagView.setChecked(true);
                }
            }
        });


        findViewById(R.id.b).setOnClickListener(new OnClickListener() {
            //tag的名字字符串
            String str = "";
            String strId = "";

            @Override
            public void onClick(View arg0) {
                for (int i = 0; i < mTags.size(); i++) {
                    Tag tag = mTags.get(i);
                    if (tag.getOr()) {
                        if (str.length() < 2) {
                            str = tag.getTitle();
                            strId = String.valueOf(tag.getId());
                        } else {
                            str = str + "," + tag.getTitle();
                            strId = strId + "," + String.valueOf(tag.getId());
                        }
                    }
                }
                Toast.makeText(getApplicationContext(), str + "---它们的id是" + strId, 2000).show();
//				Toast.makeText(getApplicationContext(),strId, 2000).show();
                str = "";
                strId = "";
            }
        });

    }


    private void setUpData() {
        for (int i = 0; i < 10; i++) {
            Tag tag = new Tag();
            tag.setId(i);
            tag.setChecked(true);
            tag.setTitle(titles[i]);
            mTags.add(tag);
        }
    }

    @Override
    public void onClick(View arg0) {
        Toast.makeText(this, "点了", 2000).show();
    }
}
