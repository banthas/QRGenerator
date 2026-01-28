import javax.swing.JTextField
import javax.swing.text.AttributeSet
import javax.swing.text.DocumentFilter
import javax.swing.text.AbstractDocument


class LimitFieldFilter(private val limit: Int): DocumentFilter(){
    override fun insertString(fb: FilterBypass?, offset: Int, string: String?, attr: AttributeSet?) {
        if (fb != null) {
            replace(fb, offset, 0, string, attr)
        }
    }

    override fun replace(fb: FilterBypass, offset: Int, length: Int, text: String?, attrs: AttributeSet?) {
        val currentLength = fb.document.length
        val newLength = currentLength + (text?.length ?: 0) - length

        if (newLength <= limit) {
            super.replace(fb, offset, length, text, attrs)
        } else {
            // Opcional: Emitir un 'beep' si se pasan del límite
            java.awt.Toolkit.getDefaultToolkit().beep()
        }
    }
}
fun JTextField.setLimit(limit: Int) {
    (this.document as AbstractDocument).documentFilter = LimitFieldFilter(limit)
}