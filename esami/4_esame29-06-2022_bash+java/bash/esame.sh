#!/bin/bash

if [[ $# -ne 4 ]]; then
    echo "numero argomenti errato, devono essere 4: xUser, yUser, est, dir "; exit 1
fi

xUser="$1"
yUser="$2"
est="$3"
dir="$4"
recursion=rec.sh

if [[ "$est" != .* ]]; then
    echo "$est deve iniziare per . e deve essere un estensione di file"; exit 2
fi

if [[ ! -d "$dir" ]]; then
    echo "$dir deve essere una directory esistente"; exit 3
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

> "$HOME"/report

echo "invocazione comando ricorsivo su $dir"
"$recursive_cmd" "$xUser" "$yUser" "$est" "$dir"

