package com.example.shop.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.R
import com.example.shop.adapter.ListItemsAdapter
import com.example.shop.databinding.ActivityListItemsBinding
import com.example.shop.viewModel.DashboardViewModel

class ListItemsActivity : BaseActivity() {
    private lateinit var binding: ActivityListItemsBinding
    private val viewModel = DashboardViewModel()
    private var id:String =""
    private var title:String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getBundle()
        initList()

    }

    private fun initList() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            viewModel.loadItems(id).observe(this@ListItemsActivity, Observer {
                listView.layoutManager=
                    LinearLayoutManager(this@ListItemsActivity, LinearLayoutManager.VERTICAL, false)
                listView.adapter = ListItemsAdapter(it)
                progressBar.visibility = View.GONE
            })
            backBtn.setOnClickListener {
                finish()
            }
        }
    }
    private fun getBundle() {
        id=intent.getStringExtra("id")!!
        title=intent.getStringExtra("title")!!

        binding.categoryTxt.text=title
    }
}