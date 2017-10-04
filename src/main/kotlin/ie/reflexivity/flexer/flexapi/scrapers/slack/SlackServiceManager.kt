package ie.reflexivity.flexer.flexapi.scrapers.slack

import com.google.gson.GsonBuilder
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.Logger
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object SlackServiceManager {

    private val SLACK_ENDPOINT = "https://slack.com/api/"
    private val NETWORK_TIMEOUT_SECONDS = 60L
    private val GSON = GsonBuilder().create()
    private val loggingInterceptor:HttpLoggingInterceptor  = getLoggingInterceptor()

    /**
     * A retrofit instance
     */
    var retrofit = Retrofit.Builder()
            .baseUrl(SLACK_ENDPOINT)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GSON))
            .client(getOkHttpClient(NETWORK_TIMEOUT_SECONDS, loggingInterceptor))
            .build()

    private lateinit var slackServiceInstance: SlackService

    /**
     * A slack service instance
     */
    fun getInstance(): SlackService {

        slackServiceInstance = retrofit.create(SlackService::class.java)

        return slackServiceInstance
    }

    /**
     * A http client*.
     *
     * @param networkTimeoutSecond the network timeout interval.
     * @param logger the logger instance.
     */
    fun getOkHttpClient(networkTimeoutSecond: Long, logger: HttpLoggingInterceptor): OkHttpClient {

        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        okHttpClientBuilder.readTimeout(networkTimeoutSecond, TimeUnit.SECONDS)
        okHttpClientBuilder.connectTimeout(networkTimeoutSecond, TimeUnit.SECONDS)

//        if (BuildConfig.DEBUG) {
//
//            logger.level = HttpLoggingInterceptor.Level.BODY
//            okHttpClientBuilder.addInterceptor(logger)
//        }

        return okHttpClientBuilder.build()

    }

    /**
     *  A logging interceptor
     */
    fun getLoggingInterceptor():HttpLoggingInterceptor {
        val loggingInterceptor  = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return loggingInterceptor
    }
}