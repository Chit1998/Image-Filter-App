package com.imagefiltter.dependencyInjection

import com.imagefiltter.repositories.EditImageRepository
import com.imagefiltter.repositories.EditImageRepositoryImpl
import com.imagefiltter.repositories.SavedImageRepository
import com.imagefiltter.repositories.SavedImageRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val RepositoryModule = module {
    factory<EditImageRepository> {
        EditImageRepositoryImpl(androidContext())
    }

    factory<SavedImageRepository> {
        SavedImageRepositoryImpl(androidContext())
    }

}