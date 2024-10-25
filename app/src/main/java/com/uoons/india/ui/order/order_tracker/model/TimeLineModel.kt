package com.uoons.india.ui.order.order_tracker.model

import android.os.Parcelable
import com.uoons.india.ui.order.order_tracker.model.OrderStatus
import kotlinx.android.parcel.Parcelize


@Parcelize
class TimeLineModel(
        var message: String,
        var date: String,
        var status: OrderStatus
) : Parcelable
