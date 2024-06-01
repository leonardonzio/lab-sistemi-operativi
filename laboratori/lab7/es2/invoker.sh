#!/bin/bash
if [[ $# -lt 3 ]]; then
    echo "numero argomenti errato, devono essere 2: dir Fout"; exit 1
fi

str="$1"
ext="$2"
recursion=hit.sh

shift 2

for dir in "$@"; do
    if [[ "$dir" != /* ]]; then
        echo "path non assoluto"; exit 2
    elif [[ ! -d "$dir" ]]; then
        echo "non è un direttorio"; exit 3
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

> "$HOME"/report.log
#attivo comando ricorsivo
for dir in "$@"; do
    echo "invocazione comando ricorsivo su $dir"
    "$recursive_cmd" "$str" "$ext" "$dir" $$
done

echo "Trovati $(cat "$HOME/report.log" | grep $$ | wc -l) file con le caratteristiche date"

