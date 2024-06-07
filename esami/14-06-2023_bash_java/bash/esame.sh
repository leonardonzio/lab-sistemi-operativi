#!/bin/bash

if [[ $# -ne 2 ]]; then
    echo "numero argomenti errato, devono essere 2: IN, OUT"; exit 1
fi

IN="$1"
OUT="$2"
recursion=rec.sh

for dir in "$@"; do
    if [[ "$dir" != /* ]]; then
        echo "$dir non e' un path assoluto"; exit 2
    elif [[ ! -d "$dir" ]]; then
        echo "$dir non è un direttorio esistente"; exit 3
    fi
done


# ::::::::::ricorsione:::::::::
if [[ "$0" = /* ]]; then
    
    #se $0 è un path assoluto
    dir_name=$(dirname "$0")
    recursive_cmd="$dir_name/$recursion"
elif [[ "$0" = */* ]]; then
    
    #se c'è uno slash, ma non inizia con / --> $0 è un path relativo
    dir_name=$(dirname "$0")
    recursive_cmd="$(pwd)/$dir_name/$recursion"
else
    #non si tratta nè di un path relativo, nè di un assoluto,
    #il comando $0 sarà cercato in $PATH.
    recursive_cmd="$recursion"
fi


echo "invocazione comando ricorsivo su $IN"
"$recursive_cmd" "$IN" "$OUT"

