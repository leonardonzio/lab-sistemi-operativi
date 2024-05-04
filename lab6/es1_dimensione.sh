#!/bin/bash

KB=$1
if [[ $# != 1 || ! "$KB" =~ ^[0-9]+$ ]]; then
    echo "devi passare un singolo parametro intero positivo!"; exit 1
fi

read -p "Scrivere il nome del file: " Fin
path_Fin="$HOME/$Fin"

#-f: exists, regular file
#-r: exists, read permission granted
if [[ ! -f "$path_Fin"  || ! -r "$path_Fin" ]]; then
    echo "deve essere un file esistente con permessi di lettura nella home!"; exit 1
fi

bytes_Fin=$(stat -c %s "$path_Fin")
KB_Fin=$(($bytes_Fin / 1024))

if [[ $KB_Fin < $KB ]]; then 
    echo "Il file $Fin ha dimensione $KB_Fin minore di $KB KByte"
elif [[ $KB_Fin > $KB ]]; then
	echo "Il file $Fin ha dimensione $KB_Fin maggiore di $KB KByte"
else
	echo "Il file $Fin ha dimensione $KB_Fin uguale a $KB KByte"
fi
