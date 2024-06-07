#!/bin/bash


stringa="$1"
reportAbs="$2"
dir="$3"

cd "$dir"

X=0
Y=0
for file in *; do

    if [[ -f "$file" ]]; then
        ((X++))
        
        howMany=$(grep -c -o "$stringa" "$reportAbs")
        if [[ $howMany -ge 1 ]]; then
            ((Y++))
        fi
        
    elif [ -d "$file" ]; then
        echo "invoco ricorsivamente su $(pwd)/$file"
        "$0" "$1" "$2" "$file"
    fi
done


if [[ $Y -gt $(expr $X/2) ]]; then
    echo "$dir $Y" >> "$reportAbs"
fi

