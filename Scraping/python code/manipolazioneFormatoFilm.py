
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



with open('../Data/wiki_movies.tsv', encoding="utf8") as tsvfile:
    reader = csv.reader(tsvfile, dialect='excel-tab')

    lista = []

    for row in reader:
        lista.append(row)

for i in range(len(lista)):
    if i == 0:
        continue
    data = parse_date(lista[i][0])
    lista[i][0] = data.isoformat()

with open('../Data/wiki_movies_dateISO.tsv', mode='w' , newline = '', encoding="utf8") as movies_dateISO:
    writer = csv.writer(movies_dateISO, dialect='excel-tab')

    writer.writerows(lista)

