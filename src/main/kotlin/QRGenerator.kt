import com.google.zxing.BinaryBitmap
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object QRGenerator {
  private val httpClient = okhttp3.OkHttpClient.Builder()
      .connectTimeout(30, TimeUnit.SECONDS)
      .readTimeout(30, TimeUnit.SECONDS).build()

    private val urlSV = Retrofit.Builder()
        .baseUrl("https://testing.tesabiz.com/")
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(QrApiService::class.java)

    fun generateQr(){
        val idTransaction = "${System.currentTimeMillis()}"
        println("ID Transaction: $idTransaction")

        try{
            val genRequest = qrRequest(
                infoTx = infoTx(),
                qrCobro = qrCobro(
                    idTrans = idTransaction,
                    importe =
                ),

        }catch (e: Exception){

        }

    }
}