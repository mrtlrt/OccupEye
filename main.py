import cv2
import numpy as np
import firebase_admin
from firebase_admin import credentials, db
import time


#Firebase stuff
cred = credentials.Certificate("key.json")
firebase_admin.initialize_app(cred,{

    'databaseURL':'https://occupeye-dedb8-default-rtdb.asia-southeast1.firebasedatabase.app/'
})





# def throttle(delay):
#     """
#     Decorator that limits the rate at which a function can be called.
#     """
#     def decorator(func):
#         last_called = 0
        
#         def wrapper(*args, **kwargs):
#             nonlocal last_called
#             elapsed = time.time() - last_called
#             remaining = delay - elapsed
            
#             if remaining > 0:
#                 time.sleep(remaining)
            
#             result = func(*args, **kwargs)
#             last_called = time.time()
#             return result
        
#         return wrapper
    
#     return decorator

# @throttle(2)
def sendData(users):
    ref = db.reference('/').child('Locations').child('Hostel').child('Block 55').child('Test Room')
    ref.child('Users').set(users)
    ref = db.reference('/').child('Locations').child('Hostel').child('Block 55').child('Test Room')
    if users == 0:
        ref.child("Colour Grading").set("#CBE896")
    elif users >= 3:
        ref.child("Colour Grading").set("#DB5461")
    else: 
        ref.child("Colour Grading").set("#FDE74C")


lastCalled = 0

## Object detection stuff
cap = cv2.VideoCapture('https://10.16.149.238:8080/video')
net = cv2.dnn.readNetFromONNX("yolov5n.onnx")
file = open("coco.txt","r")
classes = file.read().split('\n')
print(classes)


while True:
    img = cap.read()[1]
    if img is None:
        break
    img = cv2.resize(img, (1000,600))
    blob = cv2.dnn.blobFromImage(img,scalefactor= 1/255,size=(640,640),mean=[0,0,0],swapRB= True, crop= False)
    net.setInput(blob)
    detections = net.forward()[0]
  

    # cx,cy , w,h, confidence, 80 class_scores
    # class_ids, confidences, boxes

    classes_ids = []
    confidences = []
    boxes = []
    rows = detections.shape[0]

    img_width, img_height = img.shape[1], img.shape[0]
    x_scale = img_width/640
    y_scale = img_height/640



    for i in range(rows):
        row = detections[i]
        confidence = row[4]
        if confidence > 0.5:
            classes_score = row[5:]
            ind = np.argmax(classes_score)
            if classes_score[ind] > 0.5:
                classes_ids.append(ind)
                confidences.append(confidence)
                cx, cy, w, h = row[:4]
                x1 = int((cx- w/2)*x_scale)
                y1 = int((cy-h/2)*y_scale)
                width = int(w * x_scale)
                height = int(h * y_scale)
                box = np.array([x1,y1,width,height])
                boxes.append(box)


    indices = cv2.dnn.NMSBoxes(boxes,confidences,0.5,0.5)

    
    people = 0
    for i in indices:
        x1,y1,w,h = boxes[i]
        label = classes[classes_ids[i]]
        if (label=="person"):
            people += 1
        conf = confidences[i]
        text = label + "{:.2f}".format(conf)
        cv2.rectangle(img,(x1,y1),(x1+w,y1+h),(255,0,0),2)
        cv2.putText(img, text, (x1,y1-2),cv2.FONT_HERSHEY_COMPLEX, 0.7,(255,0,255),2)


    elapsed = time.time() - lastCalled
    remaining = 1 - elapsed
    if remaining > 0:
        pass
    else:
        sendData(people)
        lastCalled = 0

    cv2.imshow("VIDEO",img)
    k = cv2.waitKey(10)
    if k == ord('q'):
        break