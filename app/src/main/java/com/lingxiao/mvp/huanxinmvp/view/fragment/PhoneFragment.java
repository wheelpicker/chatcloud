package com.lingxiao.mvp.huanxinmvp.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.lingxiao.mvp.huanxinmvp.MainActivity;
import com.lingxiao.mvp.huanxinmvp.R;
import com.lingxiao.mvp.huanxinmvp.adapter.PhoneRecycleAdapter;
import com.lingxiao.mvp.huanxinmvp.presenter.Impl.PhonePresenterImpl;
import com.lingxiao.mvp.huanxinmvp.presenter.PhonePresenter;
import com.lingxiao.mvp.huanxinmvp.utils.ToastUtils;
import com.lingxiao.mvp.huanxinmvp.view.AddFriendActivity;
import com.lingxiao.mvp.huanxinmvp.view.BaseActivity;
import com.lingxiao.mvp.huanxinmvp.view.ChatActivity;
import com.lingxiao.mvp.huanxinmvp.view.PhoneView;
import com.lingxiao.mvp.huanxinmvp.widget.PhoneLayout;

import java.util.ArrayList;

/**
 * 通讯录详情页
 */

public class PhoneFragment extends BaseFragment implements PhoneView{
    private ArrayList<String> contacts = new ArrayList<>();
    private View view;
    private PhoneLayout phoneLayout;
    private PhonePresenter phonePresenter;
    private PhoneRecycleAdapter adapter;

    @Override
    public void initData() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        phoneLayout = (PhoneLayout) view.findViewById(R.id.phonelayout);
        phonePresenter = new PhonePresenterImpl(this);
        phonePresenter.initContact();
    }

    @Override
    public View initView() {
        view = View.inflate(getContext(), R.layout.fragment_phone,null);
        return view;
    }

    @Override
    public void onInitContact(ArrayList<String> contactList) {
        //获取到数据后，设置adapter
        adapter = new PhoneRecycleAdapter(contactList);
        phoneLayout.setAdapter(adapter);

        phoneLayout.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                phonePresenter.updateContact();
            }
        });

        adapter.setOnItemClickListener(new PhoneRecycleAdapter.onItemClickListener() {
            @Override
            public void onClick(View v, String username) {
                //单击跳转到聊天界面
                Intent intent = new Intent(getContext(),ChatActivity.class);
                intent.putExtra("name",username);
                startActivity(intent);
            }

            @Override
            public boolean onLongClick(View v, final String username) {
                //长按弹出对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("提示");
                builder.setMessage("确定要删除该好友吗?");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        phonePresenter.deleteContact(username);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                return true;
            }
        });
        phoneLayout.setFbButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity baseActivity = (BaseActivity) getActivity();
                baseActivity.StartActivity(AddFriendActivity.class,false);
            }
        });
    }

    @Override
    public void onUpdateContact(ArrayList<String> contactList, boolean isUpadteSuccess, String errorMsg) {
        phoneLayout.setRefreshing(false);
        if(isUpadteSuccess){
            adapter.setContacts(contactList);
            adapter.notifyDataSetChanged();
        }else{
            ToastUtils.showToast("更新联系人失败!");
        }
    }

    @Override
    public void onDeleteContact(boolean isDeleteSuccess, String errorMsg) {
        if (isDeleteSuccess){
            Snackbar.make(view,"删除成功!",Snackbar.LENGTH_LONG);
        }else {
            Snackbar.make(view,"删除失败!"+errorMsg,Snackbar.LENGTH_LONG);
        }
    }
}
