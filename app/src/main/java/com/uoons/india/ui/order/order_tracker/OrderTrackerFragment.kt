package com.uoons.india.ui.order.order_tracker

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.vipulasri.timelineview.TimelineView
import com.uoons.india.ui.order.order_tracker.extantion.dpToPx
import com.uoons.india.ui.order.order_tracker.extantion.getColorCompat
import com.uoons.india.ui.order.order_tracker.extantion.setGone
import com.uoons.india.ui.order.order_tracker.extantion.setVisible
import com.uoons.india.ui.order.order_tracker.model.TimeLineModel
import com.uoons.india.ui.order.order_tracker.model.TimelineAttributes
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.databinding.FragmentOrderTrackerBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.order.order_tracker.model.OrderStatus
import com.uoons.india.utils.AppConstants
import java.util.ArrayList

class OrderTrackerFragment : BaseFragment<FragmentOrderTrackerBinding, OrderTrackerFragmentVM>(),
    OrderTrackerFragmentNavigator {
    override val bindingVariable: Int = BR.orderTrackerFragmentVM
    override val layoutId: Int = R.layout.fragment_order_tracker
    override val viewModelClass: Class<OrderTrackerFragmentVM> = OrderTrackerFragmentVM::class.java
    private var trackerId: String = ""
    private val mDataList = ArrayList<TimeLineModel>()
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mAttributes: TimelineAttributes

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.toolbar.txvTitleName.text = "Order Tracker"
        viewDataBinding.toolbar.ivBackBtn.setOnClickListener {
            super.onBackClick()
        }

        com.bumptech.glide.Glide.with(requireContext()).load(R.mipmap.successful_food_delivery).into(viewDataBinding.ivLoading)

        viewDataBinding.toolbar.ivHeartVector.visibility = View.GONE
        viewDataBinding.toolbar.ivCartVector.visibility = View.GONE
        viewDataBinding.toolbar.ivCartVectorInvisible.visibility = View.GONE
        viewDataBinding.toolbar.crdCountMyCart.visibility = View.GONE

        //default values
        mAttributes = dpToPx(20f)?.let {
            getColorCompat( R.color.material_grey_500)?.let { it1 ->
                dpToPx(0f)?.let { it2 ->
                    dpToPx(0f)?.let { it3 ->
                        dpToPx(0f)?.let { it4 ->
                            dpToPx(0f)?.let { it5 ->
                                dpToPx(2f)?.let { it6 ->
                                    getColorCompat(com.facebook.R.color.com_facebook_blue)?.let { it7 ->
                                        getColorCompat(com.facebook.R.color.com_facebook_blue)?.let { it8 ->
                                            dpToPx(2f)?.let { it9 ->
                                                dpToPx(4f)?.let { it10 ->
                                                    dpToPx(2f)?.let { it11 ->
                                                        TimelineAttributes(
                                                            markerSize = it,
                                                            markerColor = it1,
                                                            markerInCenter = true,
                                                            markerLeftPadding = it2,
                                                            markerTopPadding = it3,
                                                            markerRightPadding = it4,
                                                            markerBottomPadding = it5,
                                                            linePadding = it6,
                                                            startLineColor = it7,
                                                            endLineColor = it8,
                                                            lineStyle = TimelineView.LineStyle.NORMAL,
                                                            lineWidth = it9,
                                                            lineDashWidth = it10,
                                                            lineDashGap = it11
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }!!

        setDataListItems()
        initRecyclerView()
    }

    override fun init() {
        mViewModel.navigator = this
        trackerId = arguments?.getString(AppConstants.trackerId).toString()
    }

    private fun setDataListItems() {
        mDataList.add(TimeLineModel("Order placed successfully", "2017-02-10 14:00", OrderStatus.COMPLETED))
        mDataList.add(TimeLineModel("Order confirmed by seller", "2017-02-10 14:30", OrderStatus.COMPLETED))
        mDataList.add(TimeLineModel("Order processing initiated", "2017-02-10 15:00", OrderStatus.COMPLETED))
        mDataList.add(TimeLineModel("Order is being readied for dispatch", "2017-02-11 08:00", OrderStatus.COMPLETED))
        mDataList.add(TimeLineModel("Item is packed and will dispatch soon", "2017-02-11 09:30", OrderStatus.COMPLETED))
        mDataList.add(TimeLineModel("Item has been given to the courier", "2017-02-11 18:00", OrderStatus.COMPLETED))
        mDataList.add(TimeLineModel("Item has reached courier facility at New Delhi", "2017-02-11 21:00", OrderStatus.COMPLETED))
        mDataList.add(TimeLineModel("Courier is out to delivery your order", "2017-02-12 08:00", OrderStatus.ACTIVE))
        mDataList.add(TimeLineModel("Item successfully delivered", "", OrderStatus.INACTIVE))
    }

    private fun initRecyclerView() {
        initAdapter()
        viewDataBinding.recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            @SuppressLint("LongLogTag")
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(recyclerView.getChildAt(0).top < 0) viewDataBinding.dropshadow.setVisible() else viewDataBinding.dropshadow.setGone()
            }
        })
    }

    private fun initAdapter() {
        mLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        viewDataBinding.recyclerView.apply {
            layoutManager = mLayoutManager
            adapter =  TimeLineAdapter(mDataList, mAttributes)
        }
    }

}