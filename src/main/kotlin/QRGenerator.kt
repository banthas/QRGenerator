
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.awt.Image
import java.io.ByteArrayInputStream
import java.io.File
import java.util.Base64
import java.util.concurrent.TimeUnit
import javax.imageio.ImageIO
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

import javax.swing.ImageIcon
import kotlin.String
object QRGenerator {

    private val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    })

    private val sslContext = SSLContext.getInstance("SSL").apply {
        init(null, trustAllCerts, java.security.SecureRandom())
    }

    private val httpClient = OkHttpClient.Builder()
        .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
        .hostnameVerifier { _, _ -> true }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val urlSV = Retrofit.Builder()
        .baseUrl("https://testing.tesabiz.com/")
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(QrApiService::class.java)

    fun paintAndSave(contenido: String, nombreArchivo: String, onImageSaved: (String) -> Unit) {
        try {
            val textoLimpio = contenido.substringAfter("base64,")

            val imageBytes = Base64.getDecoder().decode(textoLimpio)

            val bis = ByteArrayInputStream(imageBytes)
            val imagen = ImageIO.read(bis)
            val tempDir = System.getProperty("java.io.tmpdir")
            val archivoSalida = File(tempDir, "$nombreArchivo.png")

            ImageIO.write(imagen, "png", archivoSalida)
            println("✅ Success: QR saved: ${archivoSalida.absolutePath}")
            onImageSaved(archivoSalida.absolutePath)

        } catch (e: Exception) {
            println("❌ Error: failed to generate the QR code, service error")
            e.printStackTrace()
        }
    }


    fun generateQr(qReq:qrCobro, onResult: (String) -> Unit) {
        try{
            val genRequest = qrRequest(
                infoTx = infoTx(),
                qrCobro = qReq
            )
            val response = urlSV.generateQr(genRequest).execute()

            if(!response.isSuccessful){
                println(" ❌ Generating QR code fails: ${response.code()}")
                return
            }else{
                println("✅ QR code generated: ${response.code()}")
            }

            val bodyGen = response.body()

            val hash = bodyGen?.hash.toString()

            paintAndSave(hash,"QR_${qReq.importe}",onResult)

        }catch (e: Exception){
            println("💥 Critical error: ${e.message}")
        }

    }
}