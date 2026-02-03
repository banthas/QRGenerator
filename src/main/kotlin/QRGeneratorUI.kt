import java.awt.BorderLayout
import java.awt.Color
import java.awt.Cursor
import java.awt.FlowLayout
import java.awt.Font
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.JTextField
import javax.swing.SwingConstants
import javax.swing.WindowConstants
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder
import javax.swing.border.LineBorder
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.PrintStream
import javax.swing.JSplitPane
import javax.swing.SwingUtilities

class QRGeneratorUI: JFrame("QR Code generation") {

    //Style
    private val colorBackground = Color(43, 43, 43)        // Gris oscuro suave (Fondo principal)
    private val colorPanel = Color(60, 63, 65)             // Gris un poco más claro (Para inputs)
    private val colorTextPrimary = Color(187, 187, 187)    // Blanco hueso (Para textos/labels)
    private val colorTextAccent = Color(144, 238, 144)     // Verde Matrix (Para logs)
    private val colorButton = Color(75, 110, 175)          // Azul profesional (Botón)
    private val colorButtonText = Color.WHITE
    private val colorConsoleBg = Color(30, 30, 30)
    private val colorConsoleText = Color(144, 238, 144)

    private val option= mapOf(
        "Generate Individual QR" to genType.INDIVIUAL,
        "Generate Preconfigured Set" to genType.SET
    )

    val options =arrayOf("Yes", "No")
    val today = LocalDate.now()
    val formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    private val txtAmount = JTextField("")
    private val txtDetail = JTextField("")
    private val txtExpiredDate = JTextField(today.format(formatDate))
    private val cmbUnique = JComboBox(options)
    private val txtDuration = JTextField("")
    private val txtLogArea = JTextArea("")
    private val btnRun = JButton("Generate QR 🐹")
    private val imageQR = JLabel("Wait for the image QR", SwingConstants.CENTER)

    init {
        layout = BorderLayout()
        setSize(900,800)
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        setLocationRelativeTo(null)
        isVisible = true

        val gbc = GridBagConstraints()
        gbc.insets = Insets(10, 10, 10, 10)
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.anchor = GridBagConstraints.WEST

        val panel = JPanel(BorderLayout(0,20))
        panel.background =colorBackground
        panel.border= EmptyBorder(20,20,20,20)
        add(panel)

        val topContainer = JPanel()
        topContainer.layout = BoxLayout(topContainer, BoxLayout.Y_AXIS)
        topContainer.background = colorBackground
        topContainer.isOpaque = true

        val formPanel = JPanel(GridBagLayout())
        formPanel.background = colorBackground
        formPanel.border = EmptyBorder(0, 0, 30, 0)


        val fontLabel = Font("Arial", Font.BOLD, 14)
        val fontText=Font("Arial", Font.PLAIN, 12)

        fun styleInput(field: JTextField) {
            field.font = fontLabel
            field.background = colorPanel
            field.foreground = Color.WHITE
            field.caretColor = Color.WHITE
            field.margin = Insets(5, 5, 5, 5)
            field.preferredSize = java.awt.Dimension(100,35)
            field.border = CompoundBorder(
                LineBorder(Color.gray),
                EmptyBorder(8,10,8,10)
            )

        }

        fun addLabel(text: String, yPos: Int) {
            val label = JLabel(text)
            label.font = fontLabel
            label.foreground = colorTextPrimary
            gbc.gridx = 0
            gbc.gridy = yPos
            gbc.weightx = 0.0
            gbc.ipadx = 10
            formPanel.add(label, gbc)
            gbc.ipadx = 0
        }

        fun addComponent(comp: JComponent, yPos: Int) {
            gbc.gridx = 1
            gbc.gridy = yPos
            gbc.weightx = 1.0
            formPanel.add(comp, gbc)
        }

        addLabel("Amount (Cents):", 0)
        styleInput(txtAmount)
        txtAmount.setNumericLimit(7)
        addComponent(txtAmount, 0)

        addLabel("Detail:", 1)
        styleInput(txtDetail)
        txtDetail.setLimit(50)
        addComponent(txtDetail, 1)

        addLabel("Expired Date", 2)
        styleInput(txtExpiredDate)
        addComponent(txtExpiredDate, 2)
        txtExpiredDate.isEnabled=false

        addLabel("Unique use:", 3)
        cmbUnique.font = fontText
        cmbUnique.background = colorPanel
        cmbUnique.foreground=Color.GRAY
        (cmbUnique.renderer as? JLabel)?.horizontalAlignment = SwingConstants.LEFT
        cmbUnique.preferredSize = java.awt.Dimension(300, 40)
        addComponent(cmbUnique, 3)

        addLabel("Duration (in minutes)", 4)
        styleInput(txtDuration)
        txtDuration.setNumericLimit(2)
        addComponent(txtDuration, 4)

        topContainer.add(formPanel)

        // Bottom Panel
        btnRun.font = Font("Segoe UI", Font.BOLD, 16)
        btnRun.background = colorButton
        btnRun.foreground = colorButtonText
        btnRun.isFocusPainted = false
        btnRun.isOpaque = true
        btnRun.isBorderPainted = false
        btnRun.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        btnRun.preferredSize = java.awt.Dimension(200, 50)
        btnRun.isFocusPainted = false

        val btnPanel = JPanel(FlowLayout(FlowLayout.CENTER))
        btnPanel.background = colorBackground
        btnPanel.add(btnRun)
        topContainer.add(btnPanel)
        panel.add(topContainer, BorderLayout.NORTH)

        txtLogArea.isEditable=false
        txtLogArea.background = colorConsoleBg
        txtLogArea.foreground = colorConsoleText
        txtLogArea.font = Font("Arial", Font.PLAIN, 12)
        txtLogArea.margin = Insets(20, 20, 20, 20)
        txtLogArea.lineWrap=true
        txtLogArea.wrapStyleWord = true

        val scroll = JScrollPane(txtLogArea)
        scroll.border= BorderFactory.createTitledBorder(
            LineBorder(Color.gray,1),
            "🐹Execution Console",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            Font("Arial", Font.BOLD, 10),
            Color.WHITE
        )

        scroll.background=colorBackground

        val splitPane = JSplitPane()
        val scrollImagen = JScrollPane(imageQR)
        scrollImagen.border = BorderFactory.createTitledBorder("QR Preview")

        splitPane.orientation = JSplitPane.HORIZONTAL_SPLIT
        splitPane.leftComponent = scroll
        splitPane.rightComponent = scrollImagen
        splitPane.resizeWeight = 0.5
        splitPane.dividerLocation = 400

        panel.add(splitPane, BorderLayout.CENTER)

        redirectSystemStreams()
        btnRun.addActionListener {
            validations()
        }

    }

    fun validations(){

        if(txtAmount.text.isBlank() || txtAmount.text.toLongOrNull() == null){
            JOptionPane.showMessageDialog(this, "Please input a valid numeric amount.", "Validation Error",
                JOptionPane.WARNING_MESSAGE)
            return
        }

        if(txtDetail.text.isBlank()){
            JOptionPane.showMessageDialog(this, "Please enter a detail description.", "Validation Error",
                JOptionPane.WARNING_MESSAGE)
            return
        }
        val tipo = cmbUnique.selectedItem as String
        if (tipo.isBlank()){
            JOptionPane.showMessageDialog(this, "Please select a valid value.", "Validation Error",
                JOptionPane.WARNING_MESSAGE)
            return
        }

        if(txtDuration.text.isBlank()|| txtDuration.text.toLongOrNull() == null){
            JOptionPane.showMessageDialog(this, "Please input a valid duration in minutes.", "Validation Error",
                JOptionPane.WARNING_MESSAGE)
            return
        }

        startTest()
    }

    fun startTest(){
        val amount= txtAmount.text.toLongOrNull() ?:1000L
        val detail = txtDetail.text
        val unique = cmbUnique.selectedItem.toString() == "Yes"
        val date = txtExpiredDate.text
        val duration = txtDuration.text.toIntOrNull() ?: 60
        val idTransaction = "${System.currentTimeMillis()}"
        println("ID Transaction: $idTransaction")

        val cobro = qrCobro(
            idTrans = idTransaction,
            importe = amount.toLong(),
            glosa = detail.toString(),
            fechaVencimiento = date,
            usoUnico = unique,
            tiempoDuracion = duration
        )
        val realAmount = amount/100
        GlobalScope.launch(Dispatchers.IO) {
            txtLogArea.text = ""
            println("=========================================")
            println("🐹 GUINEA PIG PROTOCOL ACTIVE 🐹")
            println("💰 Amount: $realAmount | 📝 Detail: $detail")
            println("=========================================")

            val time = measureTimeMillis {
                        println("🐹 Executing Happy path only one cuy ...")
                        println("ID Transaction: $idTransaction")
                        QRGenerator.generateQr(cobro){ pathRecibido ->
                            SwingUtilities.invokeLater {
                                val archivo = java.io.File(pathRecibido)
                                if (archivo.exists()) {
                                    val icon = javax.swing.ImageIcon(pathRecibido)
                                    val imagenEscalada = icon.image.getScaledInstance(300, 300, java.awt.Image.SCALE_SMOOTH)
                                    imageQR.icon = javax.swing.ImageIcon(imagenEscalada)
                                    imageQR.text = ""
                                }
                            }
                        }
            }

            println("\n=========================================")
            println("🎉 TEST COMPLETED - Time: ${time}ms")
            println("🐭🐇🐹🦫 The rodents are free to eat now ")
            println("=========================================")
        }


    }
    private fun redirectSystemStreams() {
        val out = object : java.io.OutputStream() {
            override fun write(b: Int) {
                updateText(String(byteArrayOf(b.toByte())))
            }
            override fun write(b: ByteArray, off: Int, len: Int) {
                updateText(String(b, off, len))
            }
            private fun updateText(text: String) {
                SwingUtilities.invokeLater {
                    txtLogArea.append(text)
                    txtLogArea.setCaretPosition(txtLogArea.document.length)
                }
            }
        }
        System.setOut(PrintStream(out, true))
        System.setErr(PrintStream(out, true))
    }
}