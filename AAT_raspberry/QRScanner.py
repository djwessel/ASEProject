from  SimpleCV import Color,Camera,Display

def scan():
  cam = Camera()  #starts the camera
 
  img = cam.getImage() #gets image from the camera
  barcode = img.findBarcode() #finds barcode data from image
  while(barcode is None):
	  img = cam.getImage() #gets image from the camera
	  barcode = img.findBarcode() #finds barcode data from image
			
  barcode = barcode[0]
  result = str(barcode.data)
  return result	 #prints result of barcode in python shell

