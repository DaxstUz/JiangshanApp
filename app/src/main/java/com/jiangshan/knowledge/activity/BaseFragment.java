package com.jiangshan.knowledge.activity;

import androidx.fragment.app.Fragment;

import com.hjq.http.listener.OnHttpListener;

/**
 * auth s_yz  2021/10/10
 */
public class BaseFragment extends Fragment implements OnHttpListener<Object> {
    @Override
    public void onSucceed(Object result) {
    }

    @Override
    public void onFail(Exception e) {
    }
}
