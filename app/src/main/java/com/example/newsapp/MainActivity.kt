package com.example.newsapp

import com.example.newsapp.root.builder.RootBuilder
import com.example.newsapp.root.builder.RootDependencies
import com.example.newsapp.utils.compose.rib.ComposeRouter
import com.example.newsapp.utils.compose.rib.RibComposeActivity

class MainActivity : RibComposeActivity() {
    override fun createRouter(): ComposeRouter<*, *> {
        return RootBuilder(applicationContext as RootDependencies).build()
    }

}

