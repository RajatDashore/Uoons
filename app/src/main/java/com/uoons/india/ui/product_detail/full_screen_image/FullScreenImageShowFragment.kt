package com.uoons.india.ui.product_detail.full_screen_image

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.databinding.FragmentFullScreenImageShowBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.product_detail.adapter.MyImagesViewPagerAdapter
import com.uoons.india.ui.product_detail.model.ImagesArrayListModel
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class FullScreenImageShowFragment : BaseFragment<FragmentFullScreenImageShowBinding, FullScreenImageShowFragmentVM>(),
    FullScreenImageShowFragmentNavigator {

    override val bindingVariable: Int = BR.fullScreenImageShowFragmentVM
    override val layoutId: Int = R.layout.fragment_full_screen_image_show
    override val viewModelClass: Class<FullScreenImageShowFragmentVM> = FullScreenImageShowFragmentVM::class.java
    private var navController: NavController? = null
    private lateinit var myImagesViewPagerAdapter : MyImagesViewPagerAdapter
    private var productMultipleImagesList: ArrayList<ImagesArrayListModel> = ArrayList<ImagesArrayListModel>()

    override   fun init(){
        mViewModel.navigator = this
        if (arguments != null) {
            productMultipleImagesList =
                (requireArguments().getSerializable("imagesList") as ArrayList<ImagesArrayListModel>?)!!
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("","")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        myImagesViewPagerAdapter = MyImagesViewPagerAdapter(productMultipleImagesList,requireContext(), onClick = {})
        viewDataBinding.productImageSlider.adapter = myImagesViewPagerAdapter
        TabLayoutMediator(viewDataBinding.dotsLayout,viewDataBinding.productImageSlider, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
        }).attach()
        viewDataBinding.productImageSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })
    }

}