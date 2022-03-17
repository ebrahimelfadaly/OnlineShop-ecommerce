package com.example.onlineshop.ui.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.onlineshop.NavGraphDirections
import com.example.onlineshop.R
import com.example.onlineshop.ViewModelFactory
import com.example.onlineshop.data.entity.allproducts.allProduct
import com.example.onlineshop.data.remoteDataSource.RemoteDataSourceImpl
import com.example.onlineshop.data.roomData.RoomDataSourceImpl
import com.example.onlineshop.data.roomData.RoomService
import com.example.onlineshop.databinding.FragmentSearchBinding
import com.example.onlineshop.repository.RepositoryImpl
import com.example.onlineshop.ui.ShopTap.ShopViewModel
import com.example.onlineshop.ui.category.ItemRecyclerClick
import kotlinx.android.synthetic.main.activity_main.*


class SearchFragment : Fragment(),ItemRecyclerClick {
  lateinit var _binding: FragmentSearchBinding
    lateinit var  shopTabViewModel : ShopViewModel
    lateinit var products:List<allProduct>
    lateinit var sortedProducts:List<allProduct>
    var queryText=""
    var productFilter=""
    private lateinit var menu: Menu

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root
        changeToolbar()

        val application = requireNotNull(this.activity).application
        val repository = RepositoryImpl(
            RemoteDataSourceImpl(),
            RoomDataSourceImpl(RoomService.getInstance(application))
        )
        val viewModelFactory = ViewModelFactory(repository, application)
        shopTabViewModel =
            ViewModelProvider(
                this, viewModelFactory
            )[ShopViewModel::class.java]
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainSearchView=  requireActivity().findViewById<SearchView>(R.id.mainSearchView)

        mainSearchView?.isFocusable = true
        mainSearchView?.isIconified = false

        shopTabViewModel.fetchallProductsList().observe(viewLifecycleOwner,{
            products = it.products
            sortedProducts = products
            binding.itemsRecView.adapter=SearchCategoryItemAdapter(it.products,requireContext(),this)

        })
        binding.sortSpinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent!!.getItemAtPosition(position).equals("Alphabetically")){
                    sortedProducts = products.sortedBy { it.title }
                    // binding.itemsRecView.adapter=SearchCategoryItemAdapter(filteredProducts,requireContext())
                }else if (parent!!.getItemAtPosition(position).equals("price: Low to High")){
                    sortedProducts = products.sortedBy { it.variants!!.get(0).price}
                    //binding.itemsRecView.adapter=SearchCategoryItemAdapter(filteredProducts,requireContext())
                }else if(parent!!.getItemAtPosition(position).equals("price: High to Low")){
                    sortedProducts = products.sortedByDescending { it.variants!!.get(0).price }
                    //binding.itemsRecView.adapter=SearchCategoryItemAdapter(filteredProducts,requireContext())
                }else if (parent!!.getItemAtPosition(position).equals("none")) {
                    binding.itemsRecView.adapter = SearchCategoryItemAdapter(products, requireContext(),this@SearchFragment)
                }
                if (!parent!!.getItemAtPosition(position).equals("SORT")) {
                    showData()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("Nottttttt","NOTHING")
            }

        }

        binding.filterSpinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent!!.getItemAtPosition(position).equals("shoes")){
                    productFilter="shoes"
                }
                else if (parent!!.getItemAtPosition(position).equals("t-shirts")){
                    productFilter="t-shirts"
                }
                else if (parent!!.getItemAtPosition(position).equals("accessories")){
                    productFilter="accessories"
                }
                else if (parent!!.getItemAtPosition(position).equals("none")){
                    productFilter=""
                }
                if (!parent!!.getItemAtPosition(position).equals("FILTER")) {
                    showData()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        mainSearchView?.setOnQueryTextListener(object :SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                queryText=query?:""
                showData()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                queryText=newText?:""
                showData()
                return true
            }

        })

    }
    private fun showData(){
        var filteredProducts = sortedProducts.filter { it.title!!.contains(queryText?:"none",true)&& it.productType!!.contains(productFilter,true)}
        if(filteredProducts.size!=0) {
            binding.placeHolder.visibility=View.GONE
            binding.itemsRecView.adapter = SearchCategoryItemAdapter(
                filteredProducts,
                requireContext(),
                this@SearchFragment
            )
        }
        else{
            binding.itemsRecView.adapter = SearchCategoryItemAdapter(
                filteredProducts,
                requireContext(),
                this@SearchFragment
            )
            binding.placeHolder.visibility=View.VISIBLE

        }
    }

    private fun changeToolbar() {
        requireActivity().findViewById<SearchView>(R.id.mainSearchView).visibility=View.VISIBLE
        requireActivity().findViewById<View>(R.id.nav_view).visibility = View.GONE
        requireActivity().toolbar.visibility = View.VISIBLE

        requireActivity().toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.black_arrow))
        requireActivity().toolbar.setNavigationOnClickListener {
            view?.findNavController()?.popBackStack()
        }
        //requireActivity().bottom_nav.visibility = View.VISIBLE
        requireActivity().toolbar_title.text = ""
        requireActivity().findViewById<View>(R.id.searchIcon).visibility = View.INVISIBLE
        requireActivity().findViewById<View>(R.id.favourite).visibility = View.INVISIBLE
        requireActivity().findViewById<View>(R.id.cartView).visibility = View.INVISIBLE
    }

    override fun itemOnClick(itemId: Long) {
        val action = NavGraphDirections.actionGlobalProuductDetailsFragment(itemId)
        findNavController().navigate(action)
    }

}