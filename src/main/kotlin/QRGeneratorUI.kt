import java.awt.BorderLayout
import java.awt.Color
import java.awt.Cursor
import java.awt.Font
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.SwingConstants
import javax.swing.WindowConstants
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder
import javax.swing.border.LineBorder

class QRGeneratorUI: JFrame("QR Code generation") {

    //Style
    private val colorBackground = Color(37, 38, 42)

    private val option= mapOf(
        "Generate Individual QR" to genType.INDIVIUAL,
        "Generate Preconfigured Set" to genType.SET
    )

    val options =arrayOf("Yes", "No")
    val today = LocalDate.now()
    val formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    private val txtAmount = JTextField("")
    private val txtDetail = JTextField("")
    private val txtExpiredDate = JTextField(today.format(formatDate))
    private val cmbUnique = JComboBox(options)
    private val txtDuration = JTextField("")
    private val btnRun = JButton("Generate QR 🐹")

    init {
        layout = BorderLayout()
        setSize(900,700)
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        setLocationRelativeTo(null)
        isVisible = true

        val gbc = GridBagConstraints()
        gbc.insets = Insets(10, 10, 10, 10)
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.anchor = GridBagConstraints.WEST

        val panel = JPanel(BorderLayout())
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
            field.background = Color.WHITE
            field.foreground=Color.BLACK
            field.margin = Insets(5, 5, 5, 5)
            field.preferredSize = java.awt.Dimension(100,25)
            field.border = CompoundBorder(
                LineBorder(Color(209,213,219)),
                EmptyBorder(8,10,8,10)
            )

        }

        fun addLabel(text: String, yPos: Int) {
            val label = JLabel(text)
            label.font = fontLabel
            label.foreground = Color.BLACK
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
        addComponent(txtAmount, 0)

        addLabel("Detail:", 1)
        styleInput(txtDetail)
        addComponent(txtDetail, 1)

        addLabel("Expired Date", 2)
        styleInput(txtExpiredDate)
        addComponent(txtExpiredDate, 2)
        txtExpiredDate.isEnabled=false

        addLabel("Unique use:", 3)
        cmbUnique.font = fontText
        cmbUnique.background = Color.WHITE
        (cmbUnique.renderer as? JLabel)?.horizontalAlignment = SwingConstants.LEFT
        cmbUnique.preferredSize = java.awt.Dimension(300, 40)
        addComponent(cmbUnique, 3)

        addLabel("Duration (in minutes)", 4)
        styleInput(txtDuration)
        addComponent(txtDuration, 4)

        topContainer.add(formPanel)

        // Bottom Panel
        btnRun.font = Font("Monospaced", Font.BOLD, 14)
        btnRun.background = colorBackground
        btnRun.foreground = Color.white
        btnRun.isOpaque = true
        btnRun.isBorderPainted = false
        btnRun.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        btnRun.preferredSize = java.awt.Dimension(200, 50)
        btnRun.isFocusPainted = false
    }

}