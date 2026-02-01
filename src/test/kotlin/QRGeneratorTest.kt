import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import kotlin.test.Test

@DisplayName("QR Generator Test")
class QRGeneratorTest {

    lateinit var qrCobro: qrCobro

    @BeforeEach
    fun setUp() {
        qrCobro = qrCobro(
            idTrans = "",
            importe = 0,
            glosa = "",
            fechaVencimiento = "2026-01-25",
            usoUnico = false,
            tiempoDuracion = 0
        )

    }
    @Test
    @DisplayName("Invalid data")
    fun invalidDataTest() {
       QRGenerator.generateQr(qrCobro){}

    }

}