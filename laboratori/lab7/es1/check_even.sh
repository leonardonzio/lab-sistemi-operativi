#!/bin/bash

dir="$1"
Fout="$2"

cd "$dir"
count=0
for file in *; do
    if [ -f "$file" ]; then
        N=$(wc -w $file | awk '{print $1}')
        
        # pari
        if [ $(expr $N % 2) = 0 ]; then
            echo "il file $(pwd)/$file contiene $N parole" >> "$Fout"
            ((count++))
        fi
    elif [ -d "$file" ]; then
        echo "invoco ricorsivamente su $(pwd)/$file"
        "$0" "$file" "$Fout"
    fi
done

echo "directory $(pwd) ci sono $count file con un numero pari di parole"
