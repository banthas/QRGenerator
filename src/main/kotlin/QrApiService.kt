import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface QrApiService{
    @POST("ws-qr-service/qr/simple/encript64")
    fun generateQr(@Body request: qrRequest): Call<qrResponse>
}