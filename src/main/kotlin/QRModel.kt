// Request

data class qrRequest(
    val infoTx: infoTx,
    val qrCobro: qrCobro
)

data class infoTx(
    val sisOrigen: String = "SO_STABLE"
)

data class qrCobro(
    val idTrans: String,
    val codMoneda: String ="BOB",
    val importe: Long,
    val glosa: String,
    val fechaVencimiento: String,
    val usoUnico: Boolean,
    val codOperacion: String = "",
    val cuentaAbono: String = "CUENTA1_BOB",
    val pos: String = "POS1",
    val cajero: String = "CAJ1",
    val canal: String= "WEB",
    val tiempoDuracion: Int,
    val codProducto: String="COD_1234",
    val catProducto: String="CAT_1234",
    val sucursal: String="Sucursal_1",
    val info: String="Info"
)

// Response

data class qrResponse(
    val header:headerResponse,
    val hash: String,
    val idQr: String,
    val idQrInterno: String
)

data class headerResponse(
    val codReturn: String,
    val txtReturn: String
)

// Aux

enum class genType{
    INDIVIUAL,
    SET
}

