package com.example.fixx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    var emailEditTxt : EditText? = null
    var passwordTxt : EditText? = null
    var signInBtn : Button? = null

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditTxt = findViewById(R.id.main_email_txt)
        passwordTxt = findViewById(R.id.main_password_txt)
        signInBtn = findViewById(R.id.main_signIn_btn)

        auth = Firebase.auth

        signInBtn?.let {
            it.setOnClickListener { event ->
                //startActivity(Intent(this, ShowTechniciansScreen::class.java))
                val email = emailEditTxt?.text.toString() ?: ""
                val password = passwordTxt?.text.toString() ?: ""
//                auth.signInWithEmailAndPassword("iti@gmail.com","123456").addOnCompleteListener {
//                    task ->
//                    if(task.isSuccessful){
//                        Log.i("TAG", "onCreate: >>>> SUCCESS")
//                    }else{
//                        Log.i("TAG", "onCreate: FAIL<<<<")
//                    }
//                }
                try {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.i("TAG", "createUserWithEmail:success")
                                val user = auth.currentUser

                                //added by esraa
                                //val addressIntent = Intent(this,AddAddressActivity::class.java)
                                // addressIntent.putExtra(MainActivity.Mobile, editMobile.getText().toString())
                                //addressIntent.putExtra(MainActivity.Msg, editMsg.getText().toString())
                                //startActivity(addressIntent)


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "createUserWithEmail:failure", task.exception)
                                Toast.makeText(
                                    baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()


                                //added by esraa
                                //val addressIntent = Intent(this,AddAddressActivity::class.java)
                                // addressIntent.putExtra(MainActivity.Mobile, editMobile.getText().toString())
                                //addressIntent.putExtra(MainActivity.Msg, editMsg.getText().toString())
                                //startActivity(addressIntent)
                            }
                        }
                }catch (error : FirebaseAuthUserCollisionException){
                    Log.i("TAG", "onCreate: Caught Exception")
                }
            }
        }
    }
}