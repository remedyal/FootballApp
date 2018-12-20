package com.remedyal.footballapp.network

import com.remedyal.footballapp.BuildConfig
import org.junit.Test
import org.mockito.Mockito

class ApiRepositoryTest {

    @Test
    fun testDoRequest() {
        val apiRepository = Mockito.mock(ApiRepository::class.java)

        val url = BuildConfig.BASE_URL + "api/v1/json/${BuildConfig.TSDB_API_KEY}" +
                "/eventspastleague.php?id=" + "4328"

        apiRepository.doRequest(url)
        Mockito.verify(apiRepository).doRequest(url)
    }
}