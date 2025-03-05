package com.example.shop.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.adapter.CartAdapter
import com.example.shop.helper.ChangeNumberItemsListener
import com.example.shop.databinding.ActivityCartBinding
import com.example.shop.helper.ManagmentCart

class CartActivity : BaseActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var managmentCart: ManagmentCart
    private var tax: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managmentCart = ManagmentCart(this)

        binding.paymentBtn.setOnClickListener{
            startActivity(Intent(this, SelectPaymentActivity::class.java))
        }

        setVariables()
        initCartList()
        calculateCart()
    }

    private fun initCartList() {
        val listCart = managmentCart.getListCart()
        if (listCart.isEmpty()) {
            binding.tvEmptyCart.visibility = View.VISIBLE
            binding.cartView.visibility = View.GONE
            binding.salesLayout.visibility = View.GONE
            binding.priceLayout.visibility = View.GONE
            binding.paymentBtn.visibility = View.GONE
        } else {
            binding.tvEmptyCart.visibility = View.GONE
            binding.scrollView3.visibility = View.VISIBLE

            binding.cartView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.cartView.adapter = CartAdapter(listCart, this, object : ChangeNumberItemsListener {
                override fun onChanged() {
                    calculateCart()
                    updateCartVisibility() // Cập nhật hiển thị giỏ hàng
                }
            })
        }
    }

    private fun updateCartVisibility() {
        val listCart = managmentCart.getListCart()
        if (listCart.isEmpty()) {
            binding.tvEmptyCart.visibility = View.VISIBLE
            binding.cartView.visibility = View.GONE
            binding.salesLayout.visibility = View.GONE
            binding.priceLayout.visibility = View.GONE
            binding.paymentBtn.visibility = View.GONE
        } else {
            binding.tvEmptyCart.visibility = View.GONE
            binding.scrollView3.visibility = View.VISIBLE
        }
    }

    private fun calculateCart() {
        val percentTax = 0.02
        val delivery = 10
        tax = Math.round(managmentCart.getTotalFee() * percentTax).toDouble()
        val total = Math.round(managmentCart.getTotalFee() + tax + delivery).toInt()
        val itemTotal = Math.round(managmentCart.getTotalFee()).toInt()

        with(binding) {
            totalFeeTxt.text = "$$itemTotal"
            taxTxt.text = "$$tax"
            deliveryTxt.text = "$$delivery"
            totalTxt.text = "$$total"
        }
    }

    private fun setVariables() {
        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}
