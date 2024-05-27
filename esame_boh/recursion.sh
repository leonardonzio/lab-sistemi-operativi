#!/bin/bash

cd "$3"

recursion="$0"
stringa="$1"
dirOut="$2"

tot=0
count_stringa=0

for f in *; do
    if [[ -f "$f" ]]; then
        
        ((tot++))
        n_word=$(cat "$f" | grep -o "$stringa" | wc -l | awk '{print $1}')

        if [[ $n_word -ne 0 ]]; then
            ((count_stringa++))
        fi

        half=$(($tot / 2))
        if [[ $count_stringa -gt $half ]]; then
            $(echo "$3 $count_stringa" >> "$dirOut"/report.$4.out)
        fi

    elif [[ -d "$f" ]]; then
        
        echo "chiamata ricorsiva su: $(pwd)/$f"
        "$recursion" "$stringa" "$dirOut" "$f" $$
    fi
done










