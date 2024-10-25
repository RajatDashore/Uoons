package com.uoons.india.ui.notification_settings

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.databinding.FragmentNotificationsSettingsBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.filter.model.FilterItemsDataModel
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class NotificationsSettingsFragment : BaseFragment<FragmentNotificationsSettingsBinding, NotificationsSettingsFragmentVM>(),
    NotificationsSettingsFragmentNavigator {

    override val bindingVariable: Int = BR.notificationSettingsFragmentVM
    override val layoutId: Int = R.layout.fragment_notifications_settings
    override val viewModelClass: Class<NotificationsSettingsFragmentVM> = NotificationsSettingsFragmentVM::class.java
    private var navController: NavController? = null
    lateinit var thiscontext: Context

    override    fun init() {
        mViewModel.navigator = this
        mViewModel.navigator = this
        viewDataBinding.executePendingBindings()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        thiscontext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("NotificationsSettingsFragment:","onCreate:-")

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        thiscontext = view.context
        navController = Navigation.findNavController(view)
        viewDataBinding.toolbar.txvTitleName.text = getResources().getString(R.string.notification_settings)
        viewDataBinding.toolbar.ivHeartVector.visibility = View.GONE
        viewDataBinding.toolbar.ivCartVector.visibility = View.GONE
        viewDataBinding.toolbar.ivCartVectorInvisible.visibility = View.GONE

        viewDataBinding.toolbar.ivBackBtn.setOnClickListener(View.OnClickListener { view ->
            super.onBackClick()
        })

        viewDataBinding.toolbar.ivCartVector.setOnClickListener(View.OnClickListener { view ->
            naviGateToMyCartFragment()
        })
        setUpBinding()
    }

    fun naviGateToMyCartFragment(){
        navController?.navigate(R.id.action_notificationsSettingsFragment_to_myCartFragment)
    }

    private fun setUpBinding(){
        viewDataBinding.rcvAllNotification.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        }
        makeApiCall()
    }

    private fun makeApiCall(){
        // --- Notifications List
        mViewModel.getRecyclerListNotificationsDataObserver().observe(viewLifecycleOwner, Observer<ArrayList<FilterItemsDataModel>>{
            if (it != null){
                context?.let { it1 -> mViewModel.setNotificationsAdapterData(it, it1) }
            }else{
                context?.let { CommonUtils.showToastMessage(it, resources.getString(R.string.error_in_fetching_data)) }
            }
        })

        mViewModel.getNotification().observe(viewLifecycleOwner, Observer<Int> {
            if (it != -1){
                Log.e("NotificationsSettingsFragment:","null:-viewLifecycleOwner")
                viewDataBinding.txvUnselectedAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_color))
            }else{
                Log.e("NotificationsSettingsFragment:","null:-else")
                viewDataBinding.txvUnselectedAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_color_light_1))
            }
        })

        mViewModel.makeAPICall("india",requireContext())
    }

}