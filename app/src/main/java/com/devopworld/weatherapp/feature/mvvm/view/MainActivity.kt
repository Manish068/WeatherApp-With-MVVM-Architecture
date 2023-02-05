package com.devopworld.weatherapp.feature.mvvm.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.devopworld.weatherapp.R
import com.devopworld.weatherapp.Util.TemperatureUtility
import com.devopworld.weatherapp.common.RequestState
import com.devopworld.weatherapp.databinding.ActivityMainBinding
import com.devopworld.weatherapp.feature.mvvm.model.CurrentWeatherResponse
import com.devopworld.weatherapp.feature.mvvm.model.DayWiseTemp
import com.devopworld.weatherapp.feature.mvvm.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var mainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel.getWeatherInfo()
    }


    override fun onStart() {
        super.onStart()

        viewModel.weatherInfo.observe(this) { state ->
            when (state) {
                //if result is succes then show weather report
                is RequestState.Success -> {
                    hideErrorScreen()
                    hideLoader()
                    showCurrentWeather(state.data.weatherResponse)
                    showFutureForecast(state.data.nextFourDayForecast)
                }
                //if result is taking time to fetch the data from network show loading progress bar
                is RequestState.Loading, is RequestState.Idle -> {
                    hideErrorScreen()
                    hideWeatherReport()
                    showLoader()
                }
                // else show error screen
                else -> {
                    hideLoader()
                    hideWeatherReport()
                    showErrorScreen()
                }
            }
        }
    }

    private fun showCurrentWeather(weatherResponse: CurrentWeatherResponse) {
        val tempWithDegree = "${
            TemperatureUtility.convertKelvinToCelsius(weatherResponse.main.temp).roundToInt()
        }\u00B0"
        mainBinding.tempTv.text = tempWithDegree
        mainBinding.cityNameTv.text = weatherResponse.name
    }

    private fun showFutureForecast(nextFourDayForecast: HashMap<Int, DayWiseTemp>) {
        slideUpAnimation()
        val adapter = ForecastRecyclerAdapter(nextFourDayForecast)
        mainBinding.futureForecastRv.layoutManager = LinearLayoutManager(this)
        mainBinding.futureForecastRv.adapter = adapter
    }

    private fun slideUpAnimation() {
        mainBinding.weatherReport.visibility = VISIBLE
        val animation =AnimationUtils.loadAnimation(this,R.anim.slide_up)
        mainBinding.forecastCv.animation = animation
    }



    private fun showLoader() {
        mainBinding.progressBarView.visibility = VISIBLE
        val rotation: Animation =
            AnimationUtils.loadAnimation(
                this,
                R.anim.rotate_loader
            )
        rotation.repeatCount = Animation.INFINITE
        mainBinding.progressBarIv.startAnimation(rotation)

    }

    private fun showErrorScreen() {
        mainBinding.errorView.visibility = VISIBLE
    }

    //hide weather report
    private fun hideWeatherReport() {
        mainBinding.weatherReport.visibility = GONE
    }

    //hide loader
    private fun hideLoader() {
        mainBinding.progressBarView.visibility = GONE
    }

    //hide error screen
    private fun hideErrorScreen() {
        mainBinding.errorView.visibility = GONE
    }

    fun onRetryClick(view: View) {
        viewModel.getWeatherInfo()
    }

}