package com.example.creatorsociety

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class CreatorToolkitFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_creator_toolkit, container, false)

        // Sell buttons
        view.findViewById<Button>(R.id.btnEtsy).setOnClickListener {
            openUrl("https://www.etsy.com/sell")
        }
        view.findViewById<Button>(R.id.btnRedbubble).setOnClickListener {
            openUrl("https://www.artpal.com/sellart/")
        }
        view.findViewById<Button>(R.id.btnKDP).setOnClickListener {
            openUrl("https://kdp.amazon.com/en_US/")
        }
        view.findViewById<Button>(R.id.btnNotionPress).setOnClickListener {
            openUrl("https://notionpress.com/")
        }
        // "Learn More" links
        view.findViewById<TextView>(R.id.pricingGuideLearnMore).setOnClickListener {
            openUrl("https://www.dharamkotstudio.com/unlocking-the-art-of-pricing-a-guide-for-artists/")
        }
        view.findViewById<TextView>(R.id.copyrightLearnMore).setOnClickListener {
            openUrl("https://copyright.gov.in/documents/handbook.html")
        }
        //inspo
        view.findViewById<TextView>(R.id.Canvamood).setOnClickListener {
            openUrl("https://www.canva.com/create/mood-boards/")
        }
        view.findViewById<TextView>(R.id.pinterest).setOnClickListener {
            openUrl("https://in.pinterest.com/")
        }

        return view
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}
