package ru.iliavolkov.weather.model

class RepositoryImpl: Repository {
    override fun getWeatherFromServer(): Weather {
        return Weather()
    }

    override fun getWeatherFromLocalStorage(): Weather {
        return Weather()
    }

    override fun getWeatherFromLocalStorageRus(): List<Weather> {
        TODO("Not yet implemented")
    }

    override fun getWeatherFromLocalStorageWorld(): List<Weather> {
        TODO("Not yet implemented")
    }
}