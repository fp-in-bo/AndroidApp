package com.fpinbo.app.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fpinbo.app.BuildConfig
import com.fpinbo.app.R
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element


class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = AboutPage(requireContext())
        .setDescription(getString(R.string.app_name))
        .setImage(R.mipmap.ic_launcher)
        .addEmail("fpinbo@gmail.com")
        .addWebsite("https://fpinbo.dev/")
        .addGitHub("fp-in-bo")
        .addFacebook("fpinbo")
        .addTwitter("fpinbo")
        .addYoutube("UCjJOUE8yNp7aeaBpgO5zTfw")
        .addSlack()
        .addOpenSource()
        .addVersion()
        .create()

    private fun AboutPage.addSlack(): AboutPage = addItem(
        Element().apply {
            title = getString(R.string.slack)
            intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://bit.ly/32VyE3w"))
            iconDrawable = R.drawable.ic_baseline_chat_24
        }
    )

    private fun AboutPage.addOpenSource(): AboutPage = addItem(
        Element().apply {
            title = getString(R.string.open_source_notices)
            intent = Intent(requireContext(), OssLicensesMenuActivity::class.java)
            iconDrawable = R.drawable.ic_baseline_developer_mode_24
        }
    )

    private fun AboutPage.addVersion(): AboutPage = addItem(
        Element().apply {
            title = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
        }
    )

}