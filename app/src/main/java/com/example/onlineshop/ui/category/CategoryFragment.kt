package com.example.onlineshop.ui.category


import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.onlineshop.NavGraphDirections
import com.example.onlineshop.R
import com.example.onlineshop.ViewModelFactory
import com.example.onlineshop.data.entity.customProduct.Product
import com.example.onlineshop.data.remoteDataSource.RemoteDataSourceImpl
import com.example.onlineshop.data.roomData.RoomDataSourceImpl
import com.example.onlineshop.data.roomData.RoomService
import com.example.onlineshop.databinding.FragmentCategoryBinding
import com.example.onlineshop.networkBase.NetworkChange
import com.example.onlineshop.repository.RepositoryImpl
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.cart_toolbar_view.view.*
import kotlinx.android.synthetic.main.list_toolbar_view.view.*


class CategoryFragment : Fragment(), SubRecyclerClick, MainRecyclerClick, ItemRecyclerClick {
    var mainCategoryIndex = 0
    var subCategoryIndex = 0
    var colID: Long = 268359696582
    lateinit var catViewModel: CategoryViewModel
    var products: List<Product>? = null
    var subList: List<Product>? = null
    lateinit var subcatList: Array<String>
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val application = requireNotNull(this.activity).application
        val repository = RepositoryImpl(
            RemoteDataSourceImpl(),
            RoomDataSourceImpl(RoomService.getInstance(application))
        )
        val viewModelFactory = ViewModelFactory(repository, application)
        catViewModel =
            ViewModelProvider(
                this, viewModelFactory
            )[CategoryViewModel::class.java]

        getProducts()
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onSubClick(position: Int) {
        super.onSubClick(position)
        subCategoryIndex = position
        subList = getSubCategoryItems(position)
        when {
            subList.isNullOrEmpty() -> {
                return
            }
            subList?.isEmpty() == true -> {
                binding.itemsRecView.adapter =
                    ItemCategoryAdapter(subList.orEmpty(), this)
                binding.placeHolder.visibility = View.VISIBLE

            }
            else -> {
                binding.placeHolder.visibility = View.GONE
                binding.itemsRecView.adapter =
                    ItemCategoryAdapter(subList.orEmpty(), this)
            }
        }
        binding.subcategoryRecView.adapter!!.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onMainClick(position: Int) {
        super.onMainClick(position)

        binding.subcategoryRecView.adapter = SubCategoryAdapter(subcatList, this)
        binding.subcategoryRecView.adapter!!.notifyDataSetChanged()
        mainCategoryIndex = position
        colID = getMainCategory(position)
        if (NetworkChange.isOnline) {
            binding.networkCatView.visibility = View.GONE
            binding.categoryLayout.visibility = View.VISIBLE
            catViewModel.fetchCatProducts(colID).observe(requireActivity()) {
                products = it
                binding.placeHolder.visibility = View.GONE
                binding.subcategoryRecView.visibility = View.VISIBLE
                binding.itemsRecView.adapter =
                    ItemCategoryAdapter(products ?: emptyList(), this)
                binding.itemsRecView.adapter!!.notifyDataSetChanged()
            }

        } else {
            binding.networkCatView.visibility = View.VISIBLE
            binding.categoryLayout.visibility = View.GONE

        }
        binding.mainCategoryRecView.adapter!!.notifyDataSetChanged()


    }

    private fun getProducts() {
        binding.shimmerFrameLayout1.startShimmer()
        binding.shimmerFrameLayout2.startShimmer()
        binding.shimmerFrameLayout3.startShimmer()
        binding.shimmerFrameLayout4.startShimmer()
        changeToolbar()
        subcatList = arrayOf("Shoes", "Accessories", "T-Shirts")
        var mainCatList = arrayOf("Women", "kids", "Men", "Sales")
        binding.subcategoryRecView.adapter = SubCategoryAdapter(subcatList, this)
        binding.mainCategoryRecView.adapter = MainCategoryAdapter(mainCatList, this)
        if (NetworkChange.isOnline) {

            binding.networkCatView.visibility = View.GONE
            binding.categoryLayout.visibility = View.VISIBLE
            catViewModel.fetchCatProducts(272069066799).observe(requireActivity()) {
                if (it != null) {
                    binding.shimmerFrameLayout1.stopShimmer()
                    binding.shimmerFrameLayout2.stopShimmer()
                    binding.shimmerFrameLayout3.stopShimmer()
                    binding.shimmerFrameLayout4.stopShimmer()

                    binding.shimmerFrameLayout1.visibility = View.GONE
                    binding.shimmerFrameLayout2.visibility = View.GONE
                    binding.shimmerFrameLayout3.visibility = View.GONE
                    binding.shimmerFrameLayout4.visibility = View.GONE

                    binding.itemsRecView.visibility = View.VISIBLE
                    binding.subcategoryRecView.visibility = View.VISIBLE
                    products = it
                    binding.itemsRecView.adapter =
                        ItemCategoryAdapter(products ?: emptyList(), this)
                }

            }
        } else {
            binding.networkCatView.visibility = View.VISIBLE
            binding.categoryLayout.visibility = View.GONE
        }

    }

    fun getMainCategory(position: Int): Long {
        var main: Long
        when (position) {
            0 -> main = 272069066799
            1 -> main = 272069099567//right kids
            2 -> main = 272069034031//right men
            3 -> main = 272069132335//right on sale
            //women 272069066799
            //kids 272069099567
            // men 272069034031
            //onsale 272069132335
            else -> main = 0
        }
        return main
    }

    private fun getSubCategoryItems(position: Int): List<Product> {
        var subCatList: List<Product> = when (position) {
            0 -> products?.filter { it.productType == "SHOES" }.orEmpty()
            1 -> products?.filter { it.productType == "ACCESSORIES" }.orEmpty()
            2 -> products?.filter { it.productType == "T-SHIRTS" }.orEmpty()
            else -> products?.filter { it.productType == "SHOES" }.orEmpty()
        }
        return subCatList
    }

    override fun itemOnClick(itemId: Long) {
        if (NetworkChange.isOnline) {
            val action = NavGraphDirections.actionGlobalProuductDetailsFragment(itemId)
            findNavController().navigate(action)
        } else {
            Toast.makeText(
                requireContext(),
                requireContext().resources.getString(R.string.no_internet_connection),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun changeToolbar() {
        requireActivity().findViewById<SearchView>(R.id.mainSearchView).visibility = View.GONE
        requireActivity().findViewById<View>(R.id.nav_view).visibility = View.VISIBLE
        requireActivity().toolbar.visibility = View.VISIBLE
        requireActivity().findViewById<View>(R.id.favourite).favouriteButton.setColorFilter(
            getResources().getColor(R.color.black)
        )
        requireActivity().findViewById<View>(R.id.cartView).cartButton.setColorFilter(
            getResources().getColor(
                R.color.black
            )
        )
        requireActivity().settingIcon.setColorFilter(getResources().getColor(R.color.black))
        requireActivity().findViewById<View>(R.id.searchIcon).visibility = View.VISIBLE
        requireActivity().findViewById<View>(R.id.settingIcon).visibility = View.INVISIBLE
        requireActivity().findViewById<View>(R.id.favourite).visibility = View.VISIBLE
        requireActivity().findViewById<View>(R.id.cartView).visibility = View.VISIBLE
        requireActivity().toolbar_title.setTextColor(Color.BLACK)
        requireActivity().toolbar.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        requireActivity().toolbar.setNavigationIcon(null)
        requireActivity().toolbar_title.text = "Category"


    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}
