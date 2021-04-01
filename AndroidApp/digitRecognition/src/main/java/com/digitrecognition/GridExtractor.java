import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.util.ArrayList;

public class GridExtractor {
    /**
     * Распознает поле судоку на оригинальном изображении.
     * @param original Изображение с полем судоку.
     * @return Изображение, полностью состоящее из поля судоку.
     */
    public static Mat contourGridExtract(Mat original) {
        Mat threshMat = new Mat();
        Imgproc.adaptiveThreshold(original, threshMat, 255,
                Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
                Imgproc.THRESH_BINARY, 11, 2);
        Core.bitwise_not(threshMat, threshMat);
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(threshMat, contours, new Mat(),
                Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        MatOfPoint largestContour = contours.get(0);
        for(int i = 1; i < contours.size(); ++i) {
            if(Imgproc.contourArea(largestContour) <=
                    Imgproc.contourArea(contours.get(i))) {
                largestContour = contours.get(i);
            }
        }
        ArrayList<Point> corners = identifyCorners(largestContour.toArray());
        ArrayList<Point> oCorners = new ArrayList<>();
        for(int i = 0; i < 4; ++i)
            oCorners.add(corners.get(i));
        ArrayList<Point> fCorners = new ArrayList<>();
        fCorners.add(new Point(0.0, 0.0));
        fCorners.add(new Point(original.width(), 0.0));
        fCorners.add(new Point(0.0, original.height()));
        fCorners.add(new Point(original.width(), original.height()));
        Mat initialPoints = Converters.vector_Point2f_to_Mat(oCorners);
        Mat finalPoints = Converters.vector_Point2f_to_Mat(fCorners);
        Mat transform = Imgproc.getPerspectiveTransform(
                initialPoints, finalPoints);
        Mat result = new Mat();
        Imgproc.warpPerspective(original, result, transform, original.size());
        return result;
    }

    private static ArrayList<Point> identifyCorners(
            Point[] contourPoints) {
        Point leftTopCorner = new Point();
        Point rightTopCorner = new Point();
        Point leftBottomCorner = new Point();
        Point rightBottomCorner = new Point();
        double xPlusY = 0.0;
        double xMinusY = 0.0;
        double smallestXPlusY = 1e9;
        double smallestXMinusY = 1e9;
        double largestXPlusY = 0.0;
        double largestXMinusY = 0.0;
        for(Point point : contourPoints) {
            xPlusY = point.x + point.y;
            xMinusY = point.x - point.y;
            if(xPlusY < smallestXPlusY) {
                smallestXPlusY = xPlusY;
                leftTopCorner = point;
            }
            if(xMinusY < smallestXMinusY) {
                smallestXMinusY = xMinusY;
                leftBottomCorner = point;
            }
            if(xMinusY > largestXMinusY) {
                largestXMinusY = xMinusY;
                rightTopCorner = point;
            }
            if(xPlusY > largestXPlusY) {
                largestXPlusY = xPlusY;
                rightBottomCorner = point;
            }
        }
        ArrayList<Point> result = new ArrayList<>();
        result.add(leftTopCorner);
        result.add(rightTopCorner);
        result.add(leftBottomCorner);
        result.add(rightBottomCorner);
        return result;
    }
}
