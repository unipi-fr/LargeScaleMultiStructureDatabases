
import csv
from random import randint
import string


def estraiSeguiti(seguitore, quanti, lista, scrivi):
    
    listaEstratti = []
    for i in range(0,quanti):
        while True:
            indice = randint(0, len(lista)-1)
            estratto = lista[indice]
            if estratto in listaEstratti:
                continue
            listaEstratti.append(estratto)
            scrivi.append([seguitore, estratto])
            break



listaUsername = []
listaFilms = []
listaPost = []

with open('UsersUnici.tsv', encoding="utf8") as tsvfile:
    reader = csv.reader(tsvfile, dialect='excel-tab') 

    for row in reader:
        listaUsername.append(row[0])

with open('FilmUnici.tsv', encoding="utf8") as tsvfile:
    reader = csv.reader(tsvfile, dialect='excel-tab') 

    for row in reader:
        listaFilms.append(row[0])

with open('TestoPost.tsv', encoding="utf8") as tsvfile:
    reader = csv.reader(tsvfile, dialect='excel-tab') 

    for row in reader:
        listaPost.append(row[0])


listaDaScrivere = []


for post in listaPost:
    indiceU = randint(0,len(listaUsername)-1)
    indiceF = randint(0,len(listaFilms)-1)
    utente = listaUsername[indiceU]
    film = listaFilms[indiceF]
    listaDaScrivere.append([utente, film, post])


with open('PostScritti.tsv', mode='w' , newline = '', encoding="utf8") as posts:
    writer = csv.writer(posts, dialect='excel-tab')

    for roba in listaDaScrivere:
        writer.writerow(roba)


