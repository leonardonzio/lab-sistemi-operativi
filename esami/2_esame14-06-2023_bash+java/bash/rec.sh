#!/bin/bash

IN="$1"
OUT="$2"

cd "$IN"
for file in *; do

    if [[ -f "$file" && "$file" =~ prenot[0-9] ]]; then
        
        cliente=$(head -n 1 $file | awk '{print $2}')
        if [[ ! -d "$OUT/$cliente" ]]; then
            mkdir "$OUT"/"$cliente"
        fi
        cp "$file" "$OUT"/"$cliente"

    elif [ -d "$file" ]; then
        echo "invoco ricorsivamente su $(pwd)/$file"
        "$0" "$file" "$OUT" 
    fi
done

