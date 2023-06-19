package org.the_chance.honeymart.ui.feature.cart

import androidx.fragment.app.viewModels
import org.the_chance.honeymart.ui.base.BaseFragment
import org.the_chance.user.R
import org.the_chance.user.databinding.FragmentCartBinding

class CartFragment : BaseFragment<FragmentCartBinding>() {
    override val TAG: String = this::class.java.simpleName
    override val layoutIdFragment: Int = R.layout.fragment_cart
    override val viewModel: CartViewModel by viewModels()
    private val cartAdapter: CartAdapter by lazy { CartAdapter(viewModel) }

    override fun setup() {
        initAdapters()
    }

    private fun initAdapters() {
        binding.recyclerCartList.adapter = cartAdapter
    }


}