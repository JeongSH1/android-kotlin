package kr.co.jsh.bedischarge

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator
import kr.co.jsh.bedischarge.databinding.ActivityMainBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

var list = mutableListOf<Fragment>(FragmentInit())
val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm.ss")
val today = LocalDateTime.now().format(formatter)

open class MainActivity : AppCompatActivity() {

    lateinit var viewPager: ViewPager2
    lateinit var mAdView: AdView

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        checkActivateFragment()
        setViewPager()
        val adView = AdView(this)
        adView.adUnitId = "ca-app-pub-3658561022574146/5065731409"
        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    public override fun onResume() {
        super.onResume()
        checkActivateFragment()
        setViewPager()
    }

    public override fun onStart() {
        super.onStart()
        checkActivateFragment()
        setViewPager()
    }

    fun setViewPager() {

        viewPager = findViewById(R.id.viewPager)
        val springDotsIndicator = findViewById<SpringDotsIndicator>(R.id.spring_dots_indicator)
        val pagerAdapter = FragmentPagerAdapter(list, this)
        viewPager.adapter = pagerAdapter
        springDotsIndicator.attachTo(viewPager)

    }

    fun checkActivateFragment() {
        list = mutableListOf<Fragment>(FragmentInit())
        if (MyApplication.prefs.getString("SavedUserName1", "NoData") != "NoData") {
            list.add(list.size-1, Fragment1())
        }
        if (MyApplication.prefs.getString("SavedUserName2", "NoData") != "NoData") {
            list.add(list.size-1, Fragment2())
        }
        if (MyApplication.prefs.getString("SavedUserName3", "NoData") != "NoData") {
            list.add(list.size-1, Fragment3())
        }
        if (MyApplication.prefs.getString("SavedUserName4", "NoData") != "NoData") {
            list.add(list.size-1, Fragment4())
        }
        if (MyApplication.prefs.getString("SavedUserName5", "NoData") != "NoData") {
            list.add(list.size-1, Fragment5())
        }

    }
}

class FragmentPagerAdapter(val fragmentList:List<Fragment>, fragmentActivity: FragmentActivity):
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList.get(position)
    }

}