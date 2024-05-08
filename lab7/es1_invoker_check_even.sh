#!/bin/bash

if [[ $# != 2 ]]; then
    echo "numero argomenti errato"; exit 1
fi

dir=$1
Fout=$2
if [[ $dir != /* || ! -d $dir ]]; then
    echo "dir deve essere un path assoluto di un direttorio"; exit 1
fi

if [[ $Fout != /* || -e $Fout ]]; then
    echo "il file non deve esistere e deve essere un path assoluto"; exit 1
fi

if [[ "$0" = /* ]]; then
    
    #se $0 è un path assoluto
    dir_name=$(dirname "$0")
    recursive_cmd="$dir_name/es1_check_even.sh"
elif [[ "$0" = */* ]]; then
    
    #se c'è uno slash, ma non inizia con / --> $0 è un path relativo
    dir_name=$(dirname "$0")
    recursive_cmd="$(pwd)/$dir_name/es1_check_even.sh"
else
    #non si tratta nè di un path relativo, nè di un assoluto,
    #il comando $0 sarà cercato in $PATH.
    recursive_cmd=es1_check_even.sh
fi

#attivo comando ricorsivo
"$recursive_cmd" "$1" "$2"

