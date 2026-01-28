import javax.swing.SwingUtilities

fun main(){
    println("QR Generator V1.0 ")

    SwingUtilities.invokeLater {
        val app = QRGeneratorUI()
        app.isVisible = true
    }
}