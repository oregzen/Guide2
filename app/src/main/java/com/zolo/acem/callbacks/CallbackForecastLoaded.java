package com.zolo.acem.callbacks;

import com.zolo.acem.weather.cmn.ForecastDay;

import java.util.List;

public interface CallbackForecastLoaded {

    void doneLoadingForecast(List<ForecastDay> result);
    void showLoadingWeather();
}
