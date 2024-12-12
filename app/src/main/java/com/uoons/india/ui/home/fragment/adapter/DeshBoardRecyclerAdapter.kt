package com.uoons.india.ui.home.fragment.adapter

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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smarteist.autoimageslider.SliderView
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.ui.home.fragment.model.DeshBoardItems
import com.uoons.india.ui.home.fragment.model.DeshBoardModel
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate
import java.text.NumberFormat
import java.util.Locale

@Obfuscate
class DeshBoardRecyclerAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //  private var LOG_TAG = DeshBoardRecyclerAdapter.Companion::class.Companion::class.java.name


    var allHomeItemsList: DeshBoardModel = DeshBoardModel()
    var allJwelleryItemList: DeshBoardModel = DeshBoardModel()

    // Top most UI Component -> Home Category items
    private val homeCategoryFragmentAdapter = HomeCategoryFragmentAdapter()

    // Slider One Items
    private val sliderOneAdapter = SliderOneAdapter()

    // Price Store Items
    private val priceStoreItemRecyclerAdapter = PriceStoreItemRecyclerAdapter()

    // Recently Views Items
    private val homeRecentlyViewRecyclerAdapter = HomeRecentlyViewRecyclerAdapter()

    // Deal of The Day Items
    private val dealOfTheDayItemRecyclerAdapter = DealOfTheDayItemRecyclerAdapter()

    // Advertisement home page
    private val homeAdvertisementAdapter = HomeAdvertisementAdapter()

    // SaasAdapter
    private val saasAdapter = SaasOneImageAdapter()

    // Slider Two Items
    private val sliderTwoAdapter = SliderTwoAdapter()

    private var fourPhotoesAdapter = FourPhotoesAdapter()

    //For the Top Slider of the App in Home Screen
    val imgList: ArrayList<Int> = arrayListOf<Int>()

    // For the four images on the homescreen and Saas Image
    val image: ArrayList<Int> = arrayListOf<Int>()
    var TextData: ArrayList<String> = arrayListOf<String>()

    // Slider Two Items
    private val sliderThreeAdapter = SliderThreeAdapter()

    // New Arrivals Items
    private val newArrivalsItemRecyclerAdapter = NewArrivalsItemRecyclerAdapter()

    // Price Store Items
    private val someProductsRecyclerAdapter = SomeProductsRecyclerAdapter()

    // Mobile And Tablets Items
    private val trendingNowItemRecyclerAdapter = TrendingNowItemRecyclerAdapter()

    lateinit var context: Context
    private var mainCustomClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClicked(subId: String, parentID: Int, categoryName: String)
    }

    fun setOnItemClickListener(mItemClick: OnItemClickListener) {
        this.mainCustomClickListener = mItemClick
    }

    fun setItemsList(data: DeshBoardModel, context: Context) {
        this.allHomeItemsList = data
        this.context = context
    }

    fun setJwellaryItemsList(data: DeshBoardModel) {
        this.allJwelleryItemList = data
    }

    private var customProductIdClickListener: OnProductIdClickListener? = null

    interface OnProductIdClickListener {
        fun onProductIdClicked(pId: String)
    }

    fun setOnProductIdClickListener(mItemClick: OnProductIdClickListener) {
        this.customProductIdClickListener = mItemClick
    }

    companion object {
        const val CATEGORIES_TYPE = 1
        const val SLIDERS_ONE_TYPE = 2
        const val PRICE_STORE_TYPE = 3
        const val DEAL_OF_THE_DAY_TYPE = 4
        const val SLIDERS_TWO_TYPE = 5
        const val RECOMMENDED_ITEMS_TYPE = 6
        const val ADVERTISEMENT_TYPE = 7
        const val RECENTLY_VIEW_TYPE = 8
        const val SLIDERS_THREE_TYPE = 9
        const val SUGGESTION_FOR_TYPE = 10
        const val NEW_ARRIVALS_TYPE = 11
        const val MORE_ITEMS_TYPE = 12
        const val TRENDING_NOW_TYPE = 13
        const val FOUR_PHOTOES = 1
        const val JWELLARY = 1
    }

    override fun getItemViewType(position: Int): Int {
        return allHomeItemsList.Data[position].id!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CATEGORIES_TYPE -> {
                val layout1: View = LayoutInflater.from(context)
                    .inflate(R.layout.home_categories_layout, parent, false)
                LayoutCategoriesViewHolder(layout1)
            }

            SLIDERS_ONE_TYPE -> {
                val layout2: View =
                    LayoutInflater.from(context).inflate(R.layout.home_slider_layout, parent, false)
                LayoutSliderOneViewHolder(layout2)
            }

            PRICE_STORE_TYPE -> {
                val layout3: View = LayoutInflater.from(context)
                    .inflate(R.layout.home_price_store_layout, parent, false)
                LayoutPriceStoreViewHolder(layout3)
            }

            FOUR_PHOTOES -> {
                val layout14: View =
                    LayoutInflater.from(context)
                        .inflate(R.layout.home_deal_of_the_day_layout, parent, false)
                LayoutDealOfTheDayViewHolder(layout14)
            }


            DEAL_OF_THE_DAY_TYPE -> {
                val layout4: View = LayoutInflater.from(context)
                    .inflate(R.layout.home_deal_of_the_day_layout, parent, false)
                LayoutDealOfTheDayViewHolder(layout4)
            }


            SLIDERS_TWO_TYPE -> {
                val layout5: View = LayoutInflater.from(context)
                    .inflate(R.layout.home_slider_two_layout, parent, false)
                LayoutSliderTwoViewHolder(layout5)
            }

            RECOMMENDED_ITEMS_TYPE -> {
                val layout6: View = LayoutInflater.from(context)
                    .inflate(R.layout.home_recommended_item_layout, parent, false)
                LayoutRecommendedViewHolder(layout6)
            }

            ADVERTISEMENT_TYPE -> {
                val layout7: View = LayoutInflater.from(context)
                    .inflate(R.layout.home_advertisement_layout, parent, false)
                LayoutAdvertisementViewHolder(layout7)
            }

            RECENTLY_VIEW_TYPE -> {
                if (AppPreference.getValue(PreferenceKeys.ACCESS_TOKEN).isEmpty()) {
                    val layout8: View =
                        LayoutInflater.from(context).inflate(R.layout.empty_view, parent, false)
                    LayoutRecentlyViewsViewHolder(layout8)
                } else {
                    val layout8: View = LayoutInflater.from(context)
                        .inflate(R.layout.home_recently_views_layout, parent, false)
                    LayoutRecentlyViewsViewHolder(layout8)
                }
            }

            SLIDERS_THREE_TYPE -> {
                val layout9: View = LayoutInflater.from(context)
                    .inflate(R.layout.home_slider_three_layout, parent, false)
                LayoutSliderThreeViewHolder(layout9)
            }

            SUGGESTION_FOR_TYPE -> {
                val layout10: View = LayoutInflater.from(context)
                    .inflate(R.layout.home_suggestion_item_layout, parent, false)
                LayoutSuggestionViewHolder(layout10)
            }

            NEW_ARRIVALS_TYPE -> {
                val layout11: View = LayoutInflater.from(context)
                    .inflate(R.layout.home_new_arrivals_layout, parent, false)
                LayoutNewArrivalsViewHolder(layout11)
            }

            MORE_ITEMS_TYPE -> {
                val layout12: View = LayoutInflater.from(context)
                    .inflate(R.layout.home_some_item_layout, parent, false)
                LayoutSomeProductsViewHolder(layout12)
            }

            TRENDING_NOW_TYPE -> {
                val layout13: View = LayoutInflater.from(context)
                    .inflate(R.layout.home_trending_now_layout, parent, false)
                LayoutTrendingNowViewHolder(layout13)
            }

            else -> emptyViewHolder(
                LayoutInflater.from(context).inflate(R.layout.empty_view, parent, false)
            )
        }
    }

    private inner class emptyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Home Categories Items
        homeCategoryFragmentAdapter.setOnItemClickListener(object :
            HomeCategoryFragmentAdapter.OnItemClickListener {
            override fun onItemClicked(position: String, type: String) {
                mainCustomClickListener?.onItemClicked(position, FOUR_PHOTOES, type)
            }
        })

        fourPhotoesAdapter.setOnItemClickListener(
            object : FourPhotoesAdapter.OnItemClickListener {
                override fun onItemClicked(position: String, type: String) {
                    mainCustomClickListener?.onItemClicked(position, CATEGORIES_TYPE, type)
                }
            }
        )


        // Auto Sliders One Items
        sliderOneAdapter.setOnItemClickListener(
            object :
                SliderOneAdapter.OnItemClickListener {
                override fun onItemClicked(position: String, type: String) {
                    mainCustomClickListener?.onItemClicked(position, SLIDERS_ONE_TYPE, type)
                }
            })


        // Price Store Items
        priceStoreItemRecyclerAdapter.setOnItemClickListener(
            object :
                PriceStoreItemRecyclerAdapter.OnItemClickListener {
                override fun onItemClicked(position: String, type: String) {
                    mainCustomClickListener?.onItemClicked(position, PRICE_STORE_TYPE, type)
                }
            })


        // Deal of the Day Items in Home UI
        dealOfTheDayItemRecyclerAdapter.setOnItemClickListener(
            object :
                DealOfTheDayItemRecyclerAdapter.OnProductIdClickListener {
                override fun onProductIdClicked(pId: String) {
                    customProductIdClickListener?.onProductIdClicked(pId)
                }
            })


        // Recently Views Items
        homeRecentlyViewRecyclerAdapter.setOnItemClickListener(
            object :
                HomeRecentlyViewRecyclerAdapter.OnProductIdClickListener {
                override fun onProductIdClicked(pId: String) {
                    customProductIdClickListener?.onProductIdClicked(pId)
                }
            })

        // Advertisement Home page
        homeAdvertisementAdapter.setOnItemClickListener(
            object :
                HomeAdvertisementAdapter.OnProductIdClickListener {
                override fun onProductIdClicked(pId: String, sponsoredName: String) {
                    mainCustomClickListener?.onItemClicked(pId, ADVERTISEMENT_TYPE, sponsoredName)
                }
            })

        // Auto Sliders Two Items
        sliderTwoAdapter.setOnItemClickListener(
            object :
                SliderTwoAdapter.OnItemClickListener {
                override fun onItemClicked(position: String, type: String) {
                    mainCustomClickListener?.onItemClicked(position, SLIDERS_TWO_TYPE, type)
                }
            })

        // Auto Sliders Three Items
        sliderThreeAdapter.setOnItemClickListener(
            object :
                SliderThreeAdapter.OnItemClickListener {
                override fun onItemClicked(position: String, type: String) {
                    mainCustomClickListener?.onItemClicked(position, SLIDERS_THREE_TYPE, type)
                }
            })

        // New Arrivals Items
        newArrivalsItemRecyclerAdapter.setOnItemClickListener(
            object :
                NewArrivalsItemRecyclerAdapter.OnProductIdClickListener {
                override fun onProductIdClicked(pId: String) {
                    customProductIdClickListener?.onProductIdClicked(pId)
                }
            })

        // Some Products Items
        someProductsRecyclerAdapter.setOnItemClickListener(
            object :
                SomeProductsRecyclerAdapter.OnProductIdClickListener {
                override fun onProductIdClicked(pId: String) {
                    customProductIdClickListener?.onProductIdClicked(pId)
                }
            })

        // Trending Items
        trendingNowItemRecyclerAdapter.setOnItemClickListener(
            object :
                TrendingNowItemRecyclerAdapter.OnProductIdClickListener {
                override fun onProductIdClicked(pId: String) {
                    customProductIdClickListener?.onProductIdClicked(pId)
                }
            })




        when (allHomeItemsList.Data[position].id) {
            CATEGORIES_TYPE -> {
                (holder as LayoutCategoriesViewHolder).bind(position)
            }

            SLIDERS_ONE_TYPE -> {
                (holder as LayoutSliderOneViewHolder).bind(position)
            }

            PRICE_STORE_TYPE -> {
                (holder as LayoutPriceStoreViewHolder).bind(position)
            }

            DEAL_OF_THE_DAY_TYPE -> {
                (holder as LayoutDealOfTheDayViewHolder).bind(position)
            }

            // This will work for that four images into the Home activity
            FOUR_PHOTOES -> {
                (holder as LayoutDealOfTheDayViewHolder).bind(position)
            }

            SLIDERS_TWO_TYPE -> {
                (holder as LayoutSliderTwoViewHolder).bind(position)
            }

            RECOMMENDED_ITEMS_TYPE -> {
                (holder as LayoutRecommendedViewHolder).bind(position)
            }

            ADVERTISEMENT_TYPE -> {
                (holder as LayoutAdvertisementViewHolder).bind(position)
            }

            RECENTLY_VIEW_TYPE -> {
                (holder as LayoutRecentlyViewsViewHolder).bind(position)
            }

            SLIDERS_THREE_TYPE -> {
                (holder as LayoutSliderThreeViewHolder).bind(position)
            }

            SUGGESTION_FOR_TYPE -> {
                (holder as LayoutSuggestionViewHolder).bind(position)
            }

            NEW_ARRIVALS_TYPE -> {
                (holder as LayoutNewArrivalsViewHolder).bind(position)
            }

            MORE_ITEMS_TYPE -> {
                (holder as LayoutSomeProductsViewHolder).bind(position)
            }

            TRENDING_NOW_TYPE -> {
                (holder as LayoutTrendingNowViewHolder).bind(position)
            }
        }


    }

    override fun getItemCount(): Int {
        //    Log.d("Click", "size is " + allHomeItemsList.Data.size)
        return allHomeItemsList.Data.size
    }

    private inner class LayoutCategoriesViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var itemRecycler: RecyclerView = itemView.findViewById(R.id.rcvDeshboardCategory)
        fun bind(position: Int) {
            setCategoriesItemRecycler(itemRecycler, allHomeItemsList.Data[position].items)
            //   Log.d("Click", "${allHomeItemsList.Data[position].id}")
        }

    }


    /*   private inner class LayoutFourPhotoesViewHolder(itemView: View) :
           RecyclerView.ViewHolder(itemView) {
           var recycler: RecyclerView = itemView.findViewById(R.id.rcvAllCategoryPhotoes)
           fun bind(position: Int) {
               LayoutDealOfTheDayViewHolderPhotoes(
                   recycler,
                   imageListFourPhotoes,
                   allHomeItemsList.Data[position].items
               )
               Log.d("RajatTag", "1")
           }

       }
     */


    private inner class LayoutSliderOneViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var slider: SliderView = itemView.findViewById(R.id.slider)
        fun bind(position: Int) {
            setSliderItemRecycler(slider, allHomeItemsList.Data[position].items)
            Log.d("Click", "${allHomeItemsList.Data[position].id}")
        }

    }

    private inner class LayoutSliderTwoViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var sliderTwo: SliderView = itemView.findViewById(R.id.sliderTwo)
        fun bind(position: Int) {
            setSliderTwoItemRecycler(sliderTwo, allHomeItemsList.Data[position].items)
            Log.d("Click", "${allHomeItemsList.Data[position].id}")
        }
    }

    private inner class LayoutSliderThreeViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var sliderThree: SliderView = itemView.findViewById(R.id.sliderThree)
        fun bind(position: Int) {
            setSliderThreeItemRecycler(sliderThree, allHomeItemsList.Data[position].items)
        }
    }

    private inner class LayoutRecommendedViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var title: TextView? = itemView.findViewById(R.id.txtCategoryName)
        var crdViewAllProducts: CardView = itemView.findViewById(R.id.crdViewAllProducts)

        var llRecommendedOne: LinearLayout = itemView.findViewById(R.id.llRecommendedOne)
        var llRecommendedTwo: LinearLayout = itemView.findViewById(R.id.llRecommendedTwo)
        var llRecommendedThree: LinearLayout = itemView.findViewById(R.id.llRecommendedThree)

        var ivRecommendedImageOne: ImageView = itemView.findViewById(R.id.ivRecommendedImageOne)
        var ivRecommendedImageTwo: ImageView = itemView.findViewById(R.id.ivRecommendedImageTwo)
        var ivRecommendedImageThree: ImageView =
            itemView.findViewById(R.id.ivRecommendedImageThree)

        var txvProductNameOne: TextView = itemView.findViewById(R.id.txvProductNameOne)
        var txvProductNameTwo: TextView = itemView.findViewById(R.id.txvProductNameTwo)
        var txvProductNameThree: TextView = itemView.findViewById(R.id.txvProductNameThree)

        var txvSellingPriceOne: TextView = itemView.findViewById(R.id.txvSellingPriceOne)
        var txvSellingPriceTwo: TextView = itemView.findViewById(R.id.txvSellingPriceTwo)
        var txvSellingPriceThree: TextView = itemView.findViewById(R.id.txvSellingPriceThree)

        var txvMRPPriceOne: TextView = itemView.findViewById(R.id.txvMRPPriceOne)
        var txvMRPPriceTwo: TextView = itemView.findViewById(R.id.txvMRPPriceTwo)
        var txvMRPPriceThree: TextView = itemView.findViewById(R.id.txvMRPPriceThree)

        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {
            title?.text = allHomeItemsList.Data[position].name
            if (allHomeItemsList.Data[position].items.size >= 2) {
                CommonUtils.loadImage(
                    ivRecommendedImageOne,
                    allHomeItemsList.Data[position].items[0].productImages,
                    ivRecommendedImageOne.id
                )
                CommonUtils.loadImage(
                    ivRecommendedImageTwo,
                    allHomeItemsList.Data[position].items[1].productImages,
                    ivRecommendedImageTwo.id
                )
                CommonUtils.loadImage(
                    ivRecommendedImageThree,
                    allHomeItemsList.Data[position].items[2].productImages,
                    ivRecommendedImageThree.id
                )

                txvProductNameOne.text = allHomeItemsList.Data[position].items[0].productName
                txvProductNameTwo.text = allHomeItemsList.Data[position].items[1].productName
                txvProductNameThree.text = allHomeItemsList.Data[position].items[2].productName

                txvSellingPriceOne.text =
                    context.getString(R.string.offer_price) + " " + context.getString(R.string.rupees) + NumberFormat.getNumberInstance(
                        Locale.getDefault()
                    ).format(allHomeItemsList.Data[position].items[0].productSalePrice?.toInt())
                txvSellingPriceTwo.text =
                    context.getString(R.string.offer_price) + " " + context.getString(R.string.rupees) + NumberFormat.getNumberInstance(
                        Locale.getDefault()
                    ).format(allHomeItemsList.Data[position].items[1].productSalePrice?.toInt())
                txvSellingPriceThree.text =
                    context.getString(R.string.offer_price) + " " + context.getString(R.string.rupees) + NumberFormat.getNumberInstance(
                        Locale.getDefault()
                    ).format(allHomeItemsList.Data[position].items[2].productSalePrice?.toInt())

                txvMRPPriceOne.text =
                    context.getString(R.string.m_r_p) + " " + context.getString(R.string.rupees) + NumberFormat.getNumberInstance(
                        Locale.getDefault()
                    ).format(allHomeItemsList.Data[position].items[0].productPrice?.toInt())
                txvMRPPriceTwo.text =
                    context.getString(R.string.m_r_p) + " " + context.getString(R.string.rupees) + NumberFormat.getNumberInstance(
                        Locale.getDefault()
                    ).format(allHomeItemsList.Data[position].items[1].productPrice?.toInt())
                txvMRPPriceThree.text =
                    context.getString(R.string.m_r_p) + " " + context.getString(R.string.rupees) + NumberFormat.getNumberInstance(
                        Locale.getDefault()
                    ).format(allHomeItemsList.Data[position].items[2].productPrice?.toInt())

                crdViewAllProducts.setOnClickListener({
                    mainCustomClickListener?.onItemClicked(
                        allHomeItemsList.Data[position].id.toString(),
                        RECOMMENDED_ITEMS_TYPE,
                        allHomeItemsList.Data[position].name.toString()
                    )
                })

                llRecommendedOne.setOnClickListener {
//                    Log.e(LOG_TAG,"ivRecommendedImageOne: "+allHomeItemsList.Data[position].items[0].pid.toString())
                    customProductIdClickListener?.onProductIdClicked(allHomeItemsList.Data[position].items[0].pid.toString())
                }

                llRecommendedTwo.setOnClickListener({
                    customProductIdClickListener?.onProductIdClicked(allHomeItemsList.Data[position].items[1].pid.toString())
                })

                llRecommendedThree.setOnClickListener({
                    customProductIdClickListener?.onProductIdClicked(allHomeItemsList.Data[position].items[2].pid.toString())
                })
            } else {
                try {
                    CommonUtils.loadImage(
                        ivRecommendedImageOne,
                        allHomeItemsList.Data[position].items[0].productImages,
                        ivRecommendedImageOne.id
                    )
                    CommonUtils.loadImage(
                        ivRecommendedImageTwo,
                        allHomeItemsList.Data[position].items[0].productImages,
                        ivRecommendedImageTwo.id
                    )
                    CommonUtils.loadImage(
                        ivRecommendedImageThree,
                        allHomeItemsList.Data[position].items[0].productImages,
                        ivRecommendedImageThree.id
                    )

                    txvProductNameOne.text =
                        allHomeItemsList.Data[position].items[0].productName
                    txvProductNameTwo.text =
                        allHomeItemsList.Data[position].items[0].productName
                    txvProductNameThree.text =
                        allHomeItemsList.Data[position].items[0].productName

                    txvSellingPriceOne.text =
                        context.getString(R.string.offer_price) + " " + context.getString(R.string.rupees) + allHomeItemsList.Data[position].items[0].productSalePrice
                    txvSellingPriceTwo.text =
                        context.getString(R.string.offer_price) + " " + context.getString(R.string.rupees) + allHomeItemsList.Data[position].items[0].productSalePrice
                    txvSellingPriceThree.text =
                        context.getString(R.string.offer_price) + " " + context.getString(R.string.rupees) + allHomeItemsList.Data[position].items[0].productSalePrice

                    txvMRPPriceOne.text =
                        context.getString(R.string.m_r_p) + " " + context.getString(R.string.rupees) + allHomeItemsList.Data[position].items[0].productPrice
                    txvMRPPriceTwo.text =
                        context.getString(R.string.m_r_p) + " " + context.getString(R.string.rupees) + allHomeItemsList.Data[position].items[0].productPrice
                    txvMRPPriceThree.text =
                        context.getString(R.string.m_r_p) + " " + context.getString(R.string.rupees) + allHomeItemsList.Data[position].items[0].productPrice

                    crdViewAllProducts.setOnClickListener({
                        mainCustomClickListener?.onItemClicked(
                            allHomeItemsList.Data[position].id.toString(),
                            RECOMMENDED_ITEMS_TYPE,
                            allHomeItemsList.Data[position].name.toString()
                        )
                    })

                    llRecommendedOne.setOnClickListener({
//                    Log.e(LOG_TAG,"ivRecommendedImageOne: "+allHomeItemsList.Data[position].items[0].pid.toString())
                        customProductIdClickListener?.onProductIdClicked(allHomeItemsList.Data[position].items[0].pid.toString())
                    })

                    llRecommendedTwo.setOnClickListener({
                        customProductIdClickListener?.onProductIdClicked(allHomeItemsList.Data[position].items[0].pid.toString())
                    })

                    llRecommendedThree.setOnClickListener({
                        customProductIdClickListener?.onProductIdClicked(allHomeItemsList.Data[position].items[0].pid.toString())
                    })
                } catch (e: IndexOutOfBoundsException) {
                    Log.e("TAG", "bind: " + e.printStackTrace())
                }

            }
        }
    }

    private inner class LayoutSuggestionViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var title: TextView? = itemView.findViewById(R.id.txtCategoryName)
        var crdViewAllProducts: CardView = itemView.findViewById(R.id.crdViewAllProducts)

        var llSuggestionOne: LinearLayout = itemView.findViewById(R.id.llSuggestionOne)
        var llSuggestionTwo: LinearLayout = itemView.findViewById(R.id.llSuggestionTwo)
        var llSuggestionThree: LinearLayout = itemView.findViewById(R.id.llSuggestionThree)

        var ivRecommendedImageOne: ImageView = itemView.findViewById(R.id.ivRecommendedImageOne)
        var ivRecommendedImageTwo: ImageView = itemView.findViewById(R.id.ivRecommendedImageTwo)
        var ivRecommendedImageThree: ImageView =
            itemView.findViewById(R.id.ivRecommendedImageThree)

        var txvProductNameOne: TextView = itemView.findViewById(R.id.txvProductNameOne)
        var txvProductNameTwo: TextView = itemView.findViewById(R.id.txvProductNameTwo)
        var txvProductNameThree: TextView = itemView.findViewById(R.id.txvProductNameThree)

        var txvSellingPriceOne: TextView = itemView.findViewById(R.id.txvSellingPriceOne)
        var txvSellingPriceTwo: TextView = itemView.findViewById(R.id.txvSellingPriceTwo)
        var txvSellingPriceThree: TextView = itemView.findViewById(R.id.txvSellingPriceThree)

        var txvMRPPriceOne: TextView = itemView.findViewById(R.id.txvMRPPriceOne)
        var txvMRPPriceTwo: TextView = itemView.findViewById(R.id.txvMRPPriceTwo)
        var txvMRPPriceThree: TextView = itemView.findViewById(R.id.txvMRPPriceThree)

        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {
            title?.text = allHomeItemsList.Data[position].name
            if (allHomeItemsList.Data[position].items.size >= 2) {
                CommonUtils.loadImage(
                    ivRecommendedImageOne,
                    allHomeItemsList.Data[position].items[0].productImages,
                    ivRecommendedImageOne.id
                )
                CommonUtils.loadImage(
                    ivRecommendedImageTwo,
                    allHomeItemsList.Data[position].items[1].productImages,
                    ivRecommendedImageTwo.id
                )
                CommonUtils.loadImage(
                    ivRecommendedImageThree,
                    allHomeItemsList.Data[position].items[2].productImages,
                    ivRecommendedImageThree.id
                )

                txvProductNameOne.text = allHomeItemsList.Data[position].items[0].productName
                txvProductNameTwo.text = allHomeItemsList.Data[position].items[1].productName
                txvProductNameThree.text = allHomeItemsList.Data[position].items[2].productName

                txvSellingPriceOne.text =
                    context.getString(R.string.offer_price) + " " + context.getString(R.string.rupees) + NumberFormat.getNumberInstance(
                        Locale.getDefault()
                    ).format(allHomeItemsList.Data[position].items[0].productSalePrice?.toInt())
                txvSellingPriceTwo.text =
                    context.getString(R.string.offer_price) + " " + context.getString(R.string.rupees) + NumberFormat.getNumberInstance(
                        Locale.getDefault()
                    ).format(allHomeItemsList.Data[position].items[1].productSalePrice?.toInt())
                txvSellingPriceThree.text =
                    context.getString(R.string.offer_price) + " " + context.getString(R.string.rupees) + NumberFormat.getNumberInstance(
                        Locale.getDefault()
                    ).format(allHomeItemsList.Data[position].items[2].productSalePrice?.toInt())

                txvMRPPriceOne.text =
                    context.getString(R.string.m_r_p) + " " + context.getString(R.string.rupees) + NumberFormat.getNumberInstance(
                        Locale.getDefault()
                    ).format(allHomeItemsList.Data[position].items[0].productPrice?.toInt())
                txvMRPPriceTwo.text =
                    context.getString(R.string.m_r_p) + " " + context.getString(R.string.rupees) + NumberFormat.getNumberInstance(
                        Locale.getDefault()
                    ).format(allHomeItemsList.Data[position].items[1].productPrice?.toInt())
                txvMRPPriceThree.text =
                    context.getString(R.string.m_r_p) + " " + context.getString(R.string.rupees) + NumberFormat.getNumberInstance(
                        Locale.getDefault()
                    ).format(allHomeItemsList.Data[position].items[2].productPrice?.toInt())

                crdViewAllProducts.setOnClickListener({
                    mainCustomClickListener?.onItemClicked(
                        allHomeItemsList.Data[position].id.toString(),
                        SUGGESTION_FOR_TYPE,
                        allHomeItemsList.Data[position].name.toString()
                    )
                })

                llSuggestionOne.setOnClickListener({
//  Log.e(LOG_TAG,"ivRecommendedImageOne: "+allHomeItemsList.Data[position].items[0].pid.toString())
                    customProductIdClickListener?.onProductIdClicked(allHomeItemsList.Data[position].items[0].pid.toString())
                })

                llSuggestionTwo.setOnClickListener({
                    customProductIdClickListener?.onProductIdClicked(allHomeItemsList.Data[position].items[1].pid.toString())
                })

                llSuggestionThree.setOnClickListener({
                    customProductIdClickListener?.onProductIdClicked(allHomeItemsList.Data[position].items[2].pid.toString())
                })
            } else {
                CommonUtils.loadImage(
                    ivRecommendedImageOne,
                    allHomeItemsList.Data[position].items[0].productImages,
                    ivRecommendedImageOne.id
                )
                CommonUtils.loadImage(
                    ivRecommendedImageTwo,
                    allHomeItemsList.Data[position].items[0].productImages,
                    ivRecommendedImageTwo.id
                )
                CommonUtils.loadImage(
                    ivRecommendedImageThree,
                    allHomeItemsList.Data[position].items[0].productImages,
                    ivRecommendedImageThree.id
                )

                txvProductNameOne.text = allHomeItemsList.Data[position].items[0].productName
                txvProductNameTwo.text = allHomeItemsList.Data[position].items[0].productName
                txvProductNameThree.text = allHomeItemsList.Data[position].items[0].productName

                txvSellingPriceOne.text =
                    context.getString(R.string.offer_price) + " " + context.getString(R.string.rupees) + allHomeItemsList.Data[position].items[0].productSalePrice
                txvSellingPriceTwo.text =
                    context.getString(R.string.offer_price) + " " + context.getString(R.string.rupees) + allHomeItemsList.Data[position].items[0].productSalePrice
                txvSellingPriceThree.text =
                    context.getString(R.string.offer_price) + " " + context.getString(R.string.rupees) + allHomeItemsList.Data[position].items[0].productSalePrice

                txvMRPPriceOne.text =
                    context.getString(R.string.m_r_p) + " " + context.getString(R.string.rupees) + allHomeItemsList.Data[position].items[0].productPrice
                txvMRPPriceTwo.text =
                    context.getString(R.string.m_r_p) + " " + context.getString(R.string.rupees) + allHomeItemsList.Data[position].items[0].productPrice
                txvMRPPriceThree.text =
                    context.getString(R.string.m_r_p) + " " + context.getString(R.string.rupees) + allHomeItemsList.Data[position].items[0].productPrice

                crdViewAllProducts.setOnClickListener({
                    mainCustomClickListener?.onItemClicked(
                        allHomeItemsList.Data[position].id.toString(),
                        SUGGESTION_FOR_TYPE,
                        allHomeItemsList.Data[position].name.toString()
                    )
                })

                llSuggestionOne.setOnClickListener({
//                    Log.e(LOG_TAG,"ivRecommendedImageOne: "+allHomeItemsList.Data[position].items[0].pid.toString())
                    customProductIdClickListener?.onProductIdClicked(allHomeItemsList.Data[position].items[0].pid.toString())
                })

                llSuggestionTwo.setOnClickListener({
                    customProductIdClickListener?.onProductIdClicked(allHomeItemsList.Data[position].items[0].pid.toString())
                })

                llSuggestionThree.setOnClickListener({
                    customProductIdClickListener?.onProductIdClicked(allHomeItemsList.Data[position].items[0].pid.toString())
                })
            }
        }
    }

    private inner class LayoutPriceStoreViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var priceStoreItemsRecycler: RecyclerView =
            itemView.findViewById(R.id.rvcPriceStoreDeshboard)
        var title: TextView? = itemView.findViewById(R.id.txtCategoryName)
        fun bind(position: Int) {
            setPriceStoreItemRecycler(
                priceStoreItemsRecycler,
                allHomeItemsList.Data[position].items
            )
            Log.d("Click", "${allHomeItemsList.Data[position].id}")
            title?.text = allHomeItemsList.Data[position].name
        }
    }


    private inner class LayoutRecentlyViewsViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var crdRecentlyView: CardView = itemView.findViewById(R.id.crdRecentlyView)
        var crdSeeAllCategory: ConstraintLayout = itemView.findViewById(R.id.crdSeeAllCategory)
        var rcvRecentlyViews: RecyclerView = itemView.findViewById(R.id.rcvRecentlyViews)
        var crdView: CardView? = itemView.findViewById(R.id.crdRecentlyView)
        var title: TextView? = itemView.findViewById(R.id.txtCategoryName)
        fun bind(position: Int) {
            if (allHomeItemsList.Data[position].items.isEmpty()) {
                crdSeeAllCategory.visibility = View.GONE
                crdRecentlyView.visibility = View.GONE
            } else {
                crdSeeAllCategory.visibility = View.VISIBLE
                crdRecentlyView.visibility = View.VISIBLE
            }
            setRecentlyViewsRecycler(rcvRecentlyViews, allHomeItemsList.Data[position].items)
            title?.text = allHomeItemsList.Data[position].name
            if (allHomeItemsList.Data[position].items.size <= 0) {
                crdView?.visibility = View.GONE
            } else {
                title?.text = allHomeItemsList.Data[position].name
            }
        }
    }

    private inner class LayoutSomeProductsViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var someProductsItemsRecycler: RecyclerView =
            itemView.findViewById(R.id.rcvSomeProducts)

        fun bind(position: Int) {
            setSomeProductItemRecycler(
                someProductsItemsRecycler,
                allHomeItemsList.Data[position].items
            )
        }
    }


    private inner class LayoutNewArrivalsViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var crdViewAllProducts: CardView = itemView.findViewById(R.id.crdViewAllProducts)
        var categoryTitle: TextView? = itemView.findViewById(R.id.txtCategoryName)
        var itemRecycler: RecyclerView? = itemView.findViewById(R.id.rcvAllCategory)
        fun bind(position: Int) {
            itemRecycler?.let {
                setNewArrivalsItemRecycler(
                    it,
                    allHomeItemsList.Data[position].items
                )
            }
            categoryTitle?.text = allHomeItemsList.Data[position].name
            crdViewAllProducts.setOnClickListener({
                mainCustomClickListener?.onItemClicked(
                    position.toString(),
                    NEW_ARRIVALS_TYPE,
                    allHomeItemsList.Data[position].name.toString()
                )
            })
        }
    }


    private inner class LayoutDealOfTheDayViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var crdViewAllProducts: CardView = itemView.findViewById(R.id.crdViewAllProducts)
        var categoryTitle: TextView = itemView.findViewById(R.id.txtCategoryName)
        var itemRecycler: RecyclerView = itemView.findViewById(R.id.rcvAllCategory)
        var recyclerViewPhotoes: RecyclerView = itemView.findViewById(R.id.fourPhotoesRecycler)
        var recyclerSaas: RecyclerView = itemView.findViewById(R.id.recyclerSaas)
        fun bind(position: Int) {
            setDealOfTheDayItemRecycler(
                itemRecycler,
                recyclerSaas,
                recyclerViewPhotoes,
                allHomeItemsList.Data[position].items,
                allHomeItemsList.Data[1].items
            )
            categoryTitle.text = allHomeItemsList.Data[position].name
            crdViewAllProducts.setOnClickListener {
                mainCustomClickListener?.onItemClicked(
                    position.toString(),
                    DEAL_OF_THE_DAY_TYPE,
                    allHomeItemsList.Data[position].name.toString()
                )
            }
        }
    }

    private inner class LayoutAdvertisementViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var rcvAdvertisement: RecyclerView = itemView.findViewById(R.id.rcvAdvertisement)
        fun bind(position: Int) {
            setAdvertisementRecycler(rcvAdvertisement, allHomeItemsList.Data[position].items)
        }
    }

    // Four images Layout //
    /* private inner class LayoutDealOfTheDayViewHolderPhotoes(itemView: View) :
         RecyclerView.ViewHolder(itemView) {
         var itemRecycler: RecyclerView = itemView.findViewById(R.id.fourPhotoesRecycler)
         fun bind(position: Int) {
             setDealOfTheDayItemRecycler(itemRecycler, allHomeItemsList.Data[position].items)
         }
     }

     */


    private inner class LayoutTrendingNowViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var crdViewAllProducts: CardView = itemView.findViewById(R.id.crdViewAllProducts)
        var categoryTitle: TextView = itemView.findViewById(R.id.txtCategoryName)
        var itemRecycler: RecyclerView = itemView.findViewById(R.id.rcvAllTrendingProducts)
        fun bind(position: Int) {
            setTrendingNowItemRecycler(itemRecycler, allHomeItemsList.Data[position].items)
            categoryTitle.text = allHomeItemsList.Data[position].name
            crdViewAllProducts.setOnClickListener({
                mainCustomClickListener?.onItemClicked(
                    position.toString(),
                    TRENDING_NOW_TYPE,
                    allHomeItemsList.Data[position].name.toString()
                )
            })
        }
    }

    private fun setCategoriesItemRecycler(
        itemRecycler: RecyclerView,
        categoryItemList: ArrayList<DeshBoardItems>,
    ) {
        imgList.add(R.drawable.audio)
        imgList.add(R.drawable.gaming)
        imgList.add(R.drawable.refurnish)
        imgList.add(R.drawable.pheripheral)
        imgList.add(R.drawable.mobile_accessories)
        imgList.add(R.drawable.appliances)
        imgList.add(R.drawable.smart_home_icon)
        imgList.add(R.drawable.laptop_icon)
        imgList.add(R.drawable.camera)
        imgList.add(R.drawable.smart_gedget)
        imgList.add(R.drawable.personal_and_health)
        imgList.add(R.drawable.mobile)
        imgList.add(R.drawable.industry_zone)
        imgList.add(R.drawable.computer_and_desktop)
        imgList.add(R.drawable.automotive)
        imgList.add(R.drawable.services)
        imgList.add(R.drawable.jwellary)
        imgList.add(R.drawable.artision)
        homeCategoryFragmentAdapter.setAllCategoriesList(categoryItemList, context, imgList)
        itemRecycler.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        itemRecycler.adapter = homeCategoryFragmentAdapter
    }

    private fun setPriceStoreItemRecycler(
        priceStoreItemsRecycler: RecyclerView,
        categoryItemList: ArrayList<DeshBoardItems>,
    ) {
        priceStoreItemRecyclerAdapter.setAllPriceStoreList(categoryItemList, context)
        priceStoreItemsRecycler.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        priceStoreItemsRecycler.adapter = priceStoreItemRecyclerAdapter
    }

    // Used to show that four images on the Home Activity
    /*  private fun SetFourPhotoesRecycler(
          recycler: RecyclerView,
          items: ArrayList<Int>,
          itemCategory: ArrayList<DeshBoardItems>,
      ) {
          fourPhotoesAdapter.setData(itemCategory, context)
          recycler.layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
          recycler.adapter = fourPhotoesAdapter
      }
     */


    private fun setRecentlyViewsRecycler(
        recentlyViewItemsRecycler: RecyclerView,
        categoryItemList: ArrayList<DeshBoardItems>,
    ) {
        homeRecentlyViewRecyclerAdapter.setRecentlyViewsList(categoryItemList, context)
        recentlyViewItemsRecycler.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recentlyViewItemsRecycler.adapter = homeRecentlyViewRecyclerAdapter
    }


    private fun setSomeProductItemRecycler(
        someProductItemsRecycler: RecyclerView,
        categoryItemList: ArrayList<DeshBoardItems>,
    ) {
        someProductsRecyclerAdapter.setAllPriceStoreList(categoryItemList, context)
        someProductItemsRecycler.adapter = someProductsRecyclerAdapter
    }


    private fun setNewArrivalsItemRecycler(
        recyclerView: RecyclerView,
        categoryItemList: ArrayList<DeshBoardItems>,
    ) {
        newArrivalsItemRecyclerAdapter.setData(categoryItemList, context)
        recyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = newArrivalsItemRecyclerAdapter
    }


    private fun setSliderItemRecycler(
        sliderView: SliderView,
        categoryItemList: ArrayList<DeshBoardItems>,
    ) {
        sliderOneAdapter.setData(categoryItemList, context)
        sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
        sliderView.setSliderAdapter(sliderOneAdapter)
        sliderView.scrollTimeInSec = 3
        sliderView.isAutoCycle = true // adapter
        sliderView.startAutoCycle()
    }


    private fun setSliderTwoItemRecycler(
        sliderTwoView: SliderView,
        categoryItemList: ArrayList<DeshBoardItems>,
    ) {
        sliderTwoAdapter.setData(categoryItemList, context)
        sliderTwoView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
        sliderTwoView.setSliderAdapter(sliderTwoAdapter)
        sliderTwoView.scrollTimeInSec = 3
        sliderTwoView.isAutoCycle = true
        sliderTwoView.startAutoCycle()
    }

    private fun setSliderThreeItemRecycler(
        sliderThreeView: SliderView,
        categoryItemList: ArrayList<DeshBoardItems>,
    ) {
        sliderThreeAdapter.setData(categoryItemList, context)
        sliderThreeView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
        sliderThreeView.setSliderAdapter(sliderThreeAdapter)
        sliderThreeView.scrollTimeInSec = 3
        sliderThreeView.isAutoCycle = true
        sliderThreeView.startAutoCycle()
    }


    /*  private fun setDealOfTheDayItemRecyclerPhotoes(
          recyclerView: RecyclerView,
          categoryItemList: ArrayList<DeshBoardItems>,
      ) {
          image.add(R.drawable.ic_settings)
          image.add(R.drawable.ic_settings)
          image.add(R.drawable.ic_settings)
          image.add(R.drawable.ic_settings)
          Log.d("fourPhoto", "setDealOfTheDayItemRecyclerPhotoes: $image")
          fourPhotoesAdapter.setDataPhotes(categoryItemList,image,context)
          recyclerView.layoutManager = GridLayoutManager(context, 2, RecyclerView.HORIZONTAL, false)
          recyclerView.adapter = fourPhotoesAdapter
      }

     */

    private fun setDealOfTheDayItemRecycler(
        recyclerView: RecyclerView,
        recyclerSaas: RecyclerView,
        recyclerViewPhotoes: RecyclerView,
        categoryItemList: ArrayList<DeshBoardItems>,
        categoryName: ArrayList<DeshBoardItems>
    ) {
        getImageAndTextData()
        dealOfTheDayItemRecyclerAdapter.setData(categoryItemList, context)
        recyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = dealOfTheDayItemRecyclerAdapter


        // For Four Photoes Adapter
        fourPhotoesAdapter.setDataPhotes(TextData, image, context, categoryName)
        //  Log.d("Click",)
        recyclerViewPhotoes.layoutManager =
            GridLayoutManager(context, 2, RecyclerView.HORIZONTAL, false)
        recyclerViewPhotoes.adapter = fourPhotoesAdapter

        // Saas Adapter for Company Image & Logo
        saasAdapter.setData(context)
        recyclerSaas.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerSaas.adapter = saasAdapter
    }

    private fun getImageAndTextData() {
        image.add(R.drawable.boat_logo)
        image.add(R.drawable.boat_logo)
        image.add(R.drawable.services)
        image.add(R.drawable.jwellary)
        TextData.add("Boat")
        TextData.add("Artison Product")
        TextData.add("Services")
        TextData.add("Jewellery")
    }

    private fun setAdvertisementRecycler(
        recyclerView: RecyclerView,
        categoryItemList: ArrayList<DeshBoardItems>,
    ) {
        homeAdvertisementAdapter.setData(categoryItemList, context)
        recyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = homeAdvertisementAdapter
    }

    private fun setTrendingNowItemRecycler(
        recyclerView: RecyclerView,
        categoryItemList: ArrayList<DeshBoardItems>,
    ) {
        trendingNowItemRecyclerAdapter.setData(categoryItemList, context)
        recyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = trendingNowItemRecyclerAdapter
    }
}