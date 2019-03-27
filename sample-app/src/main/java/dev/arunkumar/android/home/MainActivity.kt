package dev.arunkumar.android.home

import android.content.SharedPreferences
import android.os.Bundle
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.DaggerAppCompatActivity
import dev.arunkumar.android.R
import dev.arunkumar.android.dagger.activity.PerActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var preferences: SharedPreferences
    @Inject
    lateinit var testActivityScope: TestActivityScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    @Module
    abstract class Builder {
        @PerActivity
        @ContributesAndroidInjector
        abstract fun mainActivity(): MainActivity
    }


    @PerActivity
    class TestActivityScope @Inject constructor()
}
