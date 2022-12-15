package com.imagefiltter.dependencyInjection

import com.imagefiltter.repositories.EditImageRepository
import com.imagefiltter.viewmodels.EditImageViewModel
import com.imagefiltter.viewmodels.SavedImageViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelModule = module {
    viewModel { EditImageViewModel(editImageRepository = get()) }
    viewModel { SavedImageViewModel(savedImageRepository = get()) }
}