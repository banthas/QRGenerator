import javax.swing.JTextField
import javax.swing.text.AttributeSet
import javax.swing.text.DocumentFilter
import javax.swing.text.AbstractDocument
import java.awt.Toolkit
import javax.swing.text.PlainDocument


class LimitFieldFilter(private val limit: Int, private val onlyDigits: Boolean = false) : DocumentFilter() {

    override fun insertString(fb: FilterBypass?, offset: Int, string: String?, attr: AttributeSet?) {
        if (fb != null) {
            replace(fb, offset, 0, string, attr)
        }
    }

    override fun replace(fb: FilterBypass, offset: Int, length: Int, text: String?, attrs: AttributeSet?) {
        val currentLength = fb.document.length
        val newLength = currentLength + (text?.length ?: 0) - length
        if (newLength <= limit) {

            if (onlyDigits && text != null && !text.all { it.isDigit() }) {
                Toolkit.getDefaultToolkit().beep()
                return
            }
            super.replace(fb, offset, length, text, attrs)
        } else {
            Toolkit.getDefaultToolkit().beep()
        }
    }
}


fun JTextField.setLimit(limit: Int) {
    this.document = PlainDocument()
    (this.document as AbstractDocument).documentFilter = LimitFieldFilter(limit)
}

fun JTextField.setNumericLimit(limit: Int) {
    this.document = PlainDocument()
    (this.document as AbstractDocument).documentFilter = LimitFieldFilter(limit, onlyDigits = true)
}