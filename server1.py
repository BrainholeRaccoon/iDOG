import io
import socket
import struct
from PIL import Image
import time
import cv2
import numpy as np


lower_yellow = np.array([20,100,0])
upper_yellow = np.array([30,255,255])

lower_blue = np.array([100,100,0])
upper_blue = np.array([130,255,255])


# Start a socket listening for connections on 0.0.0.0:8000 (0.0.0.0 means
# all interfaces)
server_socket = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
server_socket.bind(('0.0.0.0', 8000))
server_socket.listen(1)
def listen():
    print "trying to connect"
    conn,addr = server_socket.accept()
    connection = conn.makefile('rb')
    print addr
    while True:
        image_len = struct.unpack('<L',connection.read(struct.calcsize('<L')))[0]
        if not image_len:
            break
        image_stream = io.BytesIO()
        image_stream.write(connection.read(image_len))
        image_stream.seek(0)
        image = Image.open(image_stream)
        print('Image is %d*%d' % image.size)
        a = np.array(image)
        cv_im = a[:,:,::-1].copy()
        cv_im_hsv = cv2.cvtColor(cv_im,cv2.COLOR_BGR2HSV)
        mask_yellow = cv2.inRange(cv_im_hsv,lower_yellow,upper_yellow)
        mask_blue = cv2.inRange(cv_im_hsv,lower_blue,upper_blue)
        cv2.imshow('original image',cv_im)
        cv2.imshow('mask yellow',mask_yellow)
        cv2.imshow('mask blue',mask_blue)
        print np.sum(mask_yellow)
        print np.sum(mask_blue)
        if np.sum(mask_yellow) > np.sum(mask_blue) and np.sum(mask_yellow)>10000:
            dog = 1
        else:
            if np.sum(mask_blue) > np.sum(mask_yellow) and np.sum(mask_blue) > 10000:
                dog = 2
            else:
                dog = 1000
        conn.send(str(dog))
        cv2.waitKey(50)
        image.verify()
        print('Image is verified')
    connection.close()
    print "conntction closed"

while True:
    listen()
