package  com.uoons.india.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.uoons.india.R
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class OTPGenericTextWatcher(var currentView: EditText, var nextView: EditText?) :
    TextWatcher {
    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
    override fun afterTextChanged(editable: Editable) {
        val s = editable.toString()
        when (currentView.id) {
            R.id.otp_edit_box1 -> if (s.length == 1) nextView?.requestFocus()
            R.id.otp_edit_box2 -> if (s.length == 1) nextView?.requestFocus()
            R.id.otp_edit_box3 -> if (s.length == 1) nextView?.requestFocus()
        }
    }
}