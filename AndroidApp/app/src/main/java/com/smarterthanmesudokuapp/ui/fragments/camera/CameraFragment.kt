package com.smarterthanmesudokuapp.ui.fragments.camera

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.digitrecognition.DigitRecogniser
import com.smarterthanmesudokuapp.R
import com.smarterthanmesudokuapp.databinding.FragmentCameraBinding
import com.smarterthanmesudokuapp.domain.entities.SudokuVo
import com.smarterthanmesudokuapp.ui.fragments.home.HomeArguments
import com.smarterthanmesudokuapp.utils.ContourExtractor
import com.smarterthanmesudokuapp.utils.disable
import com.smarterthanmesudokuapp.utils.enable
import dagger.android.support.DaggerFragment
import org.opencv.android.CameraBridgeViewBase
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import javax.inject.Inject

class CameraFragment : DaggerFragment(), CameraBridgeViewBase.CvCameraViewListener2 {

    private var sudokuBoardMat: Mat? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: CameraViewModel by viewModels { viewModelFactory }

    private lateinit var viewDataBinding: FragmentCameraBinding

    private lateinit var backgroundThread: HandlerThread

    private lateinit var backgroundHandler: Handler

    @Inject
    lateinit var contourExtractor: ContourExtractor

    @Inject
    lateinit var digitRecogniser: DigitRecogniser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding =
            FragmentCameraBinding.inflate(LayoutInflater.from(context), container, false)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.captureButton.disable()
        val displayMetrics = requireContext().resources.displayMetrics
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

        viewDataBinding.cameraView.visibility = SurfaceView.VISIBLE
        viewDataBinding.cameraView.setCvCameraViewListener(this)
        viewDataBinding.cameraView.setMaxFrameSize(height, width)

        viewDataBinding.captureButton.setOnClickListener {
            if (sudokuBoardMat != null) {
                Core.rotate(sudokuBoardMat, sudokuBoardMat, Core.ROTATE_90_CLOCKWISE)
                val sudoku = digitRecogniser.recognise(sudokuBoardMat)
                findNavController().navigate(
                    R.id.action_navigation_camera_to_navigation_home,
                    Bundle().apply {
                        putParcelable(
                            "args", HomeArguments(
                                SudokuVo(
                                    sudoku = sudoku,
                                    solution = null,
                                    complexity = null,
                                    currentSudoku = null
                                )
                            )
                        )
                    }
                )
            }
        }
    }

    private fun openBackgroundThread() {
        backgroundThread = HandlerThread("cameraBackgroundThread")
        backgroundThread.start()
        backgroundHandler = Handler(backgroundThread.looper)
    }

    private fun closeBackgroundThread() {
        backgroundThread.quitSafely()
    }

    override fun onResume() {
        super.onResume()
        openBackgroundThread()
        viewDataBinding.cameraView.enableView()
    }

    override fun onStop() {
        super.onStop()
        viewDataBinding.cameraView.disableView()
        closeBackgroundThread()
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        viewDataBinding.captureButton.enable()
    }

    override fun onCameraViewStopped() {
        viewDataBinding.cameraView.disableView()
    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat {
        val ogRGBMat = inputFrame!!.rgba()

        val centrePoint = Point(ogRGBMat.size().width / 2, ogRGBMat.size().height / 2)

        val ogGRAYMat = Mat()
        Imgproc.cvtColor(ogRGBMat, ogGRAYMat, Imgproc.COLOR_BGR2GRAY, 1)

        val blurredMat = Mat()
        Imgproc.GaussianBlur(ogGRAYMat, blurredMat, Size(7.0, 7.0), 0.0)
        val threshMat = Mat()

        Imgproc.adaptiveThreshold(
            blurredMat,
            threshMat,
            255.0,
            Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
            Imgproc.THRESH_BINARY,
            11,
            4.0
        )

        val kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, Size(4.0, 4.0))
        val closed = Mat()
        Imgproc.morphologyEx(threshMat, closed, Imgproc.MORPH_OPEN, kernel)

        val contours: ArrayList<MatOfPoint> = ArrayList(0)
        Imgproc.findContours(
            threshMat,
            contours,
            Mat(),
            Imgproc.RETR_LIST,
            Imgproc.CHAIN_APPROX_SIMPLE
        )

        val ogArea = ogGRAYMat.size().width * ogGRAYMat.size().height
        val minArea = ogArea * 0.04
        val maxArea = ogArea * 0.3
//        Log.d(TAG, "onCameraFrame: $minArea $maxArea ${contours.size}")

        val realContours = ArrayList<MatOfPoint>(0)
        for (counter in 1 until contours.size) {
            val contourArea = Imgproc.contourArea(contours[counter], true)

            if (contourArea > minArea && contourArea < maxArea) {
                realContours.add(contours[counter])
            }
        }


        realContours.sortBy {
            Imgproc.contourArea(it)
        }

        val indexOfBoard = realContours.indexOfFirst {
            val corners = contourExtractor.identifyCorners(it.toArray())
            val topLeftCornerX = corners[0].x
            val topRightCornerX = corners[1].x
            val bottomLeftCornerX = corners[2].x
            val bottomRightCornerX = corners[3].x
            val topLeftCornerY = corners[0].y
            val topRightCornerY = corners[1].y
            val bottomLeftCornerY = corners[2].y
            val bottomRightCornerY = corners[3].y

            val isXInRange =
                centrePoint.x <= (topRightCornerX + bottomRightCornerX) / 2 && centrePoint.x >= (topLeftCornerX + bottomLeftCornerX) / 2
            val isYInRange =
                centrePoint.y <= (bottomLeftCornerY + bottomRightCornerY) / 2 && centrePoint.y >= (topLeftCornerY + topRightCornerY) / 2
            isXInRange && isYInRange
        }

        Imgproc.drawMarker(
            ogRGBMat,
            Point(centrePoint.x, centrePoint.y),
            Scalar(252.0, 255.0, 89.0),
            Imgproc.MARKER_CROSS,
            20,
            3
        )
        Imgproc.drawContours(ogRGBMat, realContours, indexOfBoard, Scalar(252.0, 255.0, 89.0), 3)

        try {
            val boundingRect = Imgproc.boundingRect(realContours[indexOfBoard])
            sudokuBoardMat = Mat(
                ogGRAYMat,
                Rect(
                    (boundingRect.x - 5),
                    (boundingRect.y - 5),
                    (boundingRect.width + 10),
                    (boundingRect.height + 10)
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ogRGBMat
    }

}