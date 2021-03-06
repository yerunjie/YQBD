package com.yqbd.yqbd.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.yqbd.yqbd.BaseFragment;
import com.yqbd.yqbd.R;
import com.yqbd.yqbd.actions.UserActionImpl;
import com.yqbd.yqbd.activities.initial.InitialActivity;
import com.yqbd.yqbd.activities.task.MyTaskActivity;
import com.yqbd.yqbd.beans.UserInfoBean;
import com.yqbd.yqbd.utils.BaseJson;
import com.yqbd.yqbd.utils.EventBusUtils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalFragment extends BaseFragment<Integer> implements View.OnClickListener {

    private static final String ARG_PARAM1 = "userID";


    private Integer userID;
    private CircleImageView headPortrait;
    private TextView nickName;
    private TextView sex;
    private TextView idNumber;
    private Button footMark;
    private Button logout;
    private Button published;
    private Button taken;
    private TextView professionalLevel;
    private TextView creditLevel;
    private UserActionImpl userAction;
    //private UserInfoBean userInfoBean;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_personal, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userAction = new UserActionImpl(getActivity());
        headPortrait = (CircleImageView) view.findViewById(R.id.head_portrait);
        nickName = (TextView) view.findViewById(R.id.nick_name);
        sex = (TextView) view.findViewById(R.id.sex);
        idNumber = (TextView) view.findViewById(R.id.id_number);
        footMark = (Button) view.findViewById(R.id.foot_mark);
        professionalLevel = (TextView) view.findViewById(R.id.professional_level);
        creditLevel = (TextView) view.findViewById(R.id.credit_level);
        logout = (Button) view.findViewById(R.id.logout);
        logout.setOnClickListener(this);
        published=(Button)view.findViewById(R.id.my_published);
        published.setOnClickListener(this);
        taken=(Button) view.findViewById(R.id.my_taken);
        taken.setOnClickListener(this);
        netPost(userID);
        EventBusUtils.register();
        //new Thread(new PersonalThread()).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE).edit();
                editor.clear().commit();
                editor = getActivity().getSharedPreferences("companyInfo", Context.MODE_PRIVATE).edit();
                editor.clear().commit();
                Intent intent = new Intent();
                intent.setClass(getContext(), InitialActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.my_published:
                Intent intent2=new Intent(getActivity(), MyTaskActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("tasksType",0);
                intent2.putExtras(bundle);
                intent2.putExtra("title","我发布的任务");
                startActivity(intent2);
                break;
            case R.id.my_taken:
                Intent intent3=new Intent(getActivity(),MyTaskActivity.class);
                Bundle bundle2=new Bundle();
                bundle2.putInt("tasksType",1);
                intent3.putExtras(bundle2);
                intent3.putExtra("title","我接受的任务");
                startActivity(intent3);
        }
    }

    @Override
    protected void onEventMainThread(BaseJson baseJson) {
        Log.d("PersonalFragment", "onEventMainThread收到了消息：");
        try {
            UserInfoBean userInfoBean = UserInfoBean.newInstance(baseJson.getJSONObject());
            nickName.setText(userInfoBean.getNickName().toString());
            sex.setText(userInfoBean.getSex().toString());
            idNumber.setText(userInfoBean.getAccountNumber().toString());
            professionalLevel.setText(userInfoBean.getProfessionalLevel().toString());
            creditLevel.setText(userInfoBean.getCreditLevel().toString());
        } catch (Exception e) {

        }
    }

    @Override
    protected BaseJson onEventAsync(Integer argument) {
        try {
            BaseJson baseJson = userAction.getUserInfoByUserID(userID);
            return baseJson;
            //userInfoBean = UserInfoBean.newInstance(baseJson.getJSONObject());
        } catch (Exception e) {
            return null;
        }
    }

    public PersonalFragment() {
        // Required empty public constructor
    }


    public static PersonalFragment newInstance(int userID) {
        PersonalFragment fragment = new PersonalFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, userID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userID = getArguments().getInt(ARG_PARAM1);
        }
    }
}
