package kr.co.jsh.bedischarge

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kr.co.jsh.bedischarge.databinding.FragmentInitBinding

class FragmentInit : Fragment() {

    lateinit var binding: FragmentInitBinding
    private var mInterstitialAd: InterstitialAd? = null
    lateinit var mainactivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInitBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainactivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadInterstitialAd()

        binding.btnAdd.setOnClickListener {

            if (list.size >= 6) {
                Toast.makeText(context, "최대 5개 입니다.", Toast.LENGTH_LONG).show()
            }
            else {
                showInterstitialAd()
                val intent = Intent(context, DataInputActivity::class.java)
                intent.putExtra("listSize", list.size)
                startActivity(intent)
            }
        }

    }

    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(mainactivity, "ca-app-pub-3658561022574146/8060454620", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                mInterstitialAd = null

            }

            override fun onAdLoaded(p0: InterstitialAd) {
                mInterstitialAd = p0
            }
        })
    }

    private fun showInterstitialAd() {
        if (mInterstitialAd != null) {


            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    mInterstitialAd = null
                    loadInterstitialAd()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    mInterstitialAd = null
                }

                override fun onAdShowedFullScreenContent() {

                }
            }
            mInterstitialAd?.show(mainactivity)
        }
    }
}
