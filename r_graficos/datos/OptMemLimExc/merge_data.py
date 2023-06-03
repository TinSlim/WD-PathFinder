import glob
import pandas as pd

csv_files = glob.glob('*.{}'.format('csv'))
print(csv_files)
ans = input("Write 'merge' to merge and make out.csv: ")
if ans == "merge":
	df_csv_concat = pd.concat([pd.read_csv(file) for file in csv_files ], ignore_index=True)
	df_csv_concat.to_csv('out.csv', index=False)