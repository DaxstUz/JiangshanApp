package com.jiangshan.knowledge.wxapi;

import com.hjq.toast.ToastUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

/**
 * auth s_yz  2021/10/30
 */
public class WXEntryActivity extends WXCallbackActivity {

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
            ToastUtils.show("分享成功！");
        }else {
            super.onResp(resp);
        }
    }
}
