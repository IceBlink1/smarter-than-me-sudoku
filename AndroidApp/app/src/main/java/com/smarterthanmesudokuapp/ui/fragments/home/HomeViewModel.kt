package com.smarterthanmesudokuapp.ui.fragments.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.digitrecognition.DigitRecogniser
import org.opencv.core.Mat
import javax.inject.Inject
import dagger.Lazy as LazyInject

class HomeViewModel @Inject constructor(
    private val recogniser: LazyInject<DigitRecogniser>
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun recognise() {
        Log.d("bla", "asd")
    }

    fun recogniseForReal() {
        recogniser.get().recognise(Mat())
    }
}