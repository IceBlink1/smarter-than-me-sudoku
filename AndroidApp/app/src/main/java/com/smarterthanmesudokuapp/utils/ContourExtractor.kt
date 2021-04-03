package com.smarterthanmesudokuapp.utils

import org.opencv.core.Point

class ContourExtractor {
    fun identifyCorners(contourPoints : Array<Point>): Array<Point> {
        val leftTopCorner = Point()
        val rightTopCorner = Point()
        val leftBottomCorner = Point()
        val rightBottomCorner = Point()

        var xplusy : Double
        var xminusy : Double

        var smallestxplusy = 10000000.0
        var smallestxminusy = 10000000.0
        var largestxminusy = 0.0
        var largestxplusy = 0.0

        for (counter in contourPoints.indices) {
            xplusy = contourPoints[counter].x + contourPoints[counter].y
            xminusy = contourPoints[counter].x - contourPoints[counter].y

            //finding left top corner - smallest x+y value
            if (xplusy < smallestxplusy) {
                smallestxplusy = xplusy
                leftTopCorner.x = contourPoints[counter].x
                leftTopCorner.y = contourPoints[counter].y
            }

            //finding left bottom corner - smallest x-y value
            if (xminusy < smallestxminusy) {
                smallestxminusy = xminusy
                leftBottomCorner.x = contourPoints[counter].x
                leftBottomCorner.y = contourPoints[counter].y
            }

            //finding right top corner - largest x-y value
            if (xminusy > largestxminusy) {
                largestxminusy = xminusy
                rightTopCorner.x = contourPoints[counter].x
                rightTopCorner.y = contourPoints[counter].y
            }

            //finding right bottom corner - largest x+y value
            if (xplusy > largestxplusy) {
                largestxplusy = xplusy
                rightBottomCorner.x = contourPoints[counter].x
                rightBottomCorner.y = contourPoints[counter].y
            }
        }

        return arrayOf(leftTopCorner, rightTopCorner, leftBottomCorner, rightBottomCorner)
    }
}