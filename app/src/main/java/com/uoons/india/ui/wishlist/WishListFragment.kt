package com.uoons.india.ui.wishlist

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.databinding.FragmentWishListBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.wishlist.adapter.GetWishListAdapter
import com.uoons.india.ui.wishlist.model.GetWishListDataModel
import com.uoons.india.utils.AppConstants
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class WishListFragment : BaseFragment<FragmentWishListBinding, WishListFragmentVM>(),
    WishListFragmentNavigator {

    override val bindingVariable: Int = BR.wishListFragmentVM
    override val layoutId: Int = R.layout.fragment_wish_list
    override val viewModelClass: Class<WishListFragmentVM> = WishListFragmentVM::class.java
    private var navController: NavController? = null
    lateinit var getWishListAdapter : GetWishListAdapter

    override   fun init() {
        mViewModel.navigator = this
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            viewDataBinding.shimmerWishListLayout.startShimmer()
            mViewModel.getWishListProductApiCall()
        }else{
            mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons,resources.getString(R.string.please_check_internet_connection), onClick = {})
        }


        getWishListAdapter = GetWishListAdapter(onclick = {
            mViewModel.removeWishListItemApiCall(it)
        })
        viewDataBinding.toolbar.txvTitleName.text = resources.getString(R.string.wishlist)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        init()
        viewDataBinding.toolbar.ivHeartVector.visibility = View.GONE
        viewDataBinding.toolbar.ivCartVector.visibility = View.GONE
        viewDataBinding.toolbar.ivCartVectorInvisible.visibility = View.GONE
        viewDataBinding.toolbar.crdCountMyCart.visibility = View.GONE

        viewDataBinding.toolbar.ivBackBtn.setOnClickListener(View.OnClickListener { view ->
            super.onBackClick()
        })

        getWishListAdapter.setOnItemClickListener(object :
            GetWishListAdapter.OnItemClickListener {
            override fun onItemClicked(pId: String) {
                val bundle = bundleOf("pId" to pId)
                navController?.navigate(R.id.action_wishListFragment_to_productDetailFragment,bundle)
            }
        })

    }

    override fun getWishListResponse() {
        if(view !=null){
            mViewModel.getWishListDataResponse.observe(viewLifecycleOwner, Observer<GetWishListDataModel> {
                if (it != null){
                    context?.let {
                            it2 ->setWishListAdapterData(it,it2)
                    }
                }else{
                    Toast.makeText(requireActivity(), "Error in fetching data", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    override fun getWishListFailureResponse() {
        if(view !=null){
            mViewModel.getWishListDataFailureResponse.observe(viewLifecycleOwner, Observer<GetWishListDataModel> {
                if (it != null){
                    setWishListFailureData(it)
                }else{
                    Toast.makeText(requireActivity(), "Error in fetching data", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun setWishListFailureData(getWishListDataModel: GetWishListDataModel) {
        viewDataBinding.wishListEmpty.visibility = View.VISIBLE
        viewDataBinding.rcvWishList.visibility = View.GONE
        viewDataBinding.shimmerWishListLayout.visibility = View.GONE
    }

    override fun removeWishListItemResponse() {
        mViewModel.getWishListProductApiCall()
    }

    override fun naviGateToHomeDehsBoardFragment() {
        navController?.navigate(R.id.action_wishListFragment_to_homeFragment)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setWishListAdapterData(data: GetWishListDataModel, context: Context){
        viewDataBinding.shimmerWishListLayout.stopShimmer()
        viewDataBinding.shimmerWishListLayout.visibility = View.GONE
        if (data.Data.isEmpty()){
            viewDataBinding.wishListEmpty.visibility = View.VISIBLE
            viewDataBinding.rcvWishList.visibility = View.GONE
            viewDataBinding.shimmerWishListLayout.visibility = View.GONE
        }else{
            viewDataBinding.wishListEmpty.visibility = View.GONE
            viewDataBinding.shimmerWishListLayout.visibility = View.GONE
            getWishListAdapter.setWishListItems(data,context)
            viewDataBinding.rcvWishList.adapter = getWishListAdapter
            getWishListAdapter.notifyDataSetChanged()
        }
    }

}