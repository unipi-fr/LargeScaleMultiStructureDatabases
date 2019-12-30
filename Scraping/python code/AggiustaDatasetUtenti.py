
import csv

with open('../Data/UsersUnpolished.csv') as csv_file:
    csv_reader = csv.reader(csv_file, delimiter=',')
    with open("../Data/UsersPolished.csv", mode="w") as csv_file_write:
        csv_writer = csv.writer(csv_file_write, delimiter = ',', quoting=csv.QUOTE_ALL)
        for row in csv_reader:
            csv_writer.writerow(row)

            


