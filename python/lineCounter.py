import gzip
from itertools import islice

line_counter = 0
line_added = 0

with gzip.open('latest-truthy.nt.gz','r') as f:
	for line in f:
		line_decode = line.decode()
		line_split = line_decode.split(" ")
		if "<http://www.wikidata.org/entity/Q" in line_split[0] and \
			"<http://www.wikidata.org/prop/direct/P" in line_split[1] and \
			"<http://www.wikidata.org/entity/Q" in line_split[2]:
				cache_file.write(line_decode)
				line_added += 1		
		line_counter += 1

print(f"Total {line_counter} lineas")
print(f"Total {line_added} lineas agregadas")