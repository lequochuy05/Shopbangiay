package com.example.shop.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.shop.model.CategoryModel
import com.example.shop.repository.MainRepository


class MainViewModel : ViewModel() {
    private val repository = MainRepository()

    val category: LiveData<MutableList<CategoryModel>> = repository.loadCategory()

}