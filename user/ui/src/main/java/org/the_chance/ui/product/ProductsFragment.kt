package org.the_chance.ui.product

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.the_chance.ui.BaseFragment
import org.the_chance.ui.R
import org.the_chance.ui.databinding.FragmentProductsBinding

@AndroidEntryPoint
class ProductsFragment : BaseFragment<FragmentProductsBinding>() {
    override val TAG: String = this::class.simpleName.toString()
    override val layoutIdFragment = R.layout.fragment_products
    override val viewModel: ProductViewModel by viewModels()

    override fun setup() {
        initiateProductsAdapter()
        initiateCategoryProductAdapter()
    }
    private fun initiateCategoryProductAdapter(){
        val adapter = CategoryProductAdapter(viewModel)
        binding.recyclerCategoryProduct.adapter = adapter
    }
    private fun initiateProductsAdapter(){
        val productAdapter = ProductAdapter(viewModel)
        binding.recyclerProduct.adapter = productAdapter
    }

}