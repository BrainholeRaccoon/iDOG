import serial
import time
import string
from picamera import PiCamera
import pyodbc
import imgproc
from numpy import *
import re
from PIL import Image
from socket import *
import struct
import io

eating_status = 0
flip = False
conn = pyodbc.connect('DRIVER=FreeTDS;SERVER=172.20.10.11;PORT=1433;DATABASE=acc;UID=sa;PWD=123456')
cursor = conn.cursor()
ser = serial.Serial('/dev/ttyACM0',9600)
ser.write('c')
user_defined_table = zeros([20,4])
user_defined_init_flag = 0
previous_mi = 0
alreadyfeed = 0
mode = 0
previous_weight  = 0

eating_status = 0
eating_start = 0
idx = 0

camera = PiCamera()

weight = 0
ser.write('c')

def send_rate():
    global previous_weight
    global eating_status
    global eating_start
    global weight
    global idx

    
    time.sleep(5)
    now_weight = read_weight()
    if previous_weight  - now_weight  >   5:
        if eating_status == 0:
            weight = previous_weight
        else:
            pass
        eating_status = 1
        dogidx = capture(5)
        idx = 0
        eating_start = time.time()
        rate  = abs(now_weight-previous_weight)/5
        now = time.localtime(time.time())
        now_ho = now.tm_hour  
        now_mi = now.tm_min
        writestr = "insert into zsffood(hour,min,rate) values(%s,%s,%s)" %(now_ho,now_mi,rate) 
        cursor.execute(writestr)
        conn.commit()
    else:
        if previous_weight - now_weight < 2 and eating_status == 1:
            idx = idx + 1
            if idx > 7:
                eating_period = int(time.time() - eating_start)
                eating_amount = weight - read_weight()
                writestr = "insert into zsfdog(time,weight,no) values(%s,%s,%s)" %(eating_period,eating_amount,dogidx)
                cursor.execute(writestr)
                conn.commit()
                eating_status = 0
            else:
                pass
        else:
            pass
    previous_weight = now_weight
    
def feed(amount):
    ser.write('f')
    ser.write(str(amount))

def test(x):
    return x

def convert(stri):
    y = filter(str.isdigit,stri)
    z = int(y)
    return z

def read_weight():
    ser.write('w')
    weight  =  ser.readline()
    y  = filter(str.isdigit,weight)
    w =  int(y)
    return w

def checkmode():
    readstr = 'select mode from zsfmode'
    cursor.execute(readstr)
    rows = cursor.fetchall()
    a = rows[0].mode
    mode = convert(a)
    return mode

def split_time(time):
    s = re.split(r':',time)
    hour = convert(s[0])
    minute = convert(s[1])
    return hour,minute

def feed(amount):
    ser.write('f')
    ser.write(str(amount))

    
def ismember(element,array):
    length = len(array)
    for ind in range(0,length):
        if element == array[ind]:
            return True
    return False

def execute(command):
    if command == "on":
        print "MOTOR ON"
        ser.write('q')
    if command == "off":
        print "MOTOR OFF"
        ser.write('w')
    if command == "two":
        print "mode change to user_defined"
        writestr = "UPDATE zsfmode SET mode=2"
        cursor.execute(writestr)
        conn.commit()
    if command == "three":
        print "mode change to auto"
        writestr = "UPDATE zsfmode SET mode=3"
        cursor.execute(writestr)
        conn.commit()

def wireless_control_mode():
    while True:
        conn,addr = s.accept()
        print('Connected by:',addr)
        while True:
            command = conn.recv(1024).replace('\n','')
            if not command:
                break
            command = filter(str.isalpha,command)
            execute(command)
        print "executed"
        conn.close()
        break

def user_defined_mode():
    global user_defined_init_flag
    global alreadyfeed
    global previous_mi
    print(user_defined_init_flag)
    if user_defined_init_flag == 0:
        readstr = 'select time,food,no from zsf'
        cursor.execute(readstr)
        rows = cursor.fetchall()
        length = len(rows)
        for row in range(0,length):
            ho,mi = split_time(rows[row].time)
            user_defined_table[row] = [ho,mi,rows[row].food,rows[row].no]
        user_defined_init_flag = 1
    else:
        pass
    now = time.localtime(time.time())
    now_ho = now.tm_hour
    now_mi = now.tm_min
    print(previous_mi)
    if now_mi != previous_mi:
        alreadyfeed = 0
        user_defined_init_flag = 0
    previous_mi = now_mi
    for ind in range(0,len(user_defined_table)):
        if now_ho == user_defined_table[ind][0] and now_mi == user_defined_table[ind][1] and alreadyfeed == 0:
            feed_amount = user_defined_table[ind][2]
            print(feed_amount)
            feed(feed_amount)
            alreadyfeed = 1
        else:
            pass

def capture(t):
    global camera
    client_socket = socket()
    connection = client_socket.makefile('wb')
    camera.resolution = (320,240)
    client_socket.connect(('172.20.10.9',8000))
    stream = io.BytesIO()
    start = time.time()
    for foo in camera.capture_continuous(stream,'jpeg'):
        connection.write(struct.pack('<L',stream.tell()))
        connection.flush()
        stream.seek(0)
        connection.write(stream.read())
        stream.seek(0)
        dogidx = client_socket.recv(1024).replace('\n','')
        print "dog index is"
        print dogidx
        if time.time() - start > t:
            break
        stream.seek(0)
        stream.truncate()
    connection.write(struct.pack('<L',0))
    connection.close()
    client_socket.close()
    return int(dogidx)

def auto_mode():
    ser.write('d')
    time.sleep(0.5)
    if ser.read() == '1' and (image_captured == 0):
        camera.capture('ball.jpg')
        image_captured = 1
        im = Image.open('ball.jpg')
        color = imgproc.property(im)
    else:                                                                                                                                                                                                                                                                                                                                                                                                          
        pass

HOST = '172.20.10.10'
PORT = 8888
s = socket(AF_INET,SOCK_STREAM)
s.bind((HOST,PORT))
s.listen(1)
ser.write('c')
clearflage  = 0

while True:
    if  clearflage     ==  0 :
        ser.write('c')
        clearflag  =  1
    else:
        pass
    mode = checkmode()
    print(mode)
    if mode == 1:
        wireless_control_mode()
    if mode == 2:
        user_defined_mode()
    if mode == 3:
        x = capture(30)
        print x
    send_rate()
        
