package com.example.shop.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.shop.R
import com.example.shop.adapter.PicListAdapter
import com.example.shop.adapter.SizeListAdapter
import com.example.shop.databinding.ActivityDetailBinding
import com.example.shop.helper.FavoriteManager
import com.example.shop.helper.ManagmentCart
import com.example.shop.model.ItemsModel

class DetailActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var item: ItemsModel
    private var numberOrder = 1
    private lateinit var managementCart: ManagmentCart
    private var selectedSize: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managementCart = ManagmentCart(this)

        getBundle()
        initList()
    }

    private fun initList() {
        // Thiết lập danh sách ảnh
        val colorList = ArrayList<String>()
        for (imageUrl in item.picUrl) {
            colorList.add(imageUrl)
        }
        Glide.with(this)
            .load(colorList[0])
            .into(binding.picMain)

        binding.picList.adapter = PicListAdapter(colorList, binding.picMain)
        binding.picList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Thiết lập danh sách kích cỡ (size)
        val sizeList = ArrayList<String>()
        for (size in item.size) {
            sizeList.add(size)
        }
        binding.sizeList.adapter = SizeListAdapter(sizeList) { size ->
            selectedSize = size
        }
        binding.sizeList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun getBundle() {
        item = (intent.getSerializableExtra("object") as? ItemsModel?)!!
        binding.titleTxt.text = item.title
        binding.description.text = item.description
        binding.priceTxt.text = "$ ${item.price}"
        binding.ratingTxt.text = "${item.rating}"

        binding.addToCartBtn.setOnClickListener {
            item.numberInCart = numberOrder
            item.selectedSize = selectedSize
            managementCart.insertItems(item)
        }

        binding.favBtn.setOnClickListener {
            // Kiểm tra xem người dùng đã chọn size hay chưa
            if (selectedSize.isEmpty()) {
                Toast.makeText(this, "Please select a size", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Đảm bảo sản phẩm đã có kích cỡ được chọn
            item.selectedSize = selectedSize

            if (!FavoriteManager.isFavorite(item)) {

                FavoriteManager.addFavorite(item)
                binding.favBtn.setImageResource(R.drawable.fav_icon)
                binding.favBtn.setColorFilter(R.color.red)
                Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show()
            } else {
                // Nếu sản phẩm đã có, loại bỏ khỏi favorites và cập nhật icon
                FavoriteManager.removeFavorite(item)
                binding.favBtn.setImageResource(R.drawable.fav_icon)
                Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show()
            }
        }


        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.cartBtn.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }
}
