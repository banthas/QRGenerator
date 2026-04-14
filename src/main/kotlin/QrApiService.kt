import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface QrApiService{
    @POST("ws-qr-service/qr/simple/encript64")
    fun generateQr(@Body request: qrRequest): Call<qrResponse>

    @POST("webhooks/cfx/deposit/status")
    fun simulateDeposit(
        @Header("Idempotency-Key") idempotencyKey: String,
        @Header("x-cfx-signature") signature: String,
        @Body request: depositRequest
    ): Call<Void>
}