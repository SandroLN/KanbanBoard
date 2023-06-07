package com.github.sandroln.kanbanboard.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import com.github.sandroln.kanbanboard.R
import com.github.sandroln.kanbanboard.core.ProvideViewModel

class MainActivity : AppCompatActivity(), ProvideViewModel {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = viewModel(this, MainViewModel::class.java)
        viewModel.observe(this) {
            it.show(supportFragmentManager, R.id.container)
        }
        viewModel.init(savedInstanceState == null)
    }

    override fun <T : ViewModel> viewModel(owner: ViewModelStoreOwner, className: Class<T>): T =
        (application as ProvideViewModel).viewModel(owner, className)
}