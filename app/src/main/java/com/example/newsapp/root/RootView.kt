package com.example.newsapp.root

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.newsapp.utils.compose.rib.Compose

class RootView : Compose, RootInteractor.Presenter {

    private val mutableChildView = mutableStateOf<ComposableHolder?>(null)
    private val mutableUpdateView = mutableStateOf<Compose?>(null)

    data class ComposableHolder(
        val animated: Boolean,
        val composableView: @Composable (Modifier) -> Unit,
        var remove: Boolean = false
    )

    @Composable
    override fun Content(modifier: Modifier) {
        NewsAppTheme {
            Surface(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    mutableUpdateView.value?.Content(Modifier)
                }
            }
        }
    }

    fun addFullScreenView(view: Compose, animation: Boolean = true) {
        mutableChildView.value = ComposableHolder(animated = animation, { modifier ->
            view.Content(modifier)
        }, false)
    }

    fun removeContainerView() {
        mutableChildView.value?.let {
            mutableChildView.value = it.copy(remove = true)
        }
    }

    fun setUpdateView(compose: Compose) {
        mutableUpdateView.value = compose
    }
}
