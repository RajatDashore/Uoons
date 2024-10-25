package com.uoons.india.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.bumptech.glide.Glide
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.databinding.FragmentProfileBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.profile.model.UserDetailsModel
import com.uoons.india.utils.AppConstants
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileFragmentVM>(),
    ProfileFrgamentNavigator {
    private var LOG_TAG = ProfileFragment::class.java.name
    override val bindingVariable: Int = BR.profileDetails
    override val layoutId: Int = R.layout.fragment_profile
    override val viewModelClass: Class<ProfileFragmentVM> = ProfileFragmentVM::class.java
    private var navController: NavController? = null

    override  fun init() {
        mViewModel.navigator = this
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            mViewModel.getUserDetailsApiCall()
            // Start shimmer animation
            viewDataBinding.shimmerProfileLayout.startShimmer()
        } else {
            mViewModel.navigator!!.showAlertDialog1Button(
                AppConstants.Uoons,
                resources.getString(R.string.please_check_internet_connection),
                onClick = {})
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewDataBinding.shimmerProfileLayout.stopShimmer()
        viewDataBinding.shimmerProfileLayout.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewDataBinding.toolbar.txvTitleName.text = resources.getString(R.string.profile)
        viewDataBinding.txvMobileNumber.text = AppPreference.getValue(PreferenceKeys.MOBILE_NO)
        viewDataBinding.toolbar.ivHeartVector.visibility = View.GONE
        viewDataBinding.toolbar.ivCartVector.visibility = View.GONE
        viewDataBinding.toolbar.ivCartVectorInvisible.visibility = View.GONE
        viewDataBinding.toolbar.crdCountMyCart.visibility = View.GONE

        viewDataBinding.toolbar.ivBackBtn.setOnClickListener(View.OnClickListener {
            super.onBackClick()
        })

        viewDataBinding.toolbar.ivCartVector.setOnClickListener(View.OnClickListener {
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                naviGateToMyCartFragment()
            } else if (mViewModel.navigator!!.isConnectedToInternet()) {
                return@OnClickListener
            }
        })
    }

    override fun naviGateToEditProfileFragment() {
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            /*val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
            CustomIntent.customType(requireContext(), AppConstants.LEFT_TO_RIGHT)*/
            navController?.navigate(R.id.action_profileFragment_to_editProfileFragment)
        } else {
            mViewModel.navigator!!.showAlertDialog1Button(
                AppConstants.Uoons,
                resources.getString(R.string.please_check_internet_connection),
                onClick = {})
        }
    }

    override fun getUserDetailsData() {
        if (view != null) {
            mViewModel.getUserDetailsResponse.observe(
                viewLifecycleOwner,
                Observer<UserDetailsModel> {
                    if (it != null) {
                        setUserDeatils(it)
                    } else {

                        Toast.makeText(
                            requireActivity(),
                            AppConstants.ErrorFetching,
                            Toast.LENGTH_LONG
                        ).show()

                    }
                })
        }
    }

    private fun setUserDeatils(userDetails: UserDetailsModel) {
        if (userDetails.Data[0].name.toString().isNotEmpty()) {
            if (userDetails.Data[0].profile.equals(AppConstants.assets_user, ignoreCase = true)) {
                viewDataBinding.crdProfileIcon.setImageResource(R.drawable.ic_profile)
            } else {
                Glide.with(this).load(userDetails.Data[0].profile)
                    .into(viewDataBinding.crdProfileIcon)
            }
            AppPreference.addValue(PreferenceKeys.USER_NAME, userDetails.Data[0].name.toString())

            viewDataBinding.txvUserName.text = userDetails.Data[0].name
            viewDataBinding.txvMobileNumber.text = userDetails.Data[0].mobileNo
            viewDataBinding.txvEmail.text = userDetails.Data[0].email
            viewDataBinding.txvAddress.text = userDetails.Data[0].address

        } else {
            viewDataBinding.crdProfileIcon.setImageResource(R.drawable.ic_profile)
            viewDataBinding.txvUserName.setHint(R.string.full_name)
            viewDataBinding.txvEmail.setHint(R.string.email)
            viewDataBinding.txvAddress.setHint(R.string.address)
        }

        viewDataBinding.cstLayoutProfile.visibility = View.VISIBLE
        viewDataBinding.shimmerProfileLayout.visibility = View.GONE

    }

    fun naviGateToMyCartFragment() {
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            navController?.navigate(R.id.action_profileFragment_to_myCartFragment)
        } else {
            mViewModel.navigator!!.showAlertDialog1Button(
                AppConstants.Uoons,
                resources.getString(R.string.please_check_internet_connection),
                onClick = {})
        }
    }

/*
    fun getEncryptedSharedprefs(context: Context): SharedPreferences {

        val masterkeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            "secured_prefs", masterkeyAlias, context ,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    }
*/
/*
    fun savename(context: Context,pid: String) {
        getEncryptedSharedprefs(context).edit()
            .putString("USER_NAME",pid)
            .apply()

    }
*/

}