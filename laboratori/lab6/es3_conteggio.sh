#!/bin/bash

if [[ $# != 3 || ! "$1" =~ ^[0-9]+$ ]]; then
	echo "errore argomenti, devono essere 3, di cui il primo un intero positivo"; exit 1
fi

M=$1
S=$2
filedir="$3"
if [[ "$filedir" != /* || ! -r "$filedir" ]]; then
	echo "il terzo argomento deve essere un path assoluto, leggibile ed esistente"; exit 1
fi

#ciclo le linee del file, ognuna delle quale è un direttorio
for dir in $(cat "$filedir"); do

    #per ogni file nel direttorio
    for f in $dir/*; do

        #salto se sono direttori
        if [[ -d "$f" ]]; then continue; fi
        
        #se le occorrenze di S nel file f sono > di M, allora stampo la sua dimensione in bytes
        S_occ=$(grep -c $S "$f")
        if [[ $S_occ -gt $M ]]; then
            echo "Il file $f nella directory $dir contiene $(stat -c %s $f) caratteri"
        fi
    done
done

