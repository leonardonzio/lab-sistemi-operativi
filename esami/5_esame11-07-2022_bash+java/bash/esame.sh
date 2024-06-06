#!/bin/bash

if [[ $# -lt 3 ]]; then
    echo "numero argomenti errato, devono essere almeno 4: stringa, outdir, dir1, ... ,dirN"; exit 1
fi

stringa="$1"
outDir="$2"
recursion=rec.sh

shift

for dir in "$@"; do
    if [[ "$dir" != /* ]]; then
        echo "$dir e' non e' un path assoluto"; exit 2
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

report=report.$$.out
reportAbs="$outDir"/$report
> "$reportAbs"

shift

for dir in "$@"; do
    echo "invocazione comando ricorsivo su $dir"
    "$recursive_cmd" "$stringa" "$reportAbs" "$dir"
done

