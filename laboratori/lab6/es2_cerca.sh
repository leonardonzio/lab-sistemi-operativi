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
#fino a che non arrivo all'ultimo direttorio
while [[ "$i" -le "$#" ]]; do
	
	d="${!i}"
	if [[ "$d" != /* || ! -d "$d" ]]; then
    	echo "Il percorso non è assoluto o la dir non esistente"; exit 1
	fi

	#ispeziono
	for f in $d/*; do
		
		#salto direttori o file non dell'utente
		if [[ "$(stat -c %U $f)" != "$USER" || -d "$f" ]]; then
			continue
		fi

		#traccio numero file ispezionati
		((n_files++))

		#conto caratteri delle ultime N righe
		curr_c=$(tail -n $N "$f" | wc -c | awk '{print $1}')
		
		if [[ curr_c -gt max_c ]]; then
			max_c=$curr_c
			max_name="$f"
		fi
	done

	#avanzo l'indice del direttorio
	((i++))
done

$(echo $n_files > $Fout)
echo "Nome assoluto del file col maggior numero di caratteri nelle ultime $N righe: $max_name"


