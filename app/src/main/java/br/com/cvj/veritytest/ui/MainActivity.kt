package br.com.cvj.veritytest.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.cvj.veritytest.R
import br.com.cvj.veritytest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }
}