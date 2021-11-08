package com.jiangshan.knowledge.wxapi;

import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.jiangshan.knowledge.uitl.Utils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

/**
 * auth s_yz  2021/10/30
 */
public class WXEntryActivity extends WXCallbackActivity {

    @Override
    public void onResp(BaseResp resp) {
        System.out.println("WXEntryActivity:==>"+new Gson().toJson(resp));
        super.onResp(resp);
        if(resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX){
            Utils.payUtil.payResult(resp.getType());
            ToastUtils.show(""+resp.errStr);
        }
    }

}
