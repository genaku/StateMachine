package com.genaku.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

//    val sm = stateMachine{
//        mappings(
//            Ice by Heat goesTo Water
//        )
//    }
//
//    object Heat: IAction
//    object Ice: IState
//    object Water: IState
}