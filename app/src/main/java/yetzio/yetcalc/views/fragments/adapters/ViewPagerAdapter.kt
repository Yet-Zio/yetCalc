package yetzio.yetcalc.views.fragments.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fa: FragmentActivity): FragmentStateAdapter(fa){
    var mFragList = ArrayList<Fragment>()

    override fun getItemCount(): Int {
        return mFragList.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragList[position]
    }

    fun addFragment(fragment: Fragment){
        mFragList.add(fragment)
    }

}