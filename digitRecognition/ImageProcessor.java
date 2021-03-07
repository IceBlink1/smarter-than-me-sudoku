package digitRecognition;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ImageProcessor {
    public static Mat processImage(Mat mat, boolean fromCamera) {
        if(!fromCamera) {
            Mat threshMat = new Mat();
            Imgproc.adaptiveThreshold(mat, threshMat, 255.0,
                    Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
                    Imgproc.THRESH_BINARY, 23, 2.0);
            Mat swopMat = new Mat();
            Core.bitwise_not(threshMat, swopMat);
            return swopMat;
        }
        Mat blurredMat = new Mat();
        Imgproc.GaussianBlur(mat, blurredMat, new Size(11.0, 11.0), 0.0);
        Mat threshMat = new Mat();
        Imgproc.adaptiveThreshold(blurredMat, threshMat, 255.0,
                Imgproc.ADAPTIVE_THRESH_MEAN_C,
                Imgproc.THRESH_BINARY, 11, 8.0);
        Mat morphedMat = new Mat();
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE,
                new Size(4.0, 4.0));
        Mat swopMat = new Mat();
        Core.bitwise_not(morphedMat, swopMat);
        return swopMat;
    }
}
