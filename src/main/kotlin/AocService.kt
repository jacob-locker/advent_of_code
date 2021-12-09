import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import java.lang.reflect.Type

data class DayResponse(val htmlBody: String)

interface AocService {
    @GET("{year}/day/{day}")
    suspend fun fetchDay(@Path("year") year: Int = 2021, @Path("day") day: Int) : DayResponse

    @GET("{year}/day/{day}/input")
    suspend fun fetchDayInput(@Path("year") year: Int = 2021, @Path("day") day: Int) : String
}

fun createAocService(loginCookie: String = "_ga=GA1.2.212516361.1638466956; _gid=GA1.2.809596380.1638800516; session=53616c7465645f5fd75083f0bd8562a46259611f6bc9b05be347558f7939c62131389cb6cd2241dfb92d71ccb4450858"): AocService {
    val httpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val builder = original.newBuilder()
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .header("Cookie", loginCookie)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36")
            val request = builder.build()
            chain.proceed(request)
        }
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://adventofcode.com/")
        .addConverterFactory(object : Converter.Factory() {
            override fun responseBodyConverter(
                type: Type,
                annotations: Array<out Annotation>,
                retrofit: Retrofit
            ): Converter<ResponseBody, String>? {
                return Converter<ResponseBody, String> { it.string() }
            }
        })
        .client(httpClient)
        .build()
    return retrofit.create(AocService::class.java)
}

