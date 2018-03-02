package com.jointem.fire.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.view.View
import com.jointem.base.BaseActivity
import com.jointem.fire.R
import com.jointem.fire.utils.showDialog
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 * @Description:
 * @Author:  joketng
 * @Time:  2018/3/2
 */
class SplashActivity : BaseActivity() {

    lateinit var rxPermissions: RxPermissions

    override fun setRootView() {
        super.setRootView()
        setContentView(R.layout.aty_splash)
    }

    override fun initData() {
        super.initData()
    }

    override fun initWidget() {
        super.initWidget()
        rxPermissions = RxPermissions(this)

        rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe { accept ->
                    if (accept) {
                        sendRequestAndLocation()
                    } else {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                                != PackageManager.PERMISSION_GRANTED) {
                            showDialog("需要以下权限才能正常运行:手机信息(IMEI)，定位", View.OnClickListener {

                            }, View.OnClickListener {
                                WebViewActivity.startWebViewActivity(context, "http://www.baidu.com", "sss")
                            })
                        } else {
                            sendRequestAndLocation()
                        }
                    }
                }
    }

    private fun sendRequestAndLocation() {
        startActivity(Intent(context, MainActivity::class.java))
    }


}
