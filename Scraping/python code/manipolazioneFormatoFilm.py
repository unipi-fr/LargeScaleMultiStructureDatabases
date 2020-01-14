
import csv
import datetime
from random import randint
import string

def remove_non_printable(s):
    return ''.join(c for c in s if c in string.printable)


def parse_date(text):
    for frm in ("%d/%m/%Y", "%Y-%m-%d"):
        try:
            return datetime.datetime.strptime(text, frm)
        except ValueError:
            pass
    print(text)
    raise ValueError("Non Funziona nulla!!!!")



with open('../Data/wiki_movies.tsv', encoding="utf8") as tsvfile:
    reader = csv.reader(tsvfile, dialect='excel-tab')

    lista = []

    for row in reader:
        lista.append(row)

for i in range(len(lista)):
    if i == 0:
        continue
    if lista[i][2] == "Unknown": 
        lista[i][2] = ''
    
    if lista[i][3] == "Unknown": 
        lista[i][3] = ''
    if lista[i][4] == "unknown": 
        lista[i][4] = ''
    
    #descrizione = lista[i][5].translate({ord(i): None for i in '\n'})
    #lista[i][5] = descrizione

with open('../Data/wiki_movies_stripped.tsv', mode='w' , newline = '', encoding="utf8") as movies_dateISO:
    writer = csv.writer(movies_dateISO, dialect='excel-tab')

    writer.writerows(lista)

