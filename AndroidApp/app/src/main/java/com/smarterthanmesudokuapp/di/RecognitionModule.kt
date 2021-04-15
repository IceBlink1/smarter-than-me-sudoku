package com.smarterthanmesudokuapp.di

import android.content.Context
import com.digitrecognition.DigitRecogniser
import com.digitrecognition.GridExtractor
import com.smarterthanmesudokuapp.utils.ContourExtractor
import com.smarterthanmesudokuapp.utils.FuncUtils.assetFilePath
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object RecognitionModule {

    @Singleton
    @Provides
    fun providesDigitRecognizer(context: Context): DigitRecogniser {
        return DigitRecogniser(
            assetFilePath(context, "model1.txt")
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