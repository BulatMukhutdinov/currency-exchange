package tat.mukhutdinov.currencyexchange

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

// Used in app/build.gradle.kts
class AndroidTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, TestApp::class.java.name, context)
    }
}