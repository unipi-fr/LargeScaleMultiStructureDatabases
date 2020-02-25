
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

with open('UsersUnici.tsv', encoding="utf8") as tsvfile:
    reader = csv.reader(tsvfile, dialect='excel-tab') 

    for row in reader:
        listaUsername.append(row[0])

with open('FilmUnici.tsv', encoding="utf8") as tsvfile:
    reader = csv.reader(tsvfile, dialect='excel-tab') 

    for row in reader:
        listaFilms.append(row[0])


listaDaScrivere = []

for username in listaUsername:
    numeroSeguiti = randint(0,10)
    if numeroSeguiti == 0:
        continue
    estraiSeguiti(username, numeroSeguiti, listaFilms, listaDaScrivere)



with open('FollowFilms.tsv', mode='w' , newline = '', encoding="utf8") as movies:
    writer = csv.writer(movies, dialect='excel-tab')

    for roba in listaDaScrivere:
        writer.writerow(roba)


