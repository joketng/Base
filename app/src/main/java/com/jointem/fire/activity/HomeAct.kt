package com.jointem.fire.activity

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bigkoo.convenientbanner.ConvenientBanner
import com.handmark.pulltorefresh.library.PullToRefreshBase
import com.handmark.pulltorefresh.library.rv.OnRvRefreshListener
import com.jointem.base.BaseActivity
import com.jointem.fire.R
import com.jointem.fire.adapter.HomeAdapter
import com.jointem.fire.utils.getScreenW
import com.jointem.fire.utils.showToast
import com.jointem.fire.view.ImageHolderView
import kotlinx.android.synthetic.main.aty_home.*


class HomeAct : BaseActivity() {
    lateinit var adapter: HomeAdapter
    lateinit var header1: ConvenientBanner<String>
    lateinit var header2: View

    override fun setRootView() {
        super.setRootView()
        setContentView(R.layout.aty_home)

    }

    override fun initData() {
        super.initData()
        var itemList: List<String> = emptyList()
        for (x in 1..50) {
            itemList += "我是项目$x"
        }
        adapter = HomeAdapter(context, itemList)
    }

    override fun initWidget() {
        super.initWidget()
        ptr_recycler.mode = PullToRefreshBase.Mode.BOTH
        ptr_recycler.adapter = adapter
        header1 = layoutInflater.inflate(R.layout.item_home_header1, findViewById(android.R.id.content) as ViewGroup, false) as ConvenientBanner<String>
        var params = header1.layoutParams
        params.height = (context.getScreenW() * 5f / 12).toInt()
        header1.layoutParams = params

        var testUrl: List<String> = emptyList()
        testUrl += "https://p1.ssl.qhmsg.com/dr/270_500_/t012f5d453c56ca2754.png"
        testUrl += "https://p1.ssl.qhmsg.com/dr/270_500_/t01777164d084b139c4.jpg"
        testUrl += "https://p1.ssl.qhmsg.com/t017a2b424c49000655.jpg"
        header1.setPages({ ImageHolderView() }, testUrl)
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .setOnItemClickListener {
                    context.showToast("dfsfsa")
                }
        header1.setPageIndicator(intArrayOf(R.mipmap.home_icon_normal, R.mipmap.home_icon_focus))

        header2 = layoutInflater.inflate(R.layout.item_home_header2, null, false)
        ptr_recycler.addHeaderView(header1)
        ptr_recycler.addHeaderView(header2)

        ptr_recycler.setOnRvRefreshListener(object : OnRvRefreshListener() {
            override fun onPullDownToRefresh(refreshView: RecyclerView?, curPageNo: Int) {
                context.showToast("dfsafas")
            }
        })
    }
}
