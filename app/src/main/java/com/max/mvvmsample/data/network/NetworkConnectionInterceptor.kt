package com.max.mvvmsample.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.max.mvvmsample.R
import com.max.mvvmsample.utils.NoInternetException
import okhttp3.Interceptor
import okhttp3.Response

//Check network connection
class NetworkConnectionInterceptor(
    context: Context
) : Interceptor {

    private val appContext = context.applicationContext

    /**
     * @throws NoInternetException If there is no Internet.
     */
    override fun intercept(chain: Interceptor.Chain): Response = when {

        //TODO Set No Internet Message
        !isInternetAvailable() -> throw NoInternetException("No Internet")

        else -> chain.proceed(chain.request())
    }

    private fun isInternetAvailable(): Boolean {

        val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        connectivityManager?.run {

            when {

                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {

                    getNetworkCapabilities(connectivityManager.activeNetwork)?.run {

                        return when {

                            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                            else -> false
                        }
                    }
                }

                else -> {
                    @Suppress("DEPRECATION")
                    activeNetworkInfo?.run { return isConnected }
                }
            }
        }

        return false
    }

}
