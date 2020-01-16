
import csv
import datetime
from random import randint
import string
import dateutil.parser




def remove_non_printable(s):
    return ''.join(c for c in s if c in string.printable)


def parse_date(text):
    
    try:
        return dateutil.parser.parse(text).year
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
    data = 
    lista[i][0] = anno

with open('../Data/wiki_movies_stripped.tsv', mode='w' , newline = '', encoding="utf8") as movies_dateISO:
    writer = csv.writer(movies_dateISO, dialect='excel-tab')

    writer.writerows(lista)

