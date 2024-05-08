#!/bin/bash

for f in *; do
    
    if [[ ! -d "$1" ]]; then    
        n_words=$(wc -w "$f" | awk '{print $1}')
        if [[ (n_words % 2) = 0 ]]; then
            echo "Il file $(pwd)/$f contiene $n_words parole"
        fi

    else
        cd $1

    fi

    

done




if [[ ! -d "$1" ]]; then

else
    cd "$1"
    for f in *; do
        "$0" "$f"
    done
fi


