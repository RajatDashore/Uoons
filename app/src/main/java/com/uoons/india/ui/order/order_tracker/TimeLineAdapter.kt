package com.uoons.india.ui.order.order_tracker

import android.os.Build
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView

import com.uoons.india.ui.order.order_tracker.model.TimeLineModel
import com.github.vipulasri.timelineview.TimelineView
import com.uoons.india.ui.order.order_tracker.extantion.formatDateTime
import com.uoons.india.ui.order.order_tracker.extantion.setGone
import com.uoons.india.ui.order.order_tracker.extantion.setVisible
import com.uoons.india.ui.order.order_tracker.model.TimelineAttributes
import com.uoons.india.R
import com.uoons.india.ui.order.order_tracker.model.OrderStatus
import com.uoons.india.ui.order.order_tracker.utils.VectorDrawableUtils


class TimeLineAdapter(private val mFeedList: List<TimeLineModel>, private var mAttributes: TimelineAttributes) : RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder>() {

    private lateinit var mLayoutInflater: LayoutInflater

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {

        if(!::mLayoutInflater.isInitialized) {
            mLayoutInflater = LayoutInflater.from(parent.context)
        }

        val view =   mLayoutInflater.inflate(R.layout.item_timeline, parent, false)
        return TimeLineViewHolder(view, viewType)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {

        val timeLineModel = mFeedList[position]

        when (timeLineModel.status) {
            OrderStatus.INACTIVE -> {
                holder.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, R.drawable.ic_marker_inactive, mAttributes.markerColor)
            }
            OrderStatus.ACTIVE -> {
                holder.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, R.drawable.ic_marker_active,  mAttributes.markerColor)
            }
            else -> {
                holder.timeline.setMarker(ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_marker), mAttributes.markerColor)
            }
        }

        if (timeLineModel.date.isNotEmpty()) {
            holder.date.setVisible()
            holder.date.text = timeLineModel.date.formatDateTime("yyyy-MM-dd HH:mm", "hh:mm a, dd-MMM-yyyy")
        } else
            holder.date.setGone()

        holder.message.text = timeLineModel.message
    }

    override fun getItemCount() = mFeedList.size

    inner class TimeLineViewHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {

        val date: AppCompatTextView = itemView.findViewById(R.id.textTimeLineDate)
        val message: AppCompatTextView = itemView.findViewById(R.id.textTimeLineTitle)
        val timeline: TimelineView = itemView.findViewById(R.id.timeLine)

        init {
            timeline.initLine(viewType)
            timeline.markerSize = mAttributes.markerSize
            timeline.setMarkerColor(mAttributes.markerColor)
            timeline.isMarkerInCenter = mAttributes.markerInCenter
            timeline.markerPaddingLeft = mAttributes.markerLeftPadding
            timeline.markerPaddingTop = mAttributes.markerTopPadding
            timeline.markerPaddingRight = mAttributes.markerRightPadding
            timeline.markerPaddingBottom = mAttributes.markerBottomPadding
            timeline.linePadding = mAttributes.linePadding

            timeline.lineWidth = mAttributes.lineWidth
            timeline.setStartLineColor(mAttributes.startLineColor, viewType)
            timeline.setEndLineColor(mAttributes.endLineColor, viewType)
            timeline.lineStyle = mAttributes.lineStyle
            timeline.lineStyleDashLength = mAttributes.lineDashWidth
            timeline.lineStyleDashGap = mAttributes.lineDashGap
        }
    }

}
