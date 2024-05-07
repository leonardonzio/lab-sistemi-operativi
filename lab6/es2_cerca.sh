#!/bin/bash

if [[ $# < 3 ]]; then
	echo "errore argomenti, devono essere almeno 3"; exit 1
fi

Fout="$1"
if [[ "$Fout" != /* || -e "$Fout" ]]; then
	echo "Fout deve essere un path assoluto di un file non esistente"; exit 1
fi

N=$2
if [[ ! "$N" =~ ^[0-9]+$ ]]; then
    echo "N deve essere un intero positivo!"; exit 1
fi


i=3
max_c=0
max_name=""
n_files=0
while [[ "$i" -le "$#" ]]; do
	
	d="${!i}"
	if [[ "$d" != /* || ! -d "$d" ]]; then
    	echo "Il percorso non è assoluto o la dir non esistente"; exit 1
	fi

	#ispeziono
	for f in $d/*; do
		
		if [[ "$(stat -c %U $f)" != "$USER" || -d "$f" ]]; then
			continue
		fi

		((n_files++))
		last_n_lines=$(tail -n $N "$f")
		curr_c=$(echo -n "$last_n_lines" | wc -c)
		
		if [[ curr_c -gt max_c ]]; then
			max_c=$curr_c
			max_name="$f"
		fi
	done

	((i++))
done

$(echo $n_files > $Fout)
echo "Nome assoluto del file col maggior numero di caratteri nelle ultime $N righe: $max_name"


