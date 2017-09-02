package com.yqbd.yqbd.activities.task;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.yqbd.yqbd.BaseActivity;
import com.yqbd.yqbd.R;
import com.yqbd.yqbd.actions.GroupActionImpl;
import com.yqbd.yqbd.actions.TaskActionImpl;
import com.yqbd.yqbd.beans.GroupInfoBean;
import com.yqbd.yqbd.beans.ImageBean;
import com.yqbd.yqbd.beans.TaskBean;
import com.yqbd.yqbd.beans.TypeBean;
import com.yqbd.yqbd.fragments.DataCallBack;
import com.yqbd.yqbd.fragments.DatePickerFragment;
import com.yqbd.yqbd.tagview.widget.Tag;
import com.yqbd.yqbd.tagview.widget.TagListView;
import com.yqbd.yqbd.tagview.widget.TagView;
import com.yqbd.yqbd.utils.BaseJson;
import com.yqbd.yqbd.utils.BitmapUtils;
import com.yqbd.yqbd.utils.UploadFileUtils;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class PublishLongTermTaskActivity extends BaseActivity<TaskBean> implements View.OnClickListener, DataCallBack {


    public static final int REQUEST_CODE_PICK_IMAGE = 200;
    public static final int REQUEST_CODE_CAPTURE_CAMEIA = 201;

    private TagListView tagview;
    private final List<Tag> mTags = new ArrayList<Tag>();
    private List<TypeBean> typeBeans;
    private ArrayList<GroupInfoBean> groupInfoBeans;
    private Spinner spinner;
    private Integer targetGroupId = 0;//默认不限组别
    private ImageView imageView;
    private ImageBean imageBean;
    private Date date;
    private TaskActionImpl taskAction = new TaskActionImpl(this);
    private GroupActionImpl groupAction = new GroupActionImpl(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_long_term_task);

        tagview = (TagListView) findViewById(R.id.tagview);
        findViewById(R.id.task_publish).setOnClickListener(this);
        findViewById(R.id.dealline).setOnClickListener(this);
        findViewById(R.id.upload_picture).setOnClickListener(this);
        imageView = (ImageView) findViewById(R.id.imageView);
        spinner = (Spinner) findViewById(R.id.target_group);
        initializeTop(true,"发布长期任务");
        localPost(new Integer(1));
        localPost(new Double(1.1));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventAsync(Double temp) {
        try {
            BaseJson baseJson = taskAction.getAllTypes();
            typeBeans = new ArrayList<>();
            JSONArray jsonArray = baseJson.getJSONArray();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tempJsonObject = jsonArray.getJSONObject(i);
                typeBeans.add(new TypeBean(tempJsonObject));
            }
            localPost(typeBeans);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(List<TypeBean> typeBeans) {
        setUpData();
        tagview.setTags(mTags);
        final boolean re = false;
        tagview.setOnTagClickListener(new TagListView.OnTagClickListener() {
            @Override
            public void onTagClick(TagView tagView, Tag tag) {
                if (tag.getOr()) {
                    tag.setOr(false);
                    tagView.setBackgroundResource(R.drawable.tag_checked_normal);
                    //Toast.makeText(getApplicationContext(), "您取消了" + tagView.getText().toString(), 2000).show();
                } else {
                    tag.setOr(true);
                    //Toast.makeText(getApplicationContext(), tagView.getText().toString() + "id" + tag.getId(), 2000).show();
                    tagView.setBackgroundResource(R.drawable.tag_checked_pressed);
                    tagView.setChecked(true);
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventAsync(Integer tmp) {
        try {
            BaseJson baseJson = groupAction.getCompanyGroups();
            groupInfoBeans = new ArrayList<>();
            JSONArray jsonArray = baseJson.getJSONArray();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tempJsonObject = jsonArray.getJSONObject(i);
                groupInfoBeans.add(new GroupInfoBean(tempJsonObject));
            }

            Set<String> strs = new HashSet<>();
            for (int i = 0; i< groupInfoBeans.size(); i++){
                GroupInfoBean groupInfoBean = groupInfoBeans.get(i);
                strs.add(groupInfoBean.getGroupId() + "  " + groupInfoBean.getGroupTitle());
            }
            localPost(strs);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Set<String> groupStrs) {
        String[] strTemplate = new String[groupStrs.size()];
        ArrayAdapter adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,groupStrs.toArray(strTemplate));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                targetGroupId = groupInfoBeans.get(position).getGroupId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpData() {
        for (TypeBean typeBean : typeBeans) {
            Tag tag = new Tag();
            tag.setId(typeBean.getTypeId());
            tag.setChecked(true);
            tag.setTitle(typeBean.getTypeName());
            mTags.add(tag);
        }
    }

    @Override
    protected void onEventMainThread(BaseJson argument) {
        Log.d(this.getClass().getSimpleName(), "成功");
        Toast.makeText(this, "发表成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected BaseJson onEventAsync(TaskBean argument) {
        try {
            if (imageBean != null) {
                Map<String, File> file = new HashMap<>();
                Map<String, String> map = new HashMap<>();
                file.put("", new File(imageBean.getFilePath()));
                String result = UploadFileUtils.post("image/upload", map, file);
                String path = new JSONObject(result).getString("file");
                argument.setSimpleDrawingAddress(path.substring(1));
            } else {
                argument.setSimpleDrawingAddress("");
            }
            BaseJson baseJson = taskAction.publishLongTermTask(argument);
            return baseJson;
        } catch (Exception e) {
            return null;
        }
    }

    private EditText getTaskTitle() {
        return (EditText) findViewById(R.id.task_title);
    }

    private EditText getTaskDescription() {
        return (EditText) findViewById(R.id.task_description);
    }

    private EditText getTaskAddress() {
        return (EditText) findViewById(R.id.task_address);
    }

    private EditText getPay() {
        return (EditText) findViewById(R.id.pay);
    }

    private EditText getMaxPeopleNumber() {
        return (EditText) findViewById(R.id.max_people_number);
    }

    private TextView getDealline() {
        return (TextView) findViewById(R.id.dealline_time);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.task_publish:
                TaskBean taskBean = new TaskBean(getTaskTitle().getText().toString(),
                        getTaskDescription().getText().toString(),
                        getTaskAddress().getText().toString(),
                        Double.parseDouble(getPay().getText().toString()),
                        Integer.parseInt(getMaxPeopleNumber().getText().toString()),
                        date, mTags);
                taskBean.setIsGroup(targetGroupId);
                netPost(taskBean);
                break;
            case R.id.dealline:
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                //调用show方法弹出对话框
                // 第一个参数为FragmentManager对象
                // 第二个为调用该方法的fragment的标签
                datePickerFragment.show(getSupportFragmentManager(), "date_picker");
                break;
            case R.id.upload_picture:
                getImageFromAlbum();
                break;
        }
    }

    @Override
    public void getData(Date date) {
        this.date = date;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
        getDealline().setText(formatter.format(date));
    }

    protected void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            Uri uri = data.getData();
            //to do find the path of pic by uri
            Bitmap photo = null;
            String path = BitmapUtils.getImageAbsolutePath(this, uri);
            try {
                //photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                photo = BitmapUtils.revitionImageSize(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //photo = BitmapFactory.decodeStream(resolver.openInputStream(originalUri));
            imageView.setImageBitmap(photo);
            imageBean = new ImageBean(path, photo, false);
            //gridViewAdapter.addData(new ImageBean(path, photo, false));
        } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
            Uri uri = data.getData();
            if (uri == null) {
                //use bundle to get data
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    Bitmap photo = (Bitmap) bundle.get("data"); //get bitmap
                    //spath :生成图片取个名字和路径包含类型

                } else {
                    Toast.makeText(getApplicationContext(), "err****", Toast.LENGTH_LONG).show();
                    return;
                }
            } else {
                //to do find the path of pic by uri
            }
        }
    }

    public static boolean saveImage(Bitmap photo, String spath) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(spath, false));
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
