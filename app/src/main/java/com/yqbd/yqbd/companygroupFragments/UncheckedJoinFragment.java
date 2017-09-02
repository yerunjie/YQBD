package com.yqbd.yqbd.companygroupFragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;
import com.yqbd.yqbd.R;
import com.yqbd.yqbd.activities.group.CheckJoinActivity;
import com.yqbd.yqbd.beans.ApplicationBean;
import com.yqbd.yqbd.companygroupFragments.adapters.ApplicationListAdapter;

import java.util.ArrayList;
import java.util.List;

public class UncheckedJoinFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private ArrayList<ApplicationBean> applicationsJoin;

    private ListView uncheckedJoinListView;

    private ApplicationListAdapter listAdapter;

    public UncheckedJoinFragment() {
        // Required empty public constructor
    }

    public void setApplicationsJoin(List<ApplicationBean> applicationsJoin) {
        this.applicationsJoin = new ArrayList<>();
        this.applicationsJoin.addAll(applicationsJoin);
    }


    public static UncheckedJoinFragment newInstance(List<ApplicationBean> applicationBeanList) {
        UncheckedJoinFragment fragment = new UncheckedJoinFragment();
        Bundle args = new Bundle();
        ArrayList<ApplicationBean> list = new ArrayList<>();
        list.addAll(applicationBeanList);
        args.putParcelableArrayList("applicationsJoin",list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //接收数据
        if (getArguments() != null) {
            applicationsJoin = getArguments().getParcelableArrayList("applicationsJoin");
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unchecked_join, container, false);
        uncheckedJoinListView = (ListView) view.findViewById(R.id.list_unchecked_join);
//        Log.d("UncheckedJoinFragment","当前数据个数" + applicationsJoin.size());
        listAdapter = new ApplicationListAdapter(getActivity(), R.layout.item_application_simple, applicationsJoin);
        uncheckedJoinListView.setAdapter(listAdapter);
        uncheckedJoinListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击跳转到审核申请页面
                Intent intent = new Intent(getContext(), CheckJoinActivity.class);
                intent.putExtra("applicationBean",applicationsJoin.get(position));
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
