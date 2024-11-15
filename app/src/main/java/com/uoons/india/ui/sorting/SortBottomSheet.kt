package com.uoons.india.ui.sorting

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.databinding.SortBottomSheetBinding
import com.uoons.india.ui.base.BaseBottomSheetDialogFrag
import com.uoons.india.ui.sorting.adapter.SortingAdapter
import com.uoons.india.ui.sorting.model.SortingDataModel
import android.annotation.SuppressLint
import android.widget.RadioButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.uoons.india.ui.product_list.model.SortByNameModel
import com.uoons.india.ui.sorting.model.SortingModel
import org.lsposed.lsparanoid.Obfuscate

@Suppress("DEPRECATION")
@Obfuscate
class SortBottomSheet(var onclick:(sortId:String)->Unit) : BaseBottomSheetDialogFrag<SortBottomSheetBinding, SortBottomSheetVM>(),
    SortBottomSheetNavigator {

    private  val TAG = "SortBottomSheet"
    override val bindingVariable: Int= BR.sortBottomSheetVM
    override val layoutId: Int = R.layout.sort_bottom_sheet
    override val viewModelClass: Class<SortBottomSheetVM> = SortBottomSheetVM::class.java
    lateinit var sortingAdapter : SortingAdapter
    var lstSortData: ArrayList<SortingDataModel> = ArrayList<SortingDataModel>()
    var selectedRadioButton: RadioButton? = null
    var sortingId : Int = 0
    private var sortingByNameProductsList: ArrayList<SortByNameModel> = ArrayList<SortByNameModel>()

    override   fun init() {
        mViewModel.navigator = this

        sortingAdapter = SortingAdapter(onclick = {
            onclick.invoke(it)
            dismiss()
        })

        if (arguments != null) {
            sortingByNameProductsList =
                (requireArguments().getSerializable("sortingList") as ArrayList<SortByNameModel>?)!!
            context?.let { getSortingData(sortingByNameProductsList, it) }
        }

        setUpBinding()

    }

   @SuppressLint("NotifyDataSetChanged")
   private fun getSortingData(sortingByNameProductsList: ArrayList<SortByNameModel>, context: Context) {
        sortingAdapter.setSortingData(sortingByNameProductsList,context)
        viewDataBinding.rcvSortingData.adapter = sortingAdapter
        sortingAdapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG,"onCreate")
    }

    fun newInstance(): SortBottomSheet {
        return SortBottomSheet(onclick)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(TAG,"onViewCreated")
        init()
        viewDataBinding.rcvSortingData.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        }
    }

    private fun setUpBinding(){
        // mViewModel.getSortingApiCall()
        viewDataBinding.rcvSortingData.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        }
    }

    override fun dismissDialog() {
        dismiss()
        Log.e(TAG,"dismissDialog")
    }

    override fun getSortingOptionsResponse() {
        /*if(view !=null){
            mViewModel.getSortingDataResponse.observe(viewLifecycleOwner, Observer<SortingModel> {
                if (it != null){
                    context?.let {
                            it2 ->setSortingOptionsAdapterData(it,it2)
                    }
                }else{
                    Toast.makeText(requireActivity(), "Error in fetching data", Toast.LENGTH_LONG).show()
                }
            })
        }*/

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSortingOptionsAdapterData(data: SortingModel, context: Context){
      //  sortingAdapter.setSortingData(data,context)
      //  viewDataBinding.rcvSortingData.adapter = sortingAdapter
      //  sortingAdapter.notifyDataSetChanged()
    }


}