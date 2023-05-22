def mergeDictionary(dict_1, dict_2):
    """
    Une diccionarios de formato correcto
    dict1   {0: [2,4], 1: [5]}
    dict2   {0: [3], 2: [5]}
    ret     {0: [2,4,3], 1: [5], 2: [5]}
    """
    dict_3 = {**dict_1, **dict_2}
    for key, value in dict_3.items():
        if key in dict_1 and key in dict_2:
            dict_3[key] = value + dict_1[key]
    return dict_3

def format_line (line):
    """
    Convierte de formato correcto [num, {}] a línea para el archivo .gz
    line    [0, {0: [2,3,4], 1: [5]}]
    ret     "0 0.2.3.4 1.5\n".encode()
    """
    id_line_local = line[0]
    answer = [str(id_line_local)]
    for edge in line[1].keys():
        nums_string = list(map (lambda x: str(x), line[1][edge]))
        nums_join = '.'.join([str(edge)] + nums_string)
        answer.append(nums_join)
    last = ' '.join(answer) + '\n'
    return last.encode()

def line_convert (line):
    """
    Convierte texto de línea a formato correcto [num, {}]
    line    "0 0.2.3.4 1.5"
    ret     [0, {0: [2,3,4], 1: [5]}]
    """
    line_to = []
    if line != '':
        line_to = line.split(' ')
        id_line = int(line_to[0])
        rs = {}
        for val in line_to[1:]:
            val_split = list(map(lambda x: int(x),val.split('.')))
            rs[val_split[0]] = val_split[1:]
        line_to = [id_line,rs]
    return line_to

def structToText(key, value):
	"""
	Convierte una estructura {1 : [2,3]} a una lista que se escribirá después
	key		1
	value	[2,3]
	ret		[1, '2.3']
	"""
	groups = list(map(lambda x: '.'.join( list(map(lambda y: str(y),x)) ),value))
	return [key] + groups
