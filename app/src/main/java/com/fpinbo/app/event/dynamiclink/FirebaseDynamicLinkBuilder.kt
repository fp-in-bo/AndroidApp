package com.fpinbo.app.event.dynamiclink

import android.net.Uri
import arrow.core.left
import arrow.core.right
import arrow.fx.IO
import com.fpinbo.app.entities.Event
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.dynamiclinks.ktx.socialMetaTagParameters
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class FirebaseDynamicLinkBuilder @Inject constructor() :
    DynamicLinkBuilder {

    override fun build(event: Event): IO<String> = IO.async { callback ->
        Firebase.dynamicLinks.shortLinkAsync {
            link = Uri.parse(event.shareUrl)
            domainUriPrefix = "https://fpinbo.page.link"
            androidParameters { }
            socialMetaTagParameters {
                title = event.title
                description = event.description
                imageUrl = Uri.parse(event.imageUrl)
            }
        }.addOnSuccessListener {
            callback(it.shortLink!!.toString().right())
        }.addOnFailureListener {
            callback(it.left())
        }

    }
}