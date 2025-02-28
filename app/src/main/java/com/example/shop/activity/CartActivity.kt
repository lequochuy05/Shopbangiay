package com.example.shop.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.adapter.CartAdapter
import com.example.shop.helper.ChangeNumberItemsListener
import com.example.shop.databinding.ActivityCartBinding
import com.example.shop.helper.ManagmentCart

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var managmentCart: ManagmentCart
    private var tax:Double=0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managmentCart=ManagmentCart(this)

        setVariables()

        initCartList()
        calculateCart()
    }

    private fun initCartList() {
        binding.cartView.layoutManager=LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.cartView.adapter= CartAdapter(managmentCart.getListCart(),this,object :ChangeNumberItemsListener{
            override fun onChanged() {
                calculateCart()
            }
        })
    }

    private fun calculateCart() {
        val percentTax=0.02
        val delivery=10
        tax=Math.round(managmentCart.getTotalFee()*percentTax).toDouble()
        val total=Math.round(managmentCart.getTotalFee()+tax+delivery).toInt()
        val itemTotal=Math.round(managmentCart.getTotalFee()).toInt()

        with(binding){
            totalFeeTxt.text="$itemTotal"
            taxTxt.text="$tax"
            deliveryTxt.text="$delivery"
            totalTxt.text="$total"
        }
    }

    private fun setVariables() {
        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}