package com.jiangshan.knowledge.uitl;

import android.app.AlertDialog;

/**
 * 弹出框按钮监听事件
 * @author xc.li
 * @date 2015年11月14日
 */
public interface AlertButtonClick {
	/**
	 * 左边按钮点击事件
	 * @param dlg
	 */
	public void leftBtnClick(AlertDialog dlg);
	/**
	 * 右边按钮点击事件
	 * @param dlg
	 */
	public void rightBtnClick(AlertDialog dlg);
}
