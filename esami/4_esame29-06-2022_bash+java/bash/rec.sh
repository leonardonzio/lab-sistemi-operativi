#!/bin/bash

xUser="$1"
yUser="$2"
est="$3"
dir="$4"

cd "$dir"

nX=0
nY=0
for file in *; do

    if [[ -f "$file" && "$file" = *"$est" ]]; then
        
        owner=$(ls -l "$file" | awk '{print $3}')
        if [[ "$owner" = "$xUser" ]]; then
            ((nX++))
        elif [[ "$owner" = "$yUser" ]]; then
            ((nY++))
        fi

    elif [ -d "$file" ]; then
        echo "invoco ricorsivamente su $(pwd)/$file"
        "$0" "$xUser" "$yUser" "$est" "$file"
    fi
done

if [[ $nX -gt $nY ]]; then
    echo "$(pwd) $(expr $nX - $nY)" >> "$HOME"/report
fi


