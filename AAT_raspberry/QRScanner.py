import qrtools

def scan():
	print("Start scanning")
	qr = qrtools.QR()
	while (True) :
		print("scanning...")
		qr.decode_webcam()
		if (qr.data != u'NULL') :
			break
	return qr.data

