#!/bin/bash

if [[ $# -lt 4 ]]; then
    echo "numero argomenti errato, devono essere almeno 4: origine, log, err, lista1, ... ,listaN"; exit 1
fi

origine="$1"
log="$2"
err="$3"
recursion=rec.sh

shift 3

# controllo $origine
if [[ "$origine" != /* ]]; then
    echo "$origine e' non e' un path assoluto"; exit 2
elif [[ ! -r "$origine" || ! -f "$origine" ]]; then
    echo "$origine non è un file esistente con permessi di lettura"; exit 3
elif [[ "$origine" != *".txt" ]]; then
    echo "$origine non è un file che termina per .txt"; exit 4
fi


# controllo log
if [[ "$log" != /* || "$log" != */* ]]; then
    echo "$log deve essere o assoluto o relativo"; exit 5
elif [[ ! -w "$log" || ! -f "$log" ]]; then
    echo "$log non è un file esistente con permessi di scrittura"; exit 6
fi

# controllo err
if [[ "$err" != /* || "$err" != */* ]]; then
    echo "$err deve essere o assoluto o relativo"; exit 7
elif [[ -f "$err" && ! -w "$err" ]]; then
    echo "$err se esiste deve avere permessi di scrittura"; exit 8
elif [[ ! -w $(dirname $err) ]]; then
    echo "non si hanno diritti di scrittura per creare $err"
    exit 9
fi

# creo/sovrascrivo file $err
> "$err"



shift 3

# controllo list
for list in "$@"; do
    if [[ ! -f "$list" ]]; then
        echo "$list e' non e' un file esistente!"; exit 10
    elif [[ ! -r "$list" ]]; then
        echo "$list non ha permessi di scrittura"; exit 11
    elif [[ "$list" != /* ]]; then
        echo "$list deve essere un path assoluto"; exit 12
    fi
done


for list in "$@"; do
    for line in "$(cat "$list")"; do

        if [[ ! -d "$line" ]]; then
            echo "cartella $line non esistente" >> "$err"

        elif [[ ! -w "$line" ]]; then
            echo "non ho permessi di scrittura su $line" >> "$err"
        
        else             
            cp "$origine" "$riga" 
            echo "$(ls "$riga")" >> "$log"
    done
done
