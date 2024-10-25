package  com.uoons.india.utils

import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import com.uoons.india.R
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class OtpGenericKeyEvent(var currentView: EditText, var previousView: EditText?) :
    View.OnKeyListener {
    override fun onKey(view: View, keyCode: Int, keyEvent: KeyEvent): Boolean {
       if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.otp_edit_box1 && currentView.text.toString()
                .isEmpty()
        ) {
            previousView?.setText(null)
          previousView?.requestFocus()
            return true
       }
        return false
    }
}