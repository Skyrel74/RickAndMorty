package com.skyrel74.ricknmorty.di.components

import com.skyrel74.ricknmorty.di.Application
import com.skyrel74.ricknmorty.di.modules.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Component(modules = [
    AndroidInjectionModule::class,
    ActivitiesModule::class,
    FragmentsModule::class,
    ApplicationModule::class,
    NetworkModule::class,
    DatabaseModule::class
])
@Singleton
interface ApplicationComponent : AndroidInjector<Application> {

    override fun inject(instance: Application?)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun bindContext(application: Application): Builder

        fun build(): ApplicationComponent
    }
}