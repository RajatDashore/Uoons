package com.uoons.india.ui.product_list.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.ui.product_list.model.ProductListModel
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

@Obfuscate
class ProductListAdapter(updatedProductsList: ArrayList<ProductListModel>, context: Context,var onclick:(value : String)->Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var productList: ArrayList<ProductListModel>? = null
    private var salientFeatureList1: ArrayList<String> = ArrayList()
    var context: Context

    // Salient Features
    private val salientFeaturesDataRecyclerAdapter = SalientFeaturesDataRecyclerAdapter()

    init {
        this.productList = updatedProductsList
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout: View = LayoutInflater.from(context).inflate(R.layout.row_product_list,parent,false)
       return LayoutSalientFeaturesViewHolder(layout)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)  {
        (holder as LayoutSalientFeaturesViewHolder).bind(position)
    }

    override fun getItemCount(): Int {
        return productList!!.size
    }

    private inner class LayoutSalientFeaturesViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var crdProduct: CardView = itemView.findViewById(R.id.crdProduct)
        var ivProductImage: ImageView = itemView.findViewById(R.id.ivProductImage)
        var txvProductName: TextView = itemView.findViewById(R.id.txvProductName)
        var txvRatingPoint: TextView = itemView.findViewById(R.id.txvRatingPoint)
        var txvAVGRating: TextView = itemView.findViewById(R.id.txvAVGRating)
        var txvMRPPrice: TextView = itemView.findViewById(R.id.txvMRPPrice)
        var txvSellingPrice: TextView = itemView.findViewById(R.id.txvSellingPrice)
        var txvDisountOff: TextView = itemView.findViewById(R.id.txvDisountOff)
        var txvFreeDelivery: TextView = itemView.findViewById(R.id.txvFreeDelivery)
        var txvNoCostEMI: TextView = itemView.findViewById(R.id.txvNoCostEMI)
        var llOutOfStock: LinearLayout = itemView.findViewById(R.id.llOutOfStock)

        var rcvSalientFeatures: RecyclerView = itemView.findViewById(R.id.rcvSalientFeatures)
        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {

            salientFeatureList1.clear()
            salientFeatureList1.addAll(productList!![position].salientFeatures)
            salientFeatureList1.removeAll(listOf(null,"","[]"))

            if (salientFeatureList1.isEmpty()){
                Log.d("TAG", "bind:IF::salientFeatures_isEmpty:: ")
            } else {
                Log.d("TAG", "bind:ELSE::salientFeatures_isEmpty:: ")
                setSalientFeaturesItemRecycler(rcvSalientFeatures, salientFeatureList1)
            }

            if (productList!![position].productStock?.toInt() == AppConstants.zero.toInt()){
                llOutOfStock.visibility = View.VISIBLE
            }else{
                llOutOfStock.visibility = View.GONE
            }

            if (productList!![position].productImages.isNullOrEmpty()){
                CommonUtils.loadImage(ivProductImage, "", ivProductImage.id)
            }else{
                CommonUtils.loadImage(ivProductImage, productList!![position].productImages, ivProductImage.id)
            }

            txvProductName.text = productList!![position].productName


            txvSellingPrice.text = context.getString(R.string.offer_price)+" "+ context.getString(R.string.rupees)+NumberFormat.getNumberInstance(Locale.getDefault()).format(productList!![position].productSalePrice?.toInt())
            txvMRPPrice.text = context.getString(R.string.m_r_p)+" "+ context.getString(R.string.rupees)+NumberFormat.getNumberInstance(Locale.getDefault()).format(productList!![position].productPrice?.toInt())
            txvRatingPoint.text = productList!![position].rating?.rating.toString()
            txvAVGRating.text = "( "+productList!![position].rating?.total+" )"
            txvFreeDelivery.text = productList!![position].delivery
            txvNoCostEMI.text = productList!![position].noCostEmi

            val productPrice = Integer.parseInt(productList!![position].productPrice.toString())
            val productSellPrice = Integer.parseInt(productList!![position].productSalePrice.toString())
            val youSavePrice = productPrice - productSellPrice

            val percentage = calculatePercentageChange(productPrice.toDouble(), productSellPrice.toDouble())

            val str: String = percentage.toString()
            val float1: Float = str.toFloat()
            val discount = DecimalFormat(AppConstants.hash).format(float1)

            if (youSavePrice.toString() == AppConstants.zero || youSavePrice.toString() == AppConstants.EMPTY) {
                txvDisountOff.visibility = View.GONE

            } else {
                txvDisountOff.text = "(" + discount + AppConstants.PERCENTAGE + ")" + " OFF"
                /*txvDisountOff.text = context.getString(R.string.rupees) + NumberFormat.getNumberInstance(Locale.getDefault())
                        .format(youSavePrice).toString() + " (" + discount + AppConstants.PERCENTAGE + ")" + " OFF"*/
            }

            crdProduct.setOnClickListener{
                onclick.invoke(productList!![position].pid.toString())
            }
        }
    }

    private fun setSalientFeaturesItemRecycler(recyclerView: RecyclerView, salientFeaturesList: ArrayList<String>) {
        salientFeaturesDataRecyclerAdapter.setSalientFeaturesData(salientFeaturesList, context)
        recyclerView.adapter = salientFeaturesDataRecyclerAdapter
    }

    private fun calculatePercentageChange(oldValue: Double, newValue: Double): Double {
        val percentageChange = ((newValue - oldValue) / oldValue) * 100
        return Math.abs(percentageChange)
    }
}