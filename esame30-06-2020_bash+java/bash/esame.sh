#!/bin/bash

if [[ $# -lt 4 ]]; then
    echo "numero argomenti errato, devono essere almeno 4: fileToSearch, S, M, dir1, ... ,dirN"; exit 1
fi

fileToSearch="$1"
S="$2"
M="$3"
recursion=rec.sh

shift 3

if [[ ! "$S" =~ ^[0-9]+$ || ! "$M" =~ ^[0-9]+$ ]]; then
    echo "S e M devono essere entrambi interi positivi"; exit 2
fi

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

log="$HOME"/$$"$fileToSearch".log
> "$log"

#attivo comando ricorsivo per M volte a intervalli di S secondi
count=0
while [ $count -lt $M ]; do
    
    echo "giro numero $(expr $count + 1) dei $M giri"
    for dir in "$@"; do
        sleep $S
        echo "invocazione comando ricorsivo su $dir"
        "$recursive_cmd" "$fileToSearch" "$dir" "$log"
    done
    ((count++))
done
