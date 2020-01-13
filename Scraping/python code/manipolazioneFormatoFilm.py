
import csv
import datetime
from random import randint

def parse_date(text):
    for frm in ("%d/%m/%Y", "%Y-%m-%d"):
        try:
            return datetime.datetime.strptime(text, frm)
        except ValueError:
            pass
    print(text)
    raise ValueError("Non Funziona nulla!!!!")



with open('../Data/movies.tsv', encoding="utf8") as tsvfile:
    reader = csv.reader(tsvfile, dialect='excel-tab')

    lista = []

    for row in reader:
        lista.append(row)

for i in range(len(lista)):
    if i == 0:
        continue
    if lista[i][6] == '':
        lista[i][6] = datetime.date(randint(1899,2015), randint(1,12),randint(1,28)).isoformat()
    else:
        data = parse_date(lista[i][6])
        lista[i][6] = data.isoformat()

with open('../Data/movies_dateISO.tsv', mode='w' , newline = '', encoding="utf8") as movies_dateISO:
    writer = csv.writer(movies_dateISO, dialect='excel-tab')

    writer.writerows(lista)

'''
salvo in una lista tutte le righe

faccio un for che ignora la prima riga e modifica la stringa in posizione 6 con la isodate

scrivo tutte le righe in un nuovo file tsv


    lista = []
    i = 0
    for row in reader:
        if i == 0:
            i+= 1
            continue
        dataConvertita = datetime.datetime.strptime(row[6], "%d/%m/%Y")
        print(str(dataConvertita) + "\t" + str(dataConvertita.isoformat()))
        if i == 5:
            break
        i += 1

i = 1
lista_senza_duplicati = []

while i < len(lista):
    id_corrente = lista[i][0]
    id_precedente = lista[i-1][0]

    if id_corrente != id_precedente:
        lista_senza_duplicati.append(lista[i])
    i += 1

with open('InfoTitoliPuliti.tsv', mode='w' , newline = '') as titoli_file:
    titoli_writer = csv.writer(titoli_file, dialect='excel-tab')

    titoli_writer.writerows(lista_senza_duplicati)





'''