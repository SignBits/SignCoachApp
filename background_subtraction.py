from __future__ import print_function
import cv2 as cv

def subtractBackground(inFileLoc: str, outFileLoc: str, varThreshold = 16, detectShadows=False):
    backSub = cv.createBackgroundSubtractorMOG2(varThreshold=varThreshold, detectShadows=detectShadows)
    capture = cv.VideoCapture(inFileLoc)

    width = int(capture.get(cv.CAP_PROP_FRAME_WIDTH))
    height = int(capture.get(cv.CAP_PROP_FRAME_HEIGHT))
    fps = int(capture.get(cv.CAP_PROP_FPS))

    out = cv.VideoWriter(outFileLoc, cv.VideoWriter_fourcc(*'XVID'), fps, (width, height))

    if not capture.isOpened():
        raise FileNotFoundError('Unable to open: ' + 'video0.mp4')

    while True:
        ret, frame = capture.read()

        if frame is None:
            break

        fgMask = backSub.apply(frame)
        out.write(fgMask)

subtractBackground(inFileLoc='videos/0.mp4', outFileLoc='bs_videos/0.mp4')

# backSub = cv.createBackgroundSubtractorMOG2(varThreshold=4, detectShadows=False)
#
# capture = cv.VideoCapture(cv.samples.findFileOrKeep('videos/0.mp4'))
# if not capture.isOpened:
#     print('Unable to open: ' + 'video0.mp4')
#     exit(0)
#
# while True:
#     ret, frame = capture.read()
#     if frame is None:
#         break
#
#     fgMask = backSub.apply(frame)
#
#     cv.imshow('Frame', frame)
#     cv.imshow('FG Mask', fgMask)
#
#     keyboard = cv.waitKey(30)
#     if keyboard == 'q' or keyboard == 27:
#         break
