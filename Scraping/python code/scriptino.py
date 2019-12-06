
import csv

with open('dataTitleBasics.tsv', encoding="utf8") as tsvfile:
    reader = csv.reader(tsvfile, dialect='excel-tab')
  
    lista = []

    for row in reader:
        lista.append(row)

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
    
