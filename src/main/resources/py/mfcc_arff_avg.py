import os
import numpy as np

path = os.path.join(os.getcwd(), 'arff')
everything = os.listdir(path)
np.set_printoptions(precision=2, suppress=True)

for _file in everything:
	current_file = os.path.join(path, _file)
	if os.path.isfile(current_file):
		mfcc_sum = np.zeros(13)
		n_lines = 0
		with open(current_file) as f:
			for line in f:
				try:
					x = np.array(line.split(','))
					x = x.astype(np.float)
					mfcc_sum = np.add(mfcc_sum, x)
					n_lines += 1
				except: continue
		print 'file: ', _file, '. result: ', str(np.average(mfcc_sum/n_lines))




		