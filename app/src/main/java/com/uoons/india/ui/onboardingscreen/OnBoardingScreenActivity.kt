package com.uoons.india.ui.onboardingscreen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.databinding.ActivityOnBoardingScreenBinding
import com.uoons.india.ui.base.BaseActivity
import com.uoons.india.ui.home.HomeActivity
import com.uoons.india.utils.ActivityNavigator
import com.uoons.india.utils.AppConstants
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class OnBoardingScreenActivity : BaseActivity<ActivityOnBoardingScreenBinding, OnBoardingScreenVM>(), OnBoardingScreenNavigator, OnStartedDashboardClicked {
    override val bindingVariable: Int = BR.onBoardingScreenVM
    override val layoutId: Int = R.layout.activity_on_boarding_screen
    override val viewModelClass: Class<OnBoardingScreenVM> = OnBoardingScreenVM::class.java

    var selectedPage = 0
    var ivArrayDotsPager: Array<ImageView?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        viewDataBinding?.pager?.adapter = OnBoardingPagerAdapter(this@OnBoardingScreenActivity,this@OnBoardingScreenActivity)
        setupPagerIndidcatorDots()

        viewDataBinding?.pager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//                selectedPage = position;
            }

            override fun onPageSelected(position: Int) {
                selectedPage = position
                for (imageView in ivArrayDotsPager!!) {
                    imageView?.setImageResource(R.drawable.tab_unselected)
                }
                ivArrayDotsPager!![position]?.setImageResource(R.drawable.tab_selected)
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    override  fun init() {
        mViewModel.navigator = this
    }


    override fun navigateToHomeActivity() {
        AppPreference.addValue(PreferenceKeys.GET_STARTED, AppConstants.Selected)
        ActivityNavigator.clearAllActivity(this@OnBoardingScreenActivity, HomeActivity::class.java)
    }

    override fun onGetStartDashBoardClicked(position: Int, view: View, value: String) {
        when(view.id){
            R.id.iv_GetStartApp ->  {
                mViewModel.naviGateToHomeActivity()
            }
        }
    }

    class OnBoardingPagerAdapter(onBoardingScreenBinding: OnBoardingScreenActivity?, val onStartedDashboardClicked: OnStartedDashboardClicked) : PagerAdapter() {
        private var mContext: Context? = onBoardingScreenBinding

        override fun instantiateItem(collection: ViewGroup, position: Int): Any {
            val layout = LayoutInflater.from(mContext)
                .inflate(R.layout.on_boarding_view_pager, collection, false) as ViewGroup
            val screen1: ConstraintLayout = layout.findViewById(R.id.screen1)
            val screen2: ConstraintLayout = layout.findViewById(R.id.screen2)
            val screen3: ConstraintLayout = layout.findViewById(R.id.screen3)
            val getStartApp: CardView = layout.findViewById(R.id.iv_GetStartApp)
            if (position == 0) {
                screen1.visibility = View.VISIBLE
                screen2.visibility = View.GONE
                screen3.visibility = View.GONE
            } else if (position == 1) {
                screen1.visibility = View.GONE
                screen2.visibility = View.VISIBLE
                screen3.visibility = View.GONE
            } else if (position == 2) {
                screen1.visibility = View.GONE
                screen2.visibility = View.GONE
                screen3.visibility = View.VISIBLE
            }

            getStartApp.setOnClickListener(View.OnClickListener { view ->
                onStartedDashboardClicked.onGetStartDashBoardClicked(position, view,"")
            })

            collection.addView(layout)
            return layout
        }

        override fun getCount(): Int {
            return 3
        }


        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View?)
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

    }

    private fun setupPagerIndidcatorDots() {
        ivArrayDotsPager = arrayOfNulls(3)
        for (i in ivArrayDotsPager?.indices!!) {
            ivArrayDotsPager?.set(i, ImageView(this))
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(5, 0, 5, 0)
            ivArrayDotsPager?.get(i)?.layoutParams ?: params
            ivArrayDotsPager?.get(i)?.setImageResource(R.drawable.tab_unselected)
            ivArrayDotsPager?.get(i)?.setOnClickListener { view -> view.alpha = 1f }
            viewDataBinding?.pagerDots?.addView(ivArrayDotsPager?.get(i))
            viewDataBinding?.pagerDots?.bringToFront()
            ivArrayDotsPager?.get(0)?.setImageResource(R.drawable.tab_selected)
        }
    }

}