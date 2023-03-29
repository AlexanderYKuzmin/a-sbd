package com.example.a_sbd.di

import com.example.a_sbd.data.repository.ASBDoRepositoryImpl
import com.example.a_sbd.domain.ASBDRepository
import dagger.Binds
import dagger.Module

@Module
interface DataModule {

    @Binds
    fun bindRepository(repositoryImpl: ASBDoRepositoryImpl): ASBDRepository
}