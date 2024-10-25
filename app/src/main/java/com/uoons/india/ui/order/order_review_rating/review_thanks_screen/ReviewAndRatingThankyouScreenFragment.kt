package com.uoons.india.ui.order.order_review_rating.review_thanks_screen

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.databinding.FragmentReviewAndRatingThankyouScreenBinding
import com.uoons.india.ui.base.BaseFragment
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class ReviewAndRatingThankyouScreenFragment : BaseFragment<FragmentReviewAndRatingThankyouScreenBinding, ReviewAndRatingThankyouScreenFragmentVM>(),
    ReviewAndRatingThankyouScreenFragmentNavigator {

    override val bindingVariable: Int = BR.reviewAndRatingThankyouScreenFragmentVM
    override val layoutId: Int = R.layout.fragment_review_and_rating_thankyou_screen
    override val viewModelClass: Class<ReviewAndRatingThankyouScreenFragmentVM> = ReviewAndRatingThankyouScreenFragmentVM::class.java
    private var navController: NavController? = null

    override   fun init() {
        mViewModel.navigator = this
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Log.e("ReviewAndRatingThankyouScreenFragment","")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        init()
    }

    override fun naviGateToHomeFragment() {
        navController?.navigate(R.id.action_reviewAndRatingThankyouScreenFragment_to_homeFragment)
    }

}