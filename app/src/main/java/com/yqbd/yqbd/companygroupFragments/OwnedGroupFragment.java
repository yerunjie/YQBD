package com.yqbd.yqbd.companygroupFragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;
import com.yqbd.yqbd.R;
import com.yqbd.yqbd.activities.group.DeleteGroupActivity;
import com.yqbd.yqbd.beans.GroupInfoBean;
import com.yqbd.yqbd.usergroupFragments.adapters.GroupListAdapter;

import java.util.ArrayList;
import java.util.List;

public class OwnedGroupFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private ArrayList<GroupInfoBean> groupsOwned = new ArrayList<>();

    private ListView groupOwnedListView;

    private GroupListAdapter listAdapter;

    public OwnedGroupFragment() {
        // Required empty public constructor
    }


    public void setGroupsOwned(List<GroupInfoBean> groupsOwned) {
        this.groupsOwned = new ArrayList<>();
        this.groupsOwned.addAll(groupsOwned);
    }

    public static OwnedGroupFragment newInstance(List<GroupInfoBean> groupsOwned) {
        OwnedGroupFragment fragment = new OwnedGroupFragment();
        Bundle args = new Bundle();
        ArrayList<GroupInfoBean> list = new ArrayList<>();
        list.addAll(groupsOwned);
        args.putParcelableArrayList("groupsOwned",list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            groupsOwned = getArguments().getParcelableArrayList("groupsOwned");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owned_group, container, false);
        groupOwnedListView = (ListView) view.findViewById(R.id.list_owned_group);
        listAdapter = new GroupListAdapter(getActivity(), R.layout.item_group_simple, groupsOwned);
        groupOwnedListView.setAdapter(listAdapter);
        groupOwnedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击跳转到删除小组申请页面
                Intent intent = new Intent(getContext(), DeleteGroupActivity.class);
                intent.putExtra("groupId",groupsOwned.get(position).getGroupId());
                startActivity(intent);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
