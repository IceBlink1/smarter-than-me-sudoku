package com.smarterthanmesudokuapp.di

import android.content.Context
import com.digitrecognition.DigitRecogniser
import com.digitrecognition.GridExtractor
import com.smarterthanmesudokuapp.ui.views.utils.ContourExtractor
import com.smarterthanmesudokuapp.ui.views.utils.FuncUtils.assetFilePath
import dagger.Module
import dagger.Provides
import org.opencv.android.OpenCVLoader
import javax.inject.Singleton

@Module
object RecognitionModule {

    @Singleton
    @Provides
    fun providesDigitRecognizer(context: Context): DigitRecogniser {
        return DigitRecogniser(
            assetFilePath(context, "result200-0.100000-1norm.txt")
        )
    }

    @Singleton
    @Provides
    fun providesGridExtractor(): GridExtractor {
        return GridExtractor()
    }

    @Singleton
    @Provides
    fun providesContourExtractor(): ContourExtractor {
        return ContourExtractor()
    }

}