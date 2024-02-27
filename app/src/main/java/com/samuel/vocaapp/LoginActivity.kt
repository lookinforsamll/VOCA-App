package com.samuel.vocaapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.samuel.vocaapp.databinding.ActivityLoginBinding
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        const val EXTRA_SOURCE_ACTIVITY = "source_activity"
        private const val RC_SIGN_IN = 9001
        private const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        val sessionManager = SessionManager(this)

        if (sessionManager.isLogin()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            binding.btnLogin.setOnClickListener {
                val email = binding.edtLoginEmail.text.toString()
                val password = binding.edtLoginPassword.text.toString()

                if (email.isEmpty()) {
                    binding.edtLoginEmail.error = "Email tidak bisa kosong."
                    binding.edtLoginEmail.requestFocus()
                    return@setOnClickListener
                }

                if (password.isEmpty()) {
                    binding.edtLoginPassword.error = "Password tidak bisa kosong."
                    binding.edtLoginPassword.requestFocus()
                    return@setOnClickListener
                }

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val currentUser = auth.currentUser
                            val uid = currentUser?.uid
                            val usersRef = FirebaseDatabase.getInstance().getReference("users")
                            uid?.let {
                                usersRef.child(it).child("Nama Lengkap").addListenerForSingleValueEvent(object :
                                    ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val name = snapshot.getValue(String::class.java)
                                        Toast.makeText(this@LoginActivity, "Selamat datang $name", Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                    }
                                })
                            }
                            sessionManager.sessionLogin(email, password)
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                "Email atau kata sandi salah.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(this, gso)

            if (sessionManager.isGoogleLoggedIn()) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
            }

            binding.btnGoogle.setOnClickListener {
                val signInIntent = googleSignInClient.signInIntent
                googleSignInLauncher.launch(signInIntent)
            }
        }

        val textView = binding.txtLogin1
        val text = "VOCA APP"
        val spannableString = SpannableString(text)
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#3E8BF3")),
            0, 4,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#203864")),
            5, text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textView.text = spannableString

        val text2 = "Dengan masuk ke aplikasi Voca App. Kamu Menyetujui segala Syarat dan Ketentuan dan Kebijakan Privasi Voca App"
        val spannableString2 = SpannableString(text2)
        val termAndConditionStart = text2.indexOf("Syarat dan Ketentuan")
        val termAndConditionEnd = termAndConditionStart + "Syarat dan Ketentuan".length
        val privacyPolicyStart = text2.indexOf("Kebijakan Privasi Voca App")
        val privacyPolicyEnd = privacyPolicyStart + "Kebijakan Privasi Voca App".length
        spannableString2.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                startSDKActivity(LoginActivity::class.java.simpleName)
            }
        }, termAndConditionStart, termAndConditionEnd, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)

        spannableString2.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                startKDPActivity(LoginActivity::class.java.simpleName)
            }
        }, privacyPolicyStart, privacyPolicyEnd, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)

        spannableString2.setSpan(
            StyleSpan(Typeface.BOLD),
            termAndConditionStart, termAndConditionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString2.setSpan(
            StyleSpan(Typeface.BOLD),
            privacyPolicyStart, privacyPolicyEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableString2.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorTermAndCondition)),
            termAndConditionStart, termAndConditionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString2.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrivacyPolicy)),
            privacyPolicyStart, privacyPolicyEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.txtLogin6.text = spannableString2
        binding.txtLogin6.movementMethod = LinkMovementMethod.getInstance()

        binding.txtLLogin.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.txtLLupaPassword.setOnClickListener {
            val intent = Intent(this, LPActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startSDKActivity(sourceActivity: String) {
        val sdkIntent = Intent(this@LoginActivity, SDKActivity::class.java).apply {
            putExtra(EXTRA_SOURCE_ACTIVITY, sourceActivity)
        }
        startActivity(sdkIntent)
    }

    private fun startKDPActivity(sourceActivity: String) {
        val kdpIntent = Intent(this@LoginActivity, KDPActivity::class.java).apply {
            putExtra(EXTRA_SOURCE_ACTIVITY, sourceActivity)
        }
        startActivity(kdpIntent)
    }

    private val googleSignInLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            }
        } else {
            Toast.makeText(this@LoginActivity, "Google Sign-In failed.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            if (account != null) {
                firebaseAuthWithGoogle(account)
                saveGoogleAccountToDatabase(account)
            } else {
                Toast.makeText(this@LoginActivity, "Google Sign-In failed.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            Toast.makeText(this@LoginActivity, "Google Sign-In failed.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    saveGoogleAccountToSession(acct)
                    Toast.makeText(this@LoginActivity, "Google Sign-In successful.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveGoogleAccountToDatabase(account: GoogleSignInAccount) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val usersRef = FirebaseDatabase.getInstance().getReference("users")
        uid?.let {
            val userData = hashMapOf(
                "Email" to account.email,
                "Nama Lengkap" to account.displayName
            )
            usersRef.child(it).setValue(userData)
        }
    }

    private fun saveGoogleAccountToSession(account: GoogleSignInAccount) {
        val sessionManager = SessionManager(this)
        sessionManager.sessionLoginWithGoogle(account.idToken!!)
    }
}