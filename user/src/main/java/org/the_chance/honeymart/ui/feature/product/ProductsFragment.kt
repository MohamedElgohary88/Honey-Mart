package org.the_chance.honeymart.ui.feature.product

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.the_chance.honeymart.ui.base.BaseFragment
import org.the_chance.user.R
import org.the_chance.user.databinding.FragmentProductsBinding

@AndroidEntryPoint
class ProductsFragment : BaseFragment<FragmentProductsBinding>() {
    override val TAG: String = this::class.simpleName.toString()
    override val layoutIdFragment = R.layout.fragment_products
    override val viewModel: ProductViewModel by viewModels()

    override fun setup() {
        initiateProductsAdapter()
        initiateCategoryAdapter()
    }

    private fun initiateCategoryAdapter() {
        val adapter = CategoryProductAdapter(viewModel)
        binding.recyclerCategory.adapter = adapter
    }

    private fun initiateProductsAdapter() {
        val productAdapter = ProductAdapter(viewModel)
        binding.recyclerProduct.adapter = productAdapter
    }

}