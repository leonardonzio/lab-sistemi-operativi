#!/bin/bash
if [[ $# -ne 2 ]]; then
    echo "numero argomenti errato, devono essere 2: dir Fout"; exit 1
fi

dir="$1"
Fout="$2"
recursion=check_even.sh

if [[ "$dir" != /* || ! -d "$dir" ]]; then
    echo "$dir non e' un path assoluto oppure non esiste"; exit 2 
fi

if [[ $Fout != /* || -f "$Fout" ]]; then
    echo "$Fout non e' un path assoluto, oppure esiste già"; exit 3
fi


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

> "$2"


#attivo comando ricorsivo
echo "invoco comando ricorsivo su $dir"
"$recursive_cmd" "$1" "$2"
