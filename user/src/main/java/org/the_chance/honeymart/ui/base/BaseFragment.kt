package org.the_chance.honeymart.ui.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.imageview.ShapeableImageView
import org.the_chance.honeymart.util.addOnScrollListenerWithAppbarColor
import org.the_chance.honeymart.util.addToolbarScrollListener
import org.the_chance.user.R


@Suppress("DEPRECATION")
abstract class BaseFragment<VB : ViewDataBinding> : Fragment() {

    abstract val TAG: String
    abstract val layoutIdFragment: Int
    abstract val viewModel: ViewModel
    private lateinit var _binding: VB
    private lateinit var appbar: AppBarLayout
    private lateinit var imageLogoDefault: ShapeableImageView
    private lateinit var imageLogoScrolled: ShapeableImageView
    protected val binding get() = _binding

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = DataBindingUtil.inflate(
            inflater,
            layoutIdFragment,
            container, false
        )

        _binding.apply {
            lifecycleOwner = viewLifecycleOwner
            setVariable(BR.viewModel, viewModel)
            return root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()

    }

    protected fun hideAppBarAndBottomNavigation(
        hideAppBar: Boolean,
        hideBottomNavigation: Boolean,
        changeStatusBarColor: Boolean
    ) {
        val window: Window = requireActivity().window

        if (changeStatusBarColor) {
            window.statusBarColor = ContextCompat.getColor(
                requireContext(),
                org.the_chance.design_system.R.color.primary_100
            )
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        if (hideBottomNavigation) {
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                ?.let { navigateIcon ->
                    navigateIcon.visibility = View.GONE
                }
        }else{
             requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                ?.let { navigateIcon ->
                    navigateIcon.visibility = View.VISIBLE
                }
        }

        if (hideAppBar) {
            requireActivity().findViewById<AppBarLayout>(R.id.appBarLayout)?.let { toolbar ->
                toolbar.visibility = View.GONE
            }
        }else{
             requireActivity().findViewById<AppBarLayout>(R.id.appBarLayout)?.let { toolbar ->
                toolbar.visibility = View.VISIBLE
            }
        }
    }


    protected fun setupMainFlowWindowVisibility() {
        val window: Window = requireActivity().window
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            ?.let { navigateIcon ->
                navigateIcon.visibility = View.VISIBLE
            }
        requireActivity().findViewById<AppBarLayout>(R.id.appBarLayout)?.let { toolbar ->
            toolbar.visibility = View.VISIBLE
        }
    }

    protected fun setupScrollListenerForRecyclerView(
        recyclerView: RecyclerView,
    ) {
        appbar = requireActivity().findViewById(R.id.appBarLayout)
        imageLogoDefault = requireActivity().findViewById(R.id.image_logo)
        imageLogoScrolled = requireActivity().findViewById(R.id.image_logo_scroll)
        recyclerView.addOnScrollListenerWithAppbarColor(requireContext(), this, appbar)
        recyclerView.addToolbarScrollListener(imageLogoDefault, imageLogoScrolled)
    }

    protected open fun setup() {}

    protected fun log(value: Any) {
        Log.e(TAG, value.toString())
    }
}


