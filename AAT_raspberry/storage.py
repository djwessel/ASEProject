'''
	Class for handling the storage of data. Each value is stored as a separate row in a file [filename]
	Assumes that the stored data is a string all on one line.
'''
class Store:

	def __init__(self, filename):
		self.filename = filename
		open(self.filename, 'a').close() # touches file to make sure it exists.

	# inserts val to end of store file
	def insert(self, val):
		with open(self.filename, 'a') as f:
			f.write(val + '\n')
		return True

	# gets all stored values in store file
	def get(self):
		with open(self.filename, 'r') as f:
			return map(str.strip, f.readlines())

	# deletes first [numLines] number of values from store file
	def delete(self, numLines):
		lines = []
		with open(self.filename, 'r') as f:
			lines = f.readlines()

		with open(self.filename, 'w') as f:
			f.writelines(lines[numLines:])
