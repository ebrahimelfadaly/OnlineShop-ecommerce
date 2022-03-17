package com.example.onlineshop.ui.cart

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import  androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.onlineshop.NavGraphDirections
import com.example.onlineshop.R
import com.example.onlineshop.ViewModelFactory
import com.example.onlineshop.data.entity.ads_discount_codes.AllCodes
import com.example.onlineshop.data.entity.customer.Addresse
import com.example.onlineshop.data.entity.discount.DiscountCode
import com.example.onlineshop.data.entity.order.*
import com.example.onlineshop.data.entity.orderGet.OneOrderResponce
import com.example.onlineshop.data.entity.priceRules.PriceRule
import com.example.onlineshop.data.entity.priceRules.priceRules
import com.example.onlineshop.data.remoteDataSource.RemoteDataSourceImpl
import com.example.onlineshop.data.roomData.RoomDataSourceImpl
import com.example.onlineshop.data.roomData.RoomService
import com.example.onlineshop.data.sharedprefrences.MeDataSharedPrefrenceReposatory
import com.example.onlineshop.databinding.FragmentOrderConfirmationBinding
import com.example.onlineshop.networkBase.NetworkChange
import com.example.onlineshop.repository.RepositoryImpl
import com.example.onlineshop.ui.Payment.CheckoutActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_order_confirmation.*
import timber.log.Timber
import java.io.Serializable

class OrderConfirmationFragment :  Fragment() {
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var meDataSourceReo: MeDataSharedPrefrenceReposatory
    private lateinit var binding: FragmentOrderConfirmationBinding
    private lateinit var orderItemsAdapter: OrderItemsAdapter

    private var totalPrice = 0.0f
    private var totalDiscont = 0.0f
    private var discountAmount = 0.0f
    private var discountCode = ""
    private var customerID = ""
    private var paymentMethod = ""
    private var isDefaultAddress = false
    private var isDiscount = false
    private var isCash = false
    private var priceRulesList: List<PriceRule> = arrayListOf()
    private var discountCodesList: List<priceRules> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        meDataSourceReo = MeDataSharedPrefrenceReposatory(requireActivity())
        binding = FragmentOrderConfirmationBinding.inflate(layoutInflater)
        val args: OrderConfirmationFragmentArgs by navArgs()
        totalPrice = args.totalPrice
        val application = requireNotNull(this.activity).application
        val repository = RepositoryImpl(
            RemoteDataSourceImpl(),
            RoomDataSourceImpl(RoomService.getInstance(application))
        )
        val viewModelFactory = ViewModelFactory(repository, application)
        orderViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(OrderViewModel::class.java)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeToolbar()

        if (isLoged()) {
            customerID = meDataSourceReo.loadUsertId()

            if (NetworkChange.isOnline) {
                orderViewModel.getCustomersAddressList(customerID)
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.thereIsNoNetwork),
                    Toast.LENGTH_SHORT
                ).show()

            }

            //  orderViewModel.getPriceRulesList()
            // orderViewModel.fetchallDiscountCodeList()
        }

        orderItemsAdapter = OrderItemsAdapter(arrayListOf(), orderViewModel)
        binding.rvCartItems.apply {
            adapter = orderItemsAdapter
        }
        orderViewModel.getAllCartList().observe(viewLifecycleOwner, {
            orderItemsAdapter.addNewList(it)

        })

        orderViewModel.getAddressList().observe(viewLifecycleOwner, Observer<List<Addresse>?> {
            val dafultAddress: MutableList<Addresse> = arrayListOf()
            for (item in it) {
                if (item.default == true) {
                    dafultAddress.add(item)

                }
            }

            if (dafultAddress.isEmpty()) {
                binding.group.visibility = View.INVISIBLE
                binding.addAddressText.visibility = View.VISIBLE
                isDefaultAddress = false
            } else {
                isDefaultAddress = true
                binding.group.visibility = View.VISIBLE
                binding.addAddressText.visibility = View.INVISIBLE
                binding.fullNameTxt.text = dafultAddress.get(0).firstName.toString()
                binding.countryTxt.text = dafultAddress.get(0).country.toString()
                binding.addressTxt.text = dafultAddress.get(0).address1.toString()
            }

        })
        binding.addressBtn.setOnClickListener {
            val action = NavGraphDirections.actionGlobalAddressFragment()
            findNavController().navigate(action)
        }

        binding.discountBtn.setOnClickListener {
            binding.discountEdt.visibility = View.VISIBLE
            binding.discountBtn.visibility = View.INVISIBLE
            binding.discountBtnHide.visibility = View.VISIBLE
        }
        binding.discountBtnHide.setOnClickListener {
            discount_edt.visibility = View.GONE
            binding.discountBtnHide.visibility = View.INVISIBLE
            binding.discountBtn.visibility = View.VISIBLE
        }
        binding.placeOrderBtn.setOnClickListener {
            //edit on adress add setdefelult
            if (true) {
                binding.placeOrderBtn.startAnimation();

                //[do some async task. When it finishes]
                //You can choose the color and the image after the loading is finished
                var bitmap: Bitmap? =
                    BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_check_24);
                if (bitmap != null) {
                    binding.placeOrderBtn.doneLoadingAnimation(Color.WHITE, bitmap)
                }

//[or just revert de animation]
                //  binding.placeOrderBtn.revertAnimation();
                placeOrder()

            } else {
                Toast.makeText(context, "please, set your address", Toast.LENGTH_SHORT).show()
            }
        }
        orderViewModel.getPostOrder().observe(viewLifecycleOwner, Observer<OneOrderResponce?> {
            binding.placeOrderBtn.stopAnimation()
            if (it != null) {
                // clearBackStack()
                Timber.i("ordemm+" + it.order)
                Timber.i("orderrr+" + it.order)
                orderViewModel.delAllItems()
//                val action = NavGraphDirections.actionGlobalShopTabFragment2()
//                findNavController().navigate(action)
                it?.let {
                    if (isCash) {
//                        val action = NavGraphDirections.actionGlobalShopTabFragment2()
//                        findNavController().navigate(action)
                        view.findNavController()
                            .navigate(OrderConfirmationFragmentDirections.actionOrderConfirmationFragmentToShopTabFragment2())
                        //  view?.findNavController()?.popBackStack()
                    } else {
                        // Navigation.findNavController(view).navigate(R.id.Checkout_Activity)
                        startActivity(
                            Intent(requireActivity(), CheckoutActivity::class.java).putExtra(
                                "amount",
                                totalPrice.toString()
                            )

                                .putExtra("order", it.order as? Serializable)
                        )
                    }
                }

            } else {
                Toast.makeText(context, "error Try again please", Toast.LENGTH_SHORT).show()
            }
        })
//        orderViewModel.getPriceRules().observe(viewLifecycleOwner, Observer<List<PriceRule>?> {
//            priceRulesList=it
//
//        })

        orderViewModel.fetchallDiscountCodeList().observe(viewLifecycleOwner, Observer<priceRules> {
        //    discountCodesList = it.discountCodes

        })
        binding.discountEdt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Timber.i("  afterTextChanged")
                checkDiscountCode()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Timber.i(" beforeTextChanged")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Timber.i("  onTextChanged")
            }
        })

        binding.totalPriceTxt.text = totalPrice.toString() + " EGP"
        binding.totalItemTxt.text = totalPrice.toString() + " EGP"

    }

    //    private fun checkDiscountCode() {
//        val discount: List<PriceRule> =
//            priceRulesList.filter {
//                it.title == binding.discountEdt.text.toString().trim()
//            }
//        if (discount.isEmpty()) {
//            Toast.makeText(context, "Sorry, this coupon is invalid", Toast.LENGTH_SHORT).show()
//        } else {
//            binding.totalDiscountTxt.text = discount.get(0).value+"%"
//            totalDiscont= discount.get(0).value?.toFloat() ?:0.0f
//
//            discountAmount = (totalPrice*totalDiscont)/100
//            binding.totalPriceTxt.text=(totalPrice+totalDiscont).toString() + " EGP"
//
//
//        }
//    }
    private fun checkDiscountCode() {
        val discount: List<priceRules> =
            discountCodesList.filter {

                it.priceRules?.get(2)?.title == binding.discountEdt.text.toString().trim()
            }
        var code="SUMMERSALE10OFF"
        //SUMMERSALE10OFF
        if (binding.discountEdt.text.toString().trim()!=code) {
            isDiscount = false
            binding.discountEdt.setError("Sorry, this coupon is invalid")
            binding.totalDiscountTxt.text = "---"
            binding.totalPriceTxt.text = (totalPrice).toString() + " EGP"
            // Toast.makeText(context, "Sorry, this coupon is invalid", Toast.LENGTH_SHORT).show()
        } else {
            isDiscount = true
            binding.totalDiscountTxt.text = "10%"
            discountAmount = ((totalPrice * 10) / 100)
            discountCode = code
            Timber.i("discountAmount" + discountAmount + "  " + totalDiscont + "  " + totalPrice)
            totalPrice = (totalPrice - discountAmount)
            binding.totalPriceTxt.text = totalPrice.toString() + " EGP"

        }
    }


    private fun placeOrder() {
        var customerOrder = CustomerOrder(customerID.toLong())
        var lineItem: MutableList<LineItem> = arrayListOf()
        var discount: MutableList<DiscountCodes>? = arrayListOf()
        val items = orderItemsAdapter.orderList.map {
            it.variants?.get(0)
        }

        for (item in items) {
            lineItem.add(LineItem(item?.inventory_quantity, item?.id))
        }
        getPaymentMethod()
        if (isDiscount)
            discount?.add(DiscountCodes(discountAmount.toString(), discountCode))
        else
            discount = null

        var order = Order(customerOrder, "pending", lineItem, paymentMethod, discount)
        var orders = Orders(order)
        if (NetworkChange.isOnline) {
            orderViewModel.createOrder(orders)
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.thereIsNoNetwork),
                Toast.LENGTH_SHORT
            ).show()

        }
    }

    private fun getPaymentMethod() {
        if (binding.radioCash.isChecked) {
            paymentMethod = "Cash"
            isCash = true
        } else if (binding.radioCredit.isChecked) {
            paymentMethod = "Card"
            isCash = false
        }

    }

    private fun isLoged(): Boolean {
        return meDataSourceReo.loadUsertstate()
    }

    private fun changeToolbar() {
        requireActivity().findViewById<SearchView>(R.id.mainSearchView).visibility=View.GONE
        requireActivity().findViewById<View>(R.id.nav_view).visibility = View.GONE
        requireActivity().toolbar.visibility = View.VISIBLE
        requireActivity().toolbar.searchIcon.visibility = View.INVISIBLE
        requireActivity().toolbar.cartView.visibility = View.INVISIBLE
        requireActivity().toolbar.settingIcon.visibility = View.INVISIBLE
        requireActivity().toolbar.favourite.visibility = View.INVISIBLE
        requireActivity().toolbar_title.setTextColor(Color.WHITE)

        requireActivity().toolbar.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        requireActivity().toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_ios_24))
        requireActivity().toolbar.setNavigationOnClickListener {
            view?.findNavController()?.popBackStack()
        }
        requireActivity().toolbar_title.text = "OrderConfirmation"
    }

}