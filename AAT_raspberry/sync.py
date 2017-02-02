import urllib2
import requests
import storage

class Sync:

	# Inits sync class. Checks if internet is on. If it is, it will log in as 
	# tutor account and then sync all stored data.
	def __init__(self):
		self.__urlHost = 'https://guestbook-tutorial-148615.appspot.com'
		self.__loginEndpoint = '/rest/user/login'
		if self.internet_on():
			self.login()
			self.sync_all()


	# Checks to see if a connection can be made to GAE
	def internet_on(self):
		try:
			urllib2.urlopen(self.__urlHost, timeout=1)
			return True
		except urllib2.URLError as err:
			return False

	# Syncs data, if connected to internet. Otherwise it will store data locally
	def sync_data(self, data):
		if self.internet_on():
			status_code = self.sync(data)
			return status_code == 200
		else:
			return storage.insert(data)

	# Calls REST API to update student attendance info
	def sync(self, data):
		print 'syncing: ', data
		self.cookies.clear_expired_cookies()
		if not self.cookies['sessionToken']:
			self.login()

		# sync data
		data = data.split(',')
		user = data[0]
		group = data[1]
		syncEndpoint = '/rest/user/' + user + '/group/' + group + '/attendance'
		data = {'date_week': data[2], 'token': data[3], 'flag_mode': data[4]}
		r = requests.put(self.__urlHost + syncEndpoint, data, cookies=self.cookies)
		return r.status_code

	# Syncs all stored data. If any data fails to sync, it will not delete anything
	def sync_all(self):
		print 'Syncing all'
		sync_fail = False
		datastore = storage.get()
		for data in datastore:
			status_code = self.sync(data)
			if status_code != 200:
				print 'Failed to sync', data
				if status_code != 409:
					sync_fail = True
				else:
					print 'Invalid QR code information'
		if not sync_fail:
			storage.delete(len(datastore))

	# Logs into tutor account to allow for tokens to be sent
	def login(self):
		print 'loggin in'
		r = requests.post(self.__urlHost + self.__loginEndpoint, data={'email':'tutor@tutor.com', 'password':'tutor'})
		self.cookies = r.cookies
