import gzip
def make_file ():
    #open("test.")
    with gzip.open('ttt.nt.gz','wb') as f:
        f.write(b" Hola que tal\n")

make_file()
