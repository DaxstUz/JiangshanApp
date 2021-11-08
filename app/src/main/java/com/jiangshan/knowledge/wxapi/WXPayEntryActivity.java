package com.jiangshan.knowledge.wxapi;

import com.jiangshan.knowledge.uitl.Utils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

/**
 * auth s_yz  2021/11/8
 */
public class WXPayEntryActivity extends WXCallbackActivity {

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Utils.payUtil.payResult(resp.errCode);
            finish();
        }
    }
}
