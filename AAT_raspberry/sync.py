import urllib2
import requests
from storage import Store

'''
	Class for syncing and storing data passed from the runner. 
	Handles all the interfacing with the rest API.
'''
class Sync:

	# Inits sync class. Checks if internet is on. If it is, it will log in as 
	# tutor account and then sync all stored data.
	def __init__(self):
		self.__urlHost = 'https://guestbook-tutorial-148615.appspot.com'
		#self.__urlHost = 'https://guestbook-a.appspot.com'
		self.__loginEndpoint = '/rest/user/login'
		self.__store = Store('store')
		if self.internet_on():
			self.login()
			self.sync_all()


	# Checks to see if a connection can be made to GAE
	def internet_on(self):
		try:
			urllib2.urlopen(self.__urlHost, timeout=5)
			return True
		except urllib2.URLError as err:
			return False

	# Syncs data, if connected to internet. Otherwise it will store data locally
	# Returns true if succcessfuly saved or synced, otherwise it will return false
	def sync_data(self, data):
		# If connected to internet, try to sync right away.
		if self.internet_on():
			status_code = self.sync(data)
			return status_code == 200
		# Otherwise store data for later
		else:
			return self.__store.insert(data)

	# Calls REST API to update student attendance info
	# Returns response code from sync operation.
	def sync(self, data):
		print 'syncing: ', data

		# Check to see if sessionToken has expired
		if self.cookies is None:
			self.login()
		else:
			self.cookies.clear_expired_cookies()
			if 'sessionToken' not in self.cookies: 
				self.login()

		# sync data
		data = data.split(',')
		user = data[0]
		course = data[1]
		group = data[2]
		syncEndpoint = '/rest/user/' + user + '/course/' + course + '/group/' + group + '/attendance'
		data = {'date_week': data[3], 'token': data[4], 'flag_mode': data[5]}
		# Send sync request
		r = requests.put(self.__urlHost + syncEndpoint, data, cookies=self.cookies)
		print r.text
		return r.status_code

	# Syncs all stored data. If any data fails to sync, it will not delete anything
	def sync_all(self):
		print 'Syncing all'
		sync_fail = False
		datastore = self.__store.get()
		# Loop through all stored values. Try to sync each value
		for data in datastore:
			status_code = self.sync(data)
			if status_code != 200:
				print 'Failed to sync', data
				# 409 should only occur when token is invalid or if the date doesn't match. 
				# In both cases the token is invalid and is okay to be deleated.
				if status_code != 409:
					sync_fail = True
				else:
					print 'Invalid QR code information'
		# If successfully able to sync all values, delete them from store.
		if not sync_fail:
			self.__store.delete(len(datastore))

	# Logs into tutor account to allow for tokens to be sent
	def login(self):
		print 'loggin in'
		# Credentials currently hard coded. However they can be set as environment vars
		# and then retreived using os.environ
		r = requests.post(self.__urlHost + self.__loginEndpoint, data={'email':'pi@tum.de', 'password':'tutor'}) 
		self.cookies = r.cookies
