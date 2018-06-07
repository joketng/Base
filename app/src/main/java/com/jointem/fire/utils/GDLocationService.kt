package com.jointem.fire.utils

import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.jointem.fire.WYCApplication
import com.orhanobut.logger.Logger

/**
 * @Description:
 * @Author:  joketng
 * @Email:  joketng@163.com
 * @Time:  2018/6/7
 */
object GDLocationService : AMapLocationListener {
    private lateinit var mAMapLocationClient: AMapLocationClient
    private lateinit var mLocationClientOption: AMapLocationClientOption


    fun initLocationOption(isOnceLocation: Boolean){
        mAMapLocationClient = AMapLocationClient(WYCApplication.getContextFromApplication())
        mAMapLocationClient.setLocationListener(this)

        mLocationClientOption = AMapLocationClientOption()
        mLocationClientOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        mLocationClientOption.isNeedAddress = true
        mLocationClientOption.isOnceLocation = isOnceLocation
        mAMapLocationClient.setLocationOption(mLocationClientOption)
    }

    fun startLocation(){
        mAMapLocationClient.startLocation()
    }

    fun stopLocation(){
        mAMapLocationClient.stopLocation()
    }

    override fun onLocationChanged(aMapLocation: AMapLocation?) {
        aMapLocation?.let {
            Logger.d(it.address)

        }

    }


}