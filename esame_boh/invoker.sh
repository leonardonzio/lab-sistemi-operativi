#!/bin/bash

if [[ $# -lt 3 ]]; then
    echo "numero argomenti errato"; exit 1
fi

for d in "${@:2}"; do
    if [[ "$d" -ne /* ]]; then
        echo "non e' un path assoluto"; exit 2;
    fi

    if [[ ! -d "$d" ]], then
        echo "$d non e' directory"; exit 3
    fi
done

##########
recursion=recursion.sh

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
    recursive_cmd=$recursion
fi


######


#attivo comando ricorsivo su ogni directory fornita
for d in "${@:2}"; do
    
    echo "invocazione ricorsione in $d"
    "$recursive_cmd" "$1" "$2" "$d" $$
done