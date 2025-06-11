package com.example.creatorsociety

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        emailInput = findViewById<TextInputLayout>(R.id.et_email).editText!!
        passwordInput = findViewById<TextInputLayout>(R.id.et_password).editText!!
        confirmPasswordInput = findViewById<TextInputLayout>(R.id.et_cpassword).editText!!

        auth = FirebaseAuth.getInstance()

        val loginTextView = findViewById<TextView>(R.id.tv_login)
        val loginText = "Already have an account? Log In"
        val spannableString = SpannableString(loginText)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(this@SignUpActivity, LogInActivity::class.java))
            }
        }
        val startIndex = loginText.indexOf("Log In")
        val endIndex = startIndex + "Log In".length
        spannableString.setSpan(clickableSpan, startIndex, endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        loginTextView.text = spannableString
        loginTextView.movementMethod = LinkMovementMethod.getInstance()

        findViewById<View>(R.id.btn_signup).setOnClickListener {
            if (validateInputs()) {
                signUpUser()
            }
        }
    }

    private fun validateInputs(): Boolean {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()
        val confirmPassword = confirmPasswordInput.text.toString().trim()

        if (email.isEmpty()) {
            emailInput.error = "Email cannot be empty"
            emailInput.requestFocus()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.error = "Please enter a valid email address"
            emailInput.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            passwordInput.error = "Password cannot be empty"
            passwordInput.requestFocus()
            return false
        }
        if (password.length < 6) {
            passwordInput.error = "Password must be at least 6 characters long"
            passwordInput.requestFocus()
            return false
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordInput.error = "Please confirm your password"
            confirmPasswordInput.requestFocus()
            return false
        }
        if (password != confirmPassword) {
            confirmPasswordInput.error = "Passwords do not match"
            confirmPasswordInput.requestFocus()
            return false
        }

        return true
    }

    private fun signUpUser() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Sign-Up Successful!",
                        Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, ProfileSetupActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Sign-Up Failed: ${task.exception?.message}",
                        Toast.LENGTH_LONG).show()
                }
            }
    }
}
