package com.cmdv.feature.ui.fragments.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.cmdv.feature.R
import com.cmdv.feature.ui.adapters.SectionsPagerAdapter
import kotlinx.android.synthetic.main.fragment_main_home.*


class MainHomeFragment : Fragment() {

    private var nextDrawableId = R.drawable.ic_create_shop_cart_vector

    private var rotation = 180

    private var listener: MainHomeFragmentListener? = null

    companion object {
        @JvmStatic
        fun newInstance() =
            MainHomeFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_main_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sectionsPagerAdapter = SectionsPagerAdapter(requireContext(), activity!!.supportFragmentManager)
        viewPagerHome.adapter = sectionsPagerAdapter
        tabsHome.setupWithViewPager(viewPagerHome)
        viewPagerHome.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                //Rise
                fab.animate()
                    .rotationBy(rotation.toFloat())
                    .setDuration(100)
                    .scaleX(1.1f) //Scaling to 110%
                    .scaleY(1.1f)
                    .withEndAction {
                        fab.setImageResource(nextDrawableId)

                        //Shrink Animation
                        fab.animate()
                            .rotationBy(rotation.toFloat())
                            .setDuration(100)
                            .scaleX(1F) //Scaling back to what it was
                            .scaleY(1F)
                            .start()

                        nextDrawableId =
                            if (nextDrawableId == R.drawable.ic_create_product_vector) R.drawable.ic_create_shop_cart_vector else R.drawable.ic_create_product_vector
                        rotation = -rotation
                    }
                    .start()
            }

        })

        fab.setOnClickListener {
            when (viewPagerHome.currentItem) {
                0 -> listener?.onCreateProductClick()
                1 -> listener?.onCreateShopCartClick()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainHomeFragmentListener)
            this.listener = context
        else
            throw IllegalAccessException("Calling Activity must implement ${MainHomeFragmentListener::class.java.simpleName}.")
    }

    fun goToShopCartTab() {
        viewPagerHome.currentItem = 1
    }
}