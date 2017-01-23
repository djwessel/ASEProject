
from  SimpleCV import Color,Camera,Display

cam = Camera()  #starts the camera
 
img = cam.getImage() #gets image from the camera
barcode = img.findBarcode() #finds barcode data from image
while(barcode is None):
	print "Scanning"
	img = cam.getImage() #gets image from the camera
	barcode = img.findBarcode() #finds barcode data from image
			
barcode = barcode[0]
result = str(barcode.data)
print result	 #prints result of barcode in python shell

