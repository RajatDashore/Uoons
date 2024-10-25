package com.uoons.india.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.uoons.india.R
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class CustomProgressDialog : Dialog {
    private var mContext: Context? = null
    private var pDialog: Dialog? = null

    constructor(context: Context) : super(context) {
        this.mContext = context
    }

    constructor(context: Context, theme: Int) : super(context, theme)

    fun hideProgressDialog() {
        try {
            if (pDialog != null) {
                if (pDialog!!.isShowing)
                    pDialog!!.dismiss()
                pDialog = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showProgressDialog() {
        try {
            if (pDialog == null) {
               createProgressDialog(context)
                pDialog!!.setCanceledOnTouchOutside(false)
                pDialog!!.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createProgressDialog(context: Context): CustomProgressDialog {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.dialog_custom_progress, null)
        pDialog = CustomProgressDialog(context, R.style.CustomProgress)
        (pDialog as CustomProgressDialog).setContentView(view)
        val loadingImageView = pDialog!!.findViewById<android.widget.ImageView>(R.id.loader_healthcare)
        com.bumptech.glide.Glide.with(context).load(R.mipmap.loading_circle_scale).into(loadingImageView)
        return pDialog as CustomProgressDialog
    }

    companion object {
        private var pDialog: Dialog? = null
    }
}

