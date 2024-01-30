package com.github.sandroln.kanbanboard.main

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import com.github.sandroln.core.ProvideViewModel
import com.github.sandroln.kanbanboard.R

class MainActivity : AppCompatActivity(), ProvideViewModel {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = viewModel(this, MainViewModel::class.java)
        viewModel.observe(this) {
            it.show(supportFragmentManager, R.id.container)
        }
        val errorTextView = findViewById<TextView>(R.id.errorTextView)
        viewModel.observeConnection(this) { hasInternet ->
            errorTextView.visibility = if (hasInternet) View.GONE else View.VISIBLE
        }
        viewModel.init(savedInstanceState == null)
    }

    override fun <T : ViewModel> viewModel(owner: ViewModelStoreOwner, className: Class<T>): T =
        (application as ProvideViewModel).viewModel(owner, className)
}