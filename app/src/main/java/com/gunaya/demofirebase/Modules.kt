package com.gunaya.demofirebase

import org.koin.dsl.module

val appModules = module {
    single<NetworkStateRepository> { NetworkStateRepositoryImpl(context = get()) }
}
