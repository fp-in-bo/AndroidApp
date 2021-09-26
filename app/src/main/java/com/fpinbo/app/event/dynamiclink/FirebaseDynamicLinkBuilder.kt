package com.fpinbo.app.event.dynamiclink

import android.net.Uri
import arrow.core.Either
import com.fpinbo.app.entities.Event
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.dynamiclinks.ktx.socialMetaTagParameters
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseDynamicLinkBuilder @Inject constructor() :
    DynamicLinkBuilder {

    override suspend fun build(event: Event): Either<Throwable, String> {
        return Either.catch {
            Firebase.dynamicLinks.shortLinkAsync {
                link = Uri.parse(event.shareUrl)
                domainUriPrefix = "https://fpinbo.page.link"
                androidParameters { }
                socialMetaTagParameters {
                    title = event.title
                    description = event.description
                    imageUrl = Uri.parse(event.imageUrl)
                }
            }.await()
        }.map {
            it.shortLink!!.toString()
        }
    }
}
