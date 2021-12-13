package com.jiangshan.knowledge.wxapi;

import com.hjq.http.EasyLog;
import com.hjq.toast.ToastUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

/**
 * auth s_yz  2021/10/30
 */
public class WXEntryActivity extends WXCallbackActivity {

    @Override
    public void onResp(BaseResp resp) {
//        EasyLog.print("WXEntryActivity ");
        if (resp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {
            WXLaunchMiniProgram.Resp launchMiniProResp = (WXLaunchMiniProgram.Resp) resp;
            String extraData =launchMiniProResp.extMsg; //对应小程序组件 <button open-type="launchApp"> 中的 app-parameter 属性
            EasyLog.print("组件：===》 "+extraData);
        }else
        if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
            ToastUtils.show("分享成功！");
        }else {
            super.onResp(resp);
        }
    }
}
