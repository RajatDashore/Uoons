package com.uoons.india.ui.help.view_pager_fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.databinding.FragmentContactUsBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.help.view_pager_fragment.model.ContactUsModel
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class ContactUsFragment : BaseFragment<FragmentContactUsBinding, ContactUsFragmentVM>(),
    ContactUsFragmentNavigator {

    override val bindingVariable: Int = BR.contactUsFragmentVM
    override val layoutId: Int = R.layout.fragment_contact_us
    override val viewModelClass: Class<ContactUsFragmentVM> = ContactUsFragmentVM::class.java
    private var navController: NavController? = null
    private val lstHelpTopics = arrayOf(
        "Choose Topics",
        "Order",
        "Mobile Repair",
        "Laptop / Desktop Repair",
        "Seller",
        "Payment",
        "Cancellation & Returns",
        "Other"
    )

    override  fun init() {
        mViewModel.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        // Sppiner For Gender
        val arrayGenderAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_dropdown_item_1line, lstHelpTopics)
        viewDataBinding.sppinerHelpTopics.adapter = arrayGenderAdapter
        arrayGenderAdapter.notifyDataSetChanged()
    }

    override fun submitHelpTopic() {
        if (mViewModel.isValidField(strName = viewDataBinding.edtName.text.toString(), strEmail = viewDataBinding.edtEmail.text.toString(),
                strHelpTopics = viewDataBinding.sppinerHelpTopics.selectedItem.toString(),
                strMessage = viewDataBinding.edtMessage.text.toString()))
        {
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                mViewModel.submitHelpTopicApiCall(
                    viewDataBinding.edtName.text.toString(),
                    viewDataBinding.edtEmail.text.toString(),
                    viewDataBinding.sppinerHelpTopics.selectedItem.toString(),
                    viewDataBinding.edtMessage.text.toString()
                )
            } else {
                mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons, resources.getString(R.string.please_check_internet_connection), onClick = {
                    onInternet()
                })
            }
        }
    }

    private fun onInternet() {
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            mViewModel.submitHelpTopicApiCall(
                viewDataBinding.edtName.text.toString(),
                viewDataBinding.edtEmail.text.toString(),
                viewDataBinding.sppinerHelpTopics.selectedItem.toString(),
                viewDataBinding.edtMessage.text.toString()
            )
        } else {
            mViewModel.navigator!!.showAlertDialog1Button(
                AppConstants.Uoons,
                resources.getString(R.string.please_check_internet_connection),
                onClick = {
                    onInternet()
                })
        }
    }

    override fun saveContactHelpTopicResponse() {
        if (view != null) {
            mViewModel.contactHelpTopicResponse.observe(
                viewLifecycleOwner,
                Observer<ContactUsModel> {
                    if (it != null) {
                        contactUsResponse(it)
                    }
                })
        }

    }

    private fun contactUsResponse(contactUsModel: ContactUsModel) {
        context.let { CommonUtils.showToastMessage(requireContext(), contactUsModel.message) }
    }

}