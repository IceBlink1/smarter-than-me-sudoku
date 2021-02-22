package digitRecognition;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class GridLinesRemover {
    public static Mat removeGridLines(Mat grid, Mat lines) {
        double HORIZONTAL_LINE_POSITIONS = grid.height() / 9.0;
        double HORIZONTAL_MIN_THRESHOLD = (HORIZONTAL_LINE_POSITIONS / 100.0) * 15.0;
        double HORIZONTAL_MAX_THRESHOLD = (HORIZONTAL_LINE_POSITIONS / 100.0) * 85.0;

        double VERTICAL_LINE_POSITIONS = grid.width() / 9.0;
        double VERTICAL_MIN_THRESHOLD = (VERTICAL_LINE_POSITIONS / 100.0) * 15.0;
        double VERTICAL_MAX_THRESHOLD = (VERTICAL_LINE_POSITIONS / 100.0) * 85.0;

        double HORIZONTAL_SAME_LINE = (grid.height() / 100.0) * 2.0;
        double VERTICAL_SAME_LINE = (grid.width() / 100.0) * 2.0;

        int LINE_THICKNESS = (int)((grid.width() / 100.0) * 1.0);

        for (int i = 0; i < lines.rows(); ++i){
            double[] vec = lines.get(i, 0);
            double x1 = vec[0];
            double y1 = vec[1];
            double x2 = vec[2];
            double y2 = vec[3];
            Point startLine = new Point(x1, y1);
            Point endLine = new Point(x2, y2);

            boolean isHorizontal = Math.abs(startLine.y - endLine.y) <
                    HORIZONTAL_SAME_LINE;
            boolean isVertical = Math.abs(startLine.x - endLine.x) <
                    VERTICAL_SAME_LINE;
            if (isHorizontal &&
                    (startLine.y % HORIZONTAL_LINE_POSITIONS <=
                        HORIZONTAL_MIN_THRESHOLD ||
                    startLine.y % HORIZONTAL_LINE_POSITIONS >=
                        HORIZONTAL_MAX_THRESHOLD)) {
                startLine.x = 0.0;
                endLine.x = grid.width();
            } else if (isVertical &&
                    (startLine.x % VERTICAL_LINE_POSITIONS <=
                        VERTICAL_MIN_THRESHOLD ||
                    startLine.x % VERTICAL_LINE_POSITIONS >=
                        VERTICAL_MAX_THRESHOLD)) {
                startLine.y = 0.0;
                endLine.y = grid.height();
            } else {
                startLine.x = 0.0;
                startLine.y = 0.0;
                endLine.x = 0.0;
                endLine.y = 0.0;
            }
            Imgproc.line(grid, startLine, endLine, new Scalar(0.0, 0.0,0.0),
                    LINE_THICKNESS);
        }
        return grid;
    }
}
