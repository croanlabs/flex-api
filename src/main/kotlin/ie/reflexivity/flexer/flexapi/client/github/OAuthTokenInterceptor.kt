package ie.reflexivity.flexer.flexapi.client.github

import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response


class OAuthTokenInterceptor(
        val token: String
) : Interceptor {

    override fun intercept(chain: Chain): Response {
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder().header("Authorization",
                "token ${token}")
        val newRequest = builder.build()
        return chain.proceed(newRequest)
    }
}
